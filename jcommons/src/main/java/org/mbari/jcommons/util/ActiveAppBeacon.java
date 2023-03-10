/* 
Copyright Monterey Bay Aquarium Research Institute (MBARI) 2022

MBARI licenses this file to you under the Apache License, 
Version 2.0 (the "License"); you may not use this file except in
compliance with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.   
*/
package org.mbari.jcommons.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * ActiveAppBeacon is a beacon that can be attached to a port and will ping a message when any
 * application connects to that port. It's useful for letting other apps know that a particular app
 * is already running.
 *
 * <p>The beacon accepts a collection of ports to attempt to connect to and will open a ServerSocket
 * on the first port in the collection that is available.
 *
 * <p>Example usage:
 *
 * <pre>
 *  // --- Shared properties both the beacon and pinger will need
 *  // These are the potential ports the beacon could be on.
 *  var BEACON_PORTS = Lists.newArrayList(4002, 4121, 5097, 6238, 6609,
 *          7407, 8169, 9069, 9669, 16569);
 *  // The unique-ish message that the that is checked for by the pinger.
 *  String BEACON_MESSAGE = "My Awesome App";
 *
 *  // Create a beacon
 *  ActiveAppBeacon beacon = new ActiveAppBeacon(BEACON_PORTS, BEACON_MESSAGE);
 *
 *  // Another app can check for the existence of the beacon on the same computer using:
 *  boolean beaconExists = ActiveAppPinger.pingAll(BEACON_PORTS, BEACON_MESSAGE);
 *
 *  // You can terminate a beacon at any time using:
 *  beacon.kill();
 *
 * </pre>
 *
 * Created by brian on 6/17/14.
 */
public class ActiveAppBeacon {

    private final Logger log = System.getLogger(getClass().getName());

    /** The minimum number of server port number. */
    public static final int MIN_PORT_NUMBER = 1;

    /** The maximum number of server port number. */
    public static final int MAX_PORT_NUMBER = 49151;

    private ServerSocket serverSocket;
    private final Thread beaconThread;
    private volatile boolean ok = true;
    private final String pingMessage;

    public ActiveAppBeacon(Collection<Integer> ports, final String pingMessage) {
        this.pingMessage = pingMessage;
        for (Integer p : ports) {
            // Check that port is unused and use first available port
            if (available(p)) {
                try {
                    serverSocket = new ServerSocket(p);
                    break;
                } catch (IOException e) {
                    log.log(Level.INFO, "Unable to open port {}", p);
                }
            }
        }

        if (serverSocket == null) {
            ok = false;
            String portMsg =
                    ports.stream().map(i -> i.toString()).collect(Collectors.joining(", "));
            beaconThread = null;
            log.log(
                    Level.WARNING,
                    "Failed to activate a ServerSocket for ActiveAppBeacon on any of the following"
                            + " ports: "
                            + portMsg);
        } else {
            final ServerSocket s = serverSocket;
            beaconThread =
                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    while (ok) {
                                        try {
                                            Socket socket = s.accept();
                                            Writer writer =
                                                    new OutputStreamWriter(
                                                            socket.getOutputStream());
                                            writer.write(pingMessage + "\n");
                                            writer.flush();
                                            socket.close();
                                        } catch (IOException e) {
                                            // do nothing
                                        }
                                    }
                                }
                            });
            beaconThread.setDaemon(true);
            beaconThread.start();
        }
    }

    public void kill() {
        ok = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                log.log(
                        Level.DEBUG,
                        "The beacon's ServerSocket threw an exception while killing the beacon. "
                                + "Nothing to see here. Just letting you know");
            }
        }
    }

    /**
     * Checks to see if a specific port is available.
     *
     * @param port the port to check for availability
     */
    public static boolean available(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    public boolean isAlive() {
        return ok;
    }
}
