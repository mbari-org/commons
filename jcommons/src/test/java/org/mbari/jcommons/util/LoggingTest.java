/* (C)2023 */
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
