/* (C)2023 */
package org.mbari.jcommons.math;

import static org.junit.jupiter.api.Assertions.*;
import static org.mbari.jcommons.math.Matlib.*;
import static org.mbari.jcommons.math.Statlib.*;

import org.junit.jupiter.api.Test;

/**
 * @author Brian Schlining
 * @since 2011-12-22
 */
public class HistcTest {

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
    public void test01() {
        double[] edges = linspace(0, 10, 11);
        double[] data = {1D, 3D, 5D, 7D};
        double[] histogram = histc(data, edges);
        assertEquals(edges.length, histogram.length);
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
     * <p>>> n = histc(a, b)
     *
     * <p>n =
     *
     * <p>4 1 0 0 0 0 0 0 0 0 0
     */
    @Test
    public void test02() {
        double[] centers = linspace(0, 100, 11);
        double[] data = {-1D, 1D, 3D, 5D, 7D, 11D, 101D};
        double[] histogram = histc(data, centers);
        for (int i = 0; i < histogram.length; i++) {
            if (i == 0) {
                assertEquals(4D, histogram[i], tolerance);
            } else if (i == 1) {
                assertEquals(1D, histogram[i], tolerance);
            } else {
                assertEquals(0D, histogram[i], tolerance);
            }
        }
    }
}
