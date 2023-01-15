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
package org.mbari.jcommons.ocean;

/**
 * @author Brian Schlining
 * @since 2011-12-17
 */
public class Light {

    public static double c2t(double c) {
        return c2t(c, 1.0);
    }

    /**
     * Convert beam attenuation (/m) to percent beam transmission
     *
     * @param c beam attenuation coefficient (/m)
     * @param pathLength m (default = 1.0 m)
     * @return beam transmission (percentage per pathlength)
     */
    public static double c2t(double c, double pathLength) {
        return 100D * Math.exp(-c * pathLength);
    }

    /**
     * Convert beam transmission to beam attenuation
     *
     * @param t beam transmission (percentage per pathlength)
     * @param pathLength m (default = 1.0 m)
     * @return c beam attenuation coefficient (/m)
     */
    public static double t2c(double t, double pathLength) {
        return -(Math.log(t / 100D) / pathLength);
    }
}
