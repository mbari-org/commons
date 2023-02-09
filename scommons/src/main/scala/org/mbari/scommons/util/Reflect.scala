package org.mbari.scommons.util

import scala.reflect.ClassTag
import scala.jdk.CollectionConverters.*

object Reflect:

  /**
   * Create an instance of a class from a Map of parameters. The keys of the map must match the names of the constructor
   * parameters. This works for both case classes and regular classes.
   *
   * @param m
   *   The map of parameters
   * @tparam T
   *   The type of the class to create
   * @return
   *   A new instance of the class
   * @throws IllegalArgumentException
   *   if a required parameter is missing
   */
  def fromMap[T: ClassTag](m: Map[String, ?]): T =
    val classTag        = implicitly[ClassTag[T]]
    val constructor     = classTag.runtimeClass.getDeclaredConstructors.head
    val constructorArgs = constructor
      .getParameters()
      .map { param =>
        val paramName = param.getName
        if (param.getType == classOf[Option[?]])
          m.get(paramName)
        else
          m.get(paramName)
            .getOrElse(throw new IllegalArgumentException(s"Missing required parameter: $paramName"))
      }
    constructor.newInstance(constructorArgs: _*).asInstanceOf[T]

  /**
   * Create an instance of a class from a java.util.Map of parameters. The keys of the map must match the names of the
   * constructor parameters. This works for both case classes and regular classes.
   *
   * @param m
   *   The map of parameters
   * @tparam T
   *   The type of the class to create
   * @return
   *   A new instance of the class
   * @throws IllegalArgumentException
   *   if a required parameter is missing
   */
  def fromJavaMap[T: ClassTag](m: java.util.Map[String, ?]): T =
    fromMap(m.asScala.toMap)

  /**
   * Convert a case class to a Map of parameters. The keys of the map are the names of the constructor parameters.
   *
   * @param t
   *   The case class to convert
   * @tparam T
   *   The type of the case class
   * @return
   *   A Map of parameters
   */
  def toMap[T: ClassTag](t: T): Map[String, ?] =
    val classTag = implicitly[ClassTag[T]]
    val fields   = classTag.runtimeClass.getDeclaredFields
    fields.map { field =>
      field.setAccessible(true)
      field.getName -> field.get(t)
    }.toMap
