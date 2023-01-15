package org.mbari.scommons.canadiangrid

import scala.math.*

class PixelTest extends munit.FunSuite:

  private[this] val camera       = new Camera(112, toRadians(33.62), toRadians(42), toRadians(39))
  private[this] val corners      = Pixel.imageCorners(camera, 800, 600)
  private[this] val percentError = 0.01

  test("alpha") {
    val expected = 0.2932
    val range    = expected * percentError
    assertEqualsDouble(corners(0).alpha, expected, range)
    assertEqualsDouble(corners(1).alpha, expected, range)
    assertEqualsDouble(-corners(2).alpha, expected, range)
    assertEqualsDouble(-corners(3).alpha, expected, range)
  }

  test("beta") {
    val expected = 0.3665
    val range    = expected * percentError
    assertEqualsDouble(corners(0).beta, -expected, range)
    assertEqualsDouble(corners(1).beta, expected, range)
    assertEqualsDouble(corners(2).beta, expected, range)
    assertEqualsDouble(corners(3).beta, -expected, range)
  }

  test("xDistance") {
    val range   = 0.5
    val xTop    = 113.52
    val xBottom = 51.98
    assertEqualsDouble(corners(0).xDistance, -xTop, range)
    assertEqualsDouble(corners(1).xDistance, xTop, range)
    assertEqualsDouble(corners(2).xDistance, xBottom, range)
    assertEqualsDouble(corners(3).xDistance, -xBottom, range)
  }

  test("yDistance") {
    val range   = 0.5
    val yTop    = 274.45
    val yBottom = 76.12
    assertEqualsDouble(corners(0).yDistance, yTop, range)
    assertEqualsDouble(corners(1).yDistance, yTop, range)
    assertEqualsDouble(corners(2).yDistance, yBottom, range)
    assertEqualsDouble(corners(3).yDistance, yBottom, range)
  }
