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
package org.mbari.jcommons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.System.Logger.Level;
import org.junit.jupiter.api.Test;

public class LoggingTest {

    private final Logging log = new Logging(getClass());

    @Test
    void testAtDebug() {
        log.atDebug().log("Debug");
    }

    @Test
    void testAtError() {
        log.atError().log("Error");
    }

    @Test
    void testAtInfo() {
        log.atInfo().log("Info");
    }

    @Test
    void testAtTrace() {
        log.atTrace().log("Trace");
    }

    @Test
    void testAtWarn() {
        log.atWarn().log("Warn");
    }

    @Test
    void testCause() {
        var ex = new RuntimeException();
        var logging = log.withCause(ex);
        assertEquals(ex, logging.cause());
    }

    @Test
    void testLevel() {
        var logging = log.atDebug();
        assertEquals(Level.DEBUG, logging.level());
    }

    @Test
    void testLogBySupplier() {
        log.atDebug().log(() -> "() -> Debug");
    }

    @Test
    void testWithCause() {
        log.atError().withCause(new RuntimeException()).log("Error with exception");
    }
}
