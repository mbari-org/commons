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
