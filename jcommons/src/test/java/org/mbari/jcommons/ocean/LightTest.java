/* (C)2023 */
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
