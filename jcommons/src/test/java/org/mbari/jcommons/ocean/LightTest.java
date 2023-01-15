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
package org.mbari.jcommons.ocean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mbari.jcommons.ocean.Light.*;

import org.junit.jupiter.api.Test;

/**
 * @author Brian Schlining
 * @since 2011-12-26
 */
public class LightTest {

    @Test
    public void testC2T() {
        double a = c2t(0.045);
        assertEquals(95.5997, a, 0.0001);
        double b = c2t(0.045, 0.25);
        assertEquals(98.8813, b, 0.0001);
    }
}
