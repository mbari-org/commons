package org.mbari.scommons.math

import org.junit.Assert.*

class ProbabilitiesTest extends munit.FunSuite:

  val x = (1 to 100).map(_.toDouble).toArray

  test("quantile") {
    val correct = Array(3.0, 25.5, 50.5, 75.5, 98.0)
    val q       = Matlib.quantile(x, Array(0.025, 0.25, 0.50, 0.75, 0.975))
    assertArrayEquals(correct, q, 0.00001)

  }
