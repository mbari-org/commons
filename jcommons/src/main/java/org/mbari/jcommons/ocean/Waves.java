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

import static java.lang.Math.*;

import org.mbari.jcommons.util.Tuple3;

/**
 * @author Brian Schlining
 * @since 2011-12-26
 */
public class Waves {

    /**
     * Ideal wave phase speed = f(Period, Depth)
     *
     * @param t = ideal wave period (seconds)
     * @param z = water depth (meters)
     * @return (c, Ld, L) where: C = wave phase speed (m/s), Ld = deepwater wavelength (M), and L =
     *     wavelength in water depth, Z
     */
    public static Tuple3<Double, Double, Double> celerity(double t, double z) {
        double ld = 9.8 * pow(t, 2) / (2 * PI); // deep water wavelength
        double c = sqrt(9.8 * ld / (2 * PI) * tanh(2 * PI * z / ld));
        double ll = c * t;
        return new Tuple3<>(c, ld, ll);
    }

    /**
     * Ideal wave phase speed = f(Period), Deep water approximation
     *
     * @param t = ideal wave period (seconds)
     * @return (c, Ld, L) where: C = wave phase speed (m/s), Ld = deepwater wavelength (M), and L =
     *     wavelength in deep water
     */
    public static Tuple3<Double, Double, Double> celerity(double t) {
        double ld = 9.8 * pow(t, 2) / (2 * PI); // deep water wavelength
        double c = 9.8 * t / (2 * PI);
        double ll = c * t;
        return new Tuple3<>(c, ld, ll);
    }
}
