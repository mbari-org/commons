/* (C)2022 */
package org.mbari.jcommons.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.Socket;
import java.util.Collection;

/**
 * Tools for checking if an ActiveAppBeacon is running.
 *
 * <p>Created by brian on 6/17/14.
 */
public class ActiveAppPinger {

    public static boolean pingAll(Collection<Integer> ports, String expectedMsg) {
        boolean beaconExists = false;
        for (Integer p : ports) {
            beaconExists = ping(p, expectedMsg);
            if (beaconExists) {
                break;
            }
        }
        return beaconExists;
    }

    public static boolean ping(int port, final String expectedMsg) {
        boolean beaconExists = false;
        try {
            Socket socket = new Socket("localhost", port);
            final BufferedReader reader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final OutputStream output = socket.getOutputStream();
            output.write("ping\n".getBytes());
            String response = reader.readLine();
            beaconExists = response.equals(expectedMsg);
            socket.close();
        } catch (Exception e) {
            Logger log = System.getLogger(ActiveAppPinger.class.getName());

            log.log(Level.DEBUG, "Failed to connect to port " + port, e);
        }
        return beaconExists;
    }
}
