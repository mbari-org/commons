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

import org.junit.jupiter.api.Test;

/**
 * @author Brian Schlining
 * @since 2012-05-15
 */
public class DoubleMathTest {

    @Test
    public void testIsEven() {
        assertTrue(DoubleMath.isEven(2));
        assertTrue(DoubleMath.isEven(200));
        assertTrue(DoubleMath.isEven(-200));
        assertTrue(!DoubleMath.isEven(3));
        assertTrue(!DoubleMath.isEven(-3));
    }

    @Test
    public void testSign() {
        assertTrue(1 == DoubleMath.sign(1));
        assertTrue(-1 == DoubleMath.sign(-1));
        assertTrue(1 == DoubleMath.sign(10000D));
        assertTrue(-1 == DoubleMath.sign(-1234.45));
    }
}
