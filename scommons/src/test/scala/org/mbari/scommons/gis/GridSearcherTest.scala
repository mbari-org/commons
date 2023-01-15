package org.mbari.scommons.gis

/**
 * @author
 *   Brian Schlining
 * @since 2013-05-21
 */
class GridSearcherTest extends munit.FunSuite:

  val url       = getClass.getResource("/mockup.asc")
  val tolerance = 0.00000001

  test("search") {
    val grid         = ASCGridReader.read(url)
    val gridSearcher = new GridSearcher(grid)

    val p0 = gridSearcher.search(401111.0, 3001111.0).get
    assertEqualsDouble(p0.x, 2, tolerance)
    assertEqualsDouble(p0.y, 9, tolerance)
    assertEqualsDouble(grid(p0.x, p0.y), 22.0, tolerance)

    val p1 = gridSearcher.search(400400, 3004567).get
    assertEqualsDouble(p1.x, 1, tolerance)
    assertEqualsDouble(p1.y, 2, tolerance)
    assertEqualsDouble(grid(p1.x, p1.y), 91.0, tolerance)

  }
