package org.mbari.scommons.math

import org.mbari.scommons.etc.jdk.Loggers.given
import java.lang.System.Logger.Level

/**
 * Runs a fast collation/modified nearest neighbor interpolation on two lists of values.
 *
 * @author
 *   Brian Schlining
 * @since 2015-03-02T16:20:00
 */
object FastCollator:

  private val log = System.getLogger(getClass.getName())

  /**
   * Runs a fast collation/modified nearest neighbor interpolation on two lists of values.
   *
   * @param a
   *   The first list of values
   * @param b
   *   The second list of values
   * @param tolerance
   *   The tolerance for matching values
   * @tparam A
   *   The type of the first list of values
   * @tparam B
   *   The type of the second list of values
   * @return
   *   A sequence of tuples where the first value is from the first list and the second value is from the second list.
   *   If the second value is None, then there was no match within the tolerance
   */
  def apply[A: Numeric, B: Numeric](
      a: Iterable[A],
      b: Iterable[B],
      tolerance: Double
  ): Seq[(A, Option[B])] =
    val numericA = implicitly[Numeric[A]]
    val numericB = implicitly[Numeric[B]]

    def fa(v: A) = numericA.toDouble(v)
    def fb(v: B) = numericB.toDouble(v)

    apply(a, fa, b, fb, tolerance)

  /**
   * Runs a fast collation/modified nearest neighbor interpolation on two lists of values.
   *
   * @param a
   *   The first list of values. It does not need to be sorted
   * @param fnA
   *   A function to transform the first list of values into doubles
   * @param b
   *   The second list of values. It does not need to be sorted
   * @param fnB
   *   A function to transform the second list of values into doubles
   * @param tolerance
   *   The tolerance for matching values
   * @tparam A
   *   The type of the first list of values
   * @tparam B
   *   The type of the second list of values
   * @return
   *   A sequence of tuples where the first value is from the first list and the second value is from the second list.
   *   If the second value is None, then there was no match within the tolerance
   */
  def apply[A, B](
      a: Iterable[A],
      fnA: A => Double,
      b: Iterable[B],
      fnB: B => Double,
      tolerance: Double
  ): Seq[(A, Option[B])] =

    val listA = a.toSeq.sortBy(fnA)(Ordering.Double.IeeeOrdering) // sorted d0
    val listB = b.toSeq.sortBy(fnB)(Ordering.Double.IeeeOrdering) // sorted d1

    val valuesA = listA.map(fnA).toArray // transformed d0 in same order as list0
    val valuesB = listB.map(fnB).toArray // transformed d1 in same order as list1

    if valuesB.max < valuesA.min || valuesB.min > valuesA.max then
      log
        .atWarn
        .log(
          "Unable to coallate data. There's no overlap between A and B. A: %f - %f, B: %f - %f".format(
            valuesA.min,
            valuesA.max,
            valuesB.min,
            valuesB.max
          )
        )

    val tmp =
      for (vA, iA) <- valuesA.zipWithIndex
      yield
        val iB      = Matlib.near(valuesB, vA, false)
        val nearest = if (iB >= 0)
          val vB = valuesB(iB)
          // println("A: " + vA + " -> " + " B: " + vB)
          if (math.abs(vA - vB) <= tolerance) Option(listB(iB))
          else None
        else None
        listA(iA) -> nearest
    tmp.toSeq
