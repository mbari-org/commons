package org.mbari.scommons.math

import org.junit.Assert.*
import spire.math.Complex

class MathemathicsTest extends munit.FunSuite:

  private[this] val math      = new Mathematics {}
  private[this] val tolerance = 0.000000001

  // wdata = [0:40 10:30 15:25 18:22 25 27 27 28 29 29 30 30 31 32 33 34 40 45 50];
  private[this] val wdata = Array[Double](0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
    22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
    20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 18, 19, 20, 21, 22, 25, 27,
    27, 28, 29, 29, 30, 30, 31, 32, 33, 34, 40, 45, 50)

  test("extrap1") {

    val x  = Array[Double](1, 2, 3, 4, 5)
    val y  = Array[Double](1, 2, 3, 4, 5)
    val xi = Array[Double](2, 4, 6, 8, 10)
    val yi = math.extrap1(x, y, xi)
    assertArrayEquals(Array[Double](2, 4, 6, 8, 10), yi, tolerance)

  }

  test("extrap1 with complexity") {
    val x  = Array[Double](1, 2, 3, 4, 5)
    val y  = Array[Double](1, 2, 8, 4, 5)
    val xi = Array[Double](2, 4, 6, 8, 10)
    val yi = math.extrap1(x, y, xi)
    assertArrayEquals(Array[Double](2, 4, 6, 8, 10), yi, tolerance)
  }

  test("extrap1 with more complexity") {
    val x  = Array[Double](1, 2, 3, 4, 5)
    val y  = Array[Double](1, 2, 8, 4, 8)
    val xi = Array[Double](2, 4, 6, 8, 10)
    val yi = math.extrap1(x, y, xi)
    assertArrayEquals(Array[Double](2, 4, 12, 20, 28), yi, tolerance)
  }

  test("diff") {

    val d = Array(1d, 2d, 4d)
    val a = math.diff(d)
    val e = Array(1d, 2d)
    e.indices.foreach { i =>
      assertEquals(e(i), a(i), tolerance)
    }

  }

  test("fft") {

    /*
        d = 1:10
        a = fft(d)
     */
    val d = (1 to 10).map(_.toDouble).toArray
    val a = math.fft(d)
    val e = Array(
      Complex(55d, 0.0d),
      Complex(-5d, 15.388417685876266),
      Complex(-5d, 6.881909602355867),
      Complex(-5.000000000000000, 3.632712640026803),
      Complex(-5.000000000000000, 1.624598481164532),
      Complex(-5d, 0d),
      Complex(-5.000000000000000, -1.624598481164532),
      Complex(-5.000000000000000, -3.632712640026803),
      Complex(-5.000000000000000, -6.881909602355867),
      Complex(-5.000000000000000, -15.388417685876266)
    )

    assertEquals(e.size, a.size)
    a.indices.foreach { i =>
      compare(e(i), a(i))
    }

  }

  def compare(s: Complex[Double], c: Complex[Double]): Unit =
    assertEqualsDouble(s.real, c.real, tolerance)
    assertEqualsDouble(s.imag, c.imag, tolerance)

  test("prod") {
    /*
        d = 1:10
        a = prod(d)
     */
    val d = (1 to 10).map(_.toDouble).toArray
    val a = math.prod(d)
    val e = 3628800d
    assertEqualsDouble(e, a, tolerance)
  }

  test("unique") {
    /*
        wdata = [0:40 10:30 15:25 18:22 25 27 27 28 29 29 30 30 31 32 33 34 40 45 50];
        [actual ia ic] = unique(wdata)
     */
    val expected = Array[Double](0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
      24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 45, 50)
    val eia      = Array[Int](1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 42, 43, 44, 45, 46, 63, 64, 65, 74, 75, 76, 77, 78, 71, 72, 79,
      58, 81, 82, 84, 86, 87, 88, 89, 90, 36, 37, 38, 39, 40, 91, 92, 93).map(
      _ - 1
    ) // Subtract 1 to go from matlab to JVM indices
    val eic = Array[Int](1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
      27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
      24, 25, 26, 27, 28, 29, 30, 31, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 19, 20, 21, 22, 23, 26, 28, 28, 29,
      30, 30, 31, 31, 32, 33, 34, 35, 41, 42, 43).map(_ - 1) // Subtract 1 to go from matlab to JVM indices

    val (actual, ia, ic) = math.unique(wdata)

    assertEquals(expected.size, actual.size)
    actual.indices.foreach { i =>
      assertEqualsDouble(expected(i), actual(i), tolerance)
    }

    assertEquals(eia.size, ia.size)
    eia.indices.foreach { i =>
      assertEquals(eia(i), ia(i))
    }

    assertEquals(eic.size, ic.size)
    eic.indices.foreach { i =>
      assertEquals(eic(i), ic(i))
    }
  }
