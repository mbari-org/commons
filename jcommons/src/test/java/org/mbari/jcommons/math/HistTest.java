/* 
Copyright Monterey Bay Aquarium Research Institute (MBARI) 2023

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
package org.mbari.jcommons.math;

import static org.junit.jupiter.api.Assertions.*;
import static org.mbari.jcommons.math.Matlib.*;
import static org.mbari.jcommons.math.Statlib.*;

import org.junit.jupiter.api.Test;

/**
 * @author Brian Schlining
 * @since 2011-12-22
 */
public class HistTest {

    double tolerance = 0.000000001;

    /**
     * In Matlab:
     *
     * <p>>> a = [1 3 5 7]
     *
     * <p>a =
     *
     * <p>1 3 5 7
     *
     * <p>>> b = 0:10
     *
     * <p>b =
     *
     * <p>0 1 2 3 4 5 6 7 8 9 10
     *
     * <p>>> n = hist(a, b)
     *
     * <p>n =
     *
     * <p>0 1 0 1 0 1 0 1 0 0 0
     */
    @Test
    public void testHist() {
        double[] centers = linspace(0, 10, 11);
        double[] data = {1D, 3D, 5D, 7D};
        double[] histogram = hist(data, centers, true);

        assertEquals(centers.length, histogram.length);
        for (int i = 0; i < histogram.length; i++) {
            if (i == 1 || i == 3 || i == 5 || i == 7) {
                assertEquals(1D, histogram[i], tolerance);
            } else {
                assertEquals(0D, histogram[i], tolerance);
            }
        }
    }

    /**
     * In Matlab: >> a = [1 3 5 7 11]
     *
     * <p>a =
     *
     * <p>1 3 5 7 11
     *
     * <p>>> b = 0:10:100
     *
     * <p>b =
     *
     * <p>0 10 20 30 40 50 60 70 80 90 100
     *
     * <p>>> n = hist(a, b)
     *
     * <p>n =
     *
     * <p>3 2 0 0 0 0 0 0 0 0 0
     */
    @Test
    public void testHist2() {
        double[] centers = linspace(0, 100, 11);
        double[] data = {-1D, 1D, 3D, 5D, 7D, 11D, 101D};
        double[] histogram = hist(data, centers, true);
        for (int i = 0; i < histogram.length; i++) {
            if (i == 0) {
                assertEquals(3D, histogram[i], tolerance);
            } else if (i == 1) {
                assertEquals(2D, histogram[i], tolerance);
            } else {
                assertEquals(0D, histogram[i], tolerance);
            }
        }
    }

    public void testHistc() {
        double[] data = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
            24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 18, 19, 20, 21, 22, 25, 27, 27, 28, 29, 29, 30, 30, 31, 32, 33, 34,
            40, 45, 50
        };
    }
}
