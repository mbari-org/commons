package org.mbari.scommons.geometry

import scala.math._
import java.awt.geom.{Point2D => JPoint2D}

/**
 * 2-Dimensional point
 * @author
 *   Hohonuuli
 * @since 2011-05-14
 */
trait Point2D[A]:

  def x: A

  def y: A

  def distance(that: Point2D[A])(implicit numeric: Numeric[A]): Double =
    val dx = pow(numeric.toDouble(x) - numeric.toDouble(that.x), 2)
    val dy = pow(numeric.toDouble(y) - numeric.toDouble(that.y), 2)
    sqrt(dx + dy)

  def direction(that: Point2D[A])(implicit numeric: Numeric[A]): Double =
    atan2(
      numeric.toDouble(that.y) - numeric.toDouble(y),
      numeric.toDouble(that.x) - numeric.toDouble(x)
    )

  override lazy val toString: String = s"$x,$y"

  override def equals(obj: Any): Boolean = obj match
    case that: Point2D[_] => that.x.equals(x) && that.y.equals(y)
    case _                => false

  override def hashCode(): Int = toString.hashCode

class DoublePoint2D(val x: Double, val y: Double) extends Point2D[Double]:
  lazy val toInt: IntPoint2D =
    new IntPoint2D(round(x).toInt, round(y).toInt)

/**
 * @param x
 *   The x-coordinate
 * @param y
 *   The y-coordinate
 * @param label
 *   An integer label to use as an id for the point
 */
class LabeledDoublePoint2D(x: Double, y: Double, val label: Int) extends DoublePoint2D(x, y):
  override lazy val toInt: LabeledIntPoint2D =
    new LabeledIntPoint2D(
      round(x).toInt,
      round(y).toInt,
      label
    )

class IntPoint2D(val x: Int, val y: Int) extends Point2D[Int]:
  lazy val toDouble: DoublePoint2D =
    new DoublePoint2D(x.toDouble, y.toDouble)

/**
 * @param x
 *   The x-coordinate
 * @param y
 *   The y-coordinate
 * @param label
 *   An integer label to use as an id for the point
 */
class LabeledIntPoint2D(x: Int, y: Int, val label: Int) extends IntPoint2D(x, y):
  override lazy val toDouble: LabeledDoublePoint2D =
    new LabeledDoublePoint2D(x.toDouble, y.toDouble, label)

object Point2D:

  /**
   * Implicit conversion to Java Point2D
   */
  given doublePointConversion: Conversion[Point2D[Double], JPoint2D] with
    def apply(p: Point2D[Double]): JPoint2D = new JPoint2D.Double(p.x, p.y)

  /**
   * Implicit conversion to Java Point2D
   */
  given intPointConversion: Conversion[Point2D[Int], JPoint2D] with
    def apply(p: Point2D[Int]): JPoint2D = new JPoint2D.Float(p.x.toFloat, p.y.toFloat)

  /**
   * Convert a Java Point2D to a Point2D
   */
  def apply(point: JPoint2D) = new DoublePoint2D(point.getX, point.getY)

  /**
   * Factory method
   */
  def apply(x: Int, y: Int) = new IntPoint2D(x, y)

  def apply(x: Int, y: Int, label: Int) = new LabeledIntPoint2D(x, y, label)

  /**
   * Factory method
   */
  def apply(x: Double, y: Double) = new DoublePoint2D(x, y)

  def apply(x: Double, y: Double, label: Int) =
    new LabeledDoublePoint2D(x, y, label)

  /**
   * Factory method
   */
  def apply(x: Float, y: Float) = new DoublePoint2D(x.toDouble, y.toDouble)
