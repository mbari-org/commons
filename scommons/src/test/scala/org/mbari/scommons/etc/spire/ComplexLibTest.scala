package org.mbari.scommons.etc.spire

import spire.implicits._
import org.apache.commons.math3.complex.{Complex => AComplex}
import org.junit.Assert._
import org.junit.Test
import spire.math.Complex

class ComplexLibTest extends munit.FunSuite:

  private val spire     = Seq(Complex(3d, 4d), Complex(8d, -3d))
  private val commons   = Seq(new AComplex(3d, 4d), new AComplex(8d, -3d))
  private val tolerance = 0.00000000001

  test("mod") {
    spire.indices.foreach { i =>
      compare(ComplexLib.mod(spire(i)), new AComplex(commons(i).abs(), 0))
    }
  }

  test("exp") {
    spire.indices.foreach { i =>
      compare(ComplexLib.exp(spire(i)), commons(i).exp())
    }
  }

  test("log") {
    spire.indices.foreach { i =>
      compare(ComplexLib.log(spire(i)), commons(i).log())
    }
  }

  test("sqrt") {
    spire.indices.foreach { i =>
      compare(ComplexLib.sqrt(spire(i)), commons(i).sqrt())
    }
  }

  test("sin") {
    spire.indices.foreach { i =>
      compare(ComplexLib.sin(spire(i)), commons(i).sin())
    }
  }

  test("cos") {
    spire.indices.foreach { i =>
      compare(ComplexLib.cos(spire(i)), commons(i).cos())
    }
  }

  test("sinh") {
    spire.indices.foreach { i =>
      compare(ComplexLib.sinh(spire(i)), commons(i).sinh())
    }
  }

  test("cosh") {
    spire.indices.foreach { i =>
      compare(ComplexLib.cosh(spire(i)), commons(i).cosh())
    }
  }

  test("tan") {
    spire.indices.foreach { i =>
      compare(ComplexLib.tan(spire(i)), commons(i).tan())
    }
  }

  test("fft") {
    val d = (1 to 10).map(i => Complex(i.toDouble, 0d)).toArray
    val a = ComplexLib.fft(d)
    val e = Array(
      Complex(55d, 0d),
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

    val a2 = ComplexLib.fft(a)
    def e2 = Seq(
      Complex(10d, 0d),
      Complex(100d, 0d),
      Complex(90d, 0d),
      Complex(80d, 0d),
      Complex(70d, 0d),
      Complex(60d, 0d),
      Complex(50d, 0d),
      Complex(40d, 0d),
      Complex(30d, 0d),
      Complex(20d, 0d)
    )
    a2.indices.foreach { i =>
      compare(e2(i), a2(i))
    }
  }

  test("ifft") {
    val d = (1 to 10).map(i => Complex(i.toDouble, 0d)).toArray
    val a = ComplexLib.ifft(ComplexLib.fft(d), true)
    a.indices.foreach { i =>
      compare(d(i), a(i))
    }
  }

  private def compare(s: Complex[Double], c: AComplex): Unit =
    assertEqualsDouble(s.real, c.getReal, tolerance)
    assertEqualsDouble(s.imag, c.getImaginary, tolerance)

  private def compare(s: Complex[Double], c: Complex[Double]): Unit =
    assertEqualsDouble(s.real, c.real, tolerance)
    assertEqualsDouble(s.imag, c.imag, tolerance)
