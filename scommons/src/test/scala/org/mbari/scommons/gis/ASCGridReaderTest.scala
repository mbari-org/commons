package org.mbari.scommons.gis

import org.mbari.scommons.math.Matlib.*

import org.junit.Assert.*

/**
 * @author
 *   Brian Schlining
 * @since 2013-05-21
 */
class ASCGridReaderTest extends munit.FunSuite:

  val url       = getClass.getResource("/mockup.asc")
  val tolerance = 0.00000001

  test("read") {
    val grid = ASCGridReader.read(url)
  }

  test("x") {
    val grid     = ASCGridReader.read(url)
    assertEquals(grid.x.size, 9)
    val expected = linspace(400000.0000000, 400000.0000000 + 500.0 * 8, 9).toIndexedSeq
    assertEquals(grid.x, expected.toSeq, tolerance)
  }

  test("y") {
    val grid     = ASCGridReader.read(url)
    assertEquals(grid.y.size, 12)
    val expected = linspace(3000000.0000000, 3000000.0000000 + 500.0 * 11, 12).toIndexedSeq.reverse
    assertEquals(grid.y, expected.toSeq, tolerance)
  }

  test("read the nodata values correctly") {
    val grid = ASCGridReader.read(url)
    assert(grid.z(8, 0).isNaN)
  }

  test("z") {
    val grid = ASCGridReader.read(url)
    assertEqualsDouble(grid.z(0, 0), 110.0, tolerance)
    assertEqualsDouble(grid.z(1, 1), 101.0, tolerance)
    assertEqualsDouble(grid.z(2, 8), 32.0, tolerance)
  }
