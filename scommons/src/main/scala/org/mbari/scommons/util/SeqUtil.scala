package org.mbari.scommons.util

import scala.math._
object SeqUtil:

  /**
   * Rotates a sequence of items left or write. Useful for rotating through probabilities in Bayesian SLAMs
   *
   * @param p
   *   The list to rotate
   * @param u
   *   The number of spaces to rotate + is right/ - is left. If u is greater than the number of items in the list, the
   *   rotation will just __wrap__ around
   * @tparam A
   *   The type of the contents of the list
   * @return
   *   The rotated list
   */
  def rotate[A](p: Seq[A], u: Int): Seq[A] =
    if (u > 0)
      val v = u % p.size // Wraps in case u is greater than list size
      p.slice(v, p.size) ++ p.slice(0, v)
    else
      val v = abs(u) % p.size
      p.slice(p.size - v, p.size) ++ p.slice(0, p.size - v)
