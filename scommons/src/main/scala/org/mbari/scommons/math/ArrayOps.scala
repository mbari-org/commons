package org.mbari.scommons.math

object ArrayOps:

  extension (a: Array[Double])

    def +[@specialized(Int, Long, Float, Double) T: Numeric](s: Array[T]): Array[Double] =
      val numeric = implicitly[Numeric[T]]
      (for (i <- 0 until a.size) yield a(i) + numeric.toDouble(s(i))).toArray

    def -[@specialized(Int, Long, Float, Double) T: Numeric](s: Array[T]): Array[Double] =
      val numeric = implicitly[Numeric[T]]
      (for (i <- 0 until a.size) yield a(i) - numeric.toDouble(s(i))).toArray

    def *[@specialized(Int, Long, Float, Double) T: Numeric](s: Array[T]): Array[Double] =
      val numeric = implicitly[Numeric[T]]
      (for (i <- 0 until a.size) yield a(i) * numeric.toDouble(s(i))).toArray

    def /[@specialized(Int, Long, Float, Double) T: Numeric](s: Array[T]): Array[Double] =
      val numeric = implicitly[Numeric[T]]
      (for (i <- 0 until a.size) yield a(i) / numeric.toDouble(s(i))).toArray

    def subset(idx: Seq[Int]): Array[Double] = Matlib.subset(a, idx)

    def findIdx(fn: Double => Boolean): Array[Int] = Matlib.find(a, fn).toArray
