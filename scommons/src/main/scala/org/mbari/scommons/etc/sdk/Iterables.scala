package org.mbari.scommons.etc.sdk

import scala.concurrent.Future
import org.mbari.scommons.etc.jdk.Futures.*
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext

object Iterables:

    extension [A, B <: Iterable[A]](xs: B)
        def parMap[C](f: A => C)(using executionContext: ExecutionContext): Iterable[C] =
            xs.map(x => Future { f(x) })
                .map(_.join(Duration.Inf))
