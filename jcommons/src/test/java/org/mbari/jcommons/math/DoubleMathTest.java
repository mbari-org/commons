/* (C)2023 */
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
