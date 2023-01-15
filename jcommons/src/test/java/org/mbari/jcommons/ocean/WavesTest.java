/* (C)2023 */
package org.mbari.jcommons.ocean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mbari.jcommons.ocean.Waves.*;

import org.junit.jupiter.api.Test;
import org.mbari.jcommons.util.Tuple3;

/**
 * @author Brian Schlining
 * @since 2011-12-26
 */
public class WavesTest {

    @Test
    public void testCelerity() {
        Tuple3<Double, Double, Double> a = celerity(8, 15);
        assertEquals(10.7129, a.a(), 0.0001);
        assertEquals(99.8220, a.b(), 0.0001);
    }
}
