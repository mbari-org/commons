package org.mbari.scommons.math

object ScalarArrayOps:
  extension (a: Array[Double])

    def +[@specialized(Int, Long, Float, Double) T: Numeric](s: T): Array[Double] =
      val numeric = implicitly[Numeric[T]]
      val d       = numeric.toDouble(s)
      (for (i <- 0 until a.size) yield a(i) + d).toArray

    def -[@specialized(Int, Long, Float, Double) T: Numeric](s: T): Array[Double] =
      val numeric = implicitly[Numeric[T]]
      val d       = numeric.toDouble(s)
      (for (i <- 0 until a.size) yield a(i) - d).toArray

    def *[@specialized(Int, Long, Float, Double) T: Numeric](s: T): Array[Double] =
      val numeric = implicitly[Numeric[T]]
      val d       = numeric.toDouble(s)
      (for (i <- 0 until a.size) yield a(i) * d).toArray

    def /[@specialized(Int, Long, Float, Double) T: Numeric](s: T): Array[Double] =
      val numeric = implicitly[Numeric[T]]
      val d       = numeric.toDouble(s)
      (for (i <- 0 until a.size) yield a(i) / d).toArray
