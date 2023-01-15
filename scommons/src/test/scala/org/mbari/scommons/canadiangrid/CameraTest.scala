package org.mbari.scommons.canadiangrid

import scala.math.*

class CameraTest extends munit.FunSuite:

  private[this] val percentError = 0.03

  private[this] val camera = new Camera(112, toRadians(33.62), toRadians(42), toRadians(39))

  test("Should throw an IllegalArgumentException with invalid angles") {
    intercept[IllegalArgumentException] {
      new Camera(10, 0, 0.1, 0.1)
    }
    intercept[IllegalArgumentException] {
      new Camera(10, Pi + 0.1, 0.1, 0.1)
    }
    intercept[IllegalArgumentException] {
      new Camera(10, 0.1, 0.1, 0)
    }
    intercept[IllegalArgumentException] {
      new Camera(10, 0.1, 0.1, Pi / 2 + 0.1)
    }
  }

  test("planeDistance") {
    val expected = 138.3085
    val range    = expected * percentError
    assertEqualsDouble(camera.planeDistance, expected, range)
  }

  test("lensDistance") {
    val expected = 177.9698
    val range    = expected * percentError
    assertEqualsDouble(camera.lensDistance, expected, range)
  }

  test("nearViewEdgeDistance") {
    val expected = 76.1151
    val range    = expected * percentError
    assertEqualsDouble(camera.nearViewEdgeDistance, expected, range)
  }

  test("farViewEdgeDistance") {
    val expected = 274.4476
    val range    = expected * percentError
    assertEqualsDouble(camera.farViewEdgeDistance, expected, range)
  }

  test("nearViewEdgeDistance") {
    val expected = 76.1151
    val range    = expected * percentError
    assertEqualsDouble(camera.nearViewEdgeDistance, expected, range)
  }

  test("viewWidth") {
    val expected = 136.6324
    val range    = expected * percentError
    assertEqualsDouble(camera.viewWidth, expected, range)
  }

  test("viewHeight") {
    val expected = 198.3325
    val range    = expected * percentError
    assertEqualsDouble(camera.viewHeight, expected, range)
  }
