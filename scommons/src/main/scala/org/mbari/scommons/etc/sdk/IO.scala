/*
 * Copyright (c) Monterey Bay Aquarium Research Institute 2021
 *
 * FathomNet code is non-public software. Unauthorized copying of this file,
 * via any medium is strictly prohibited. Proprietary and confidential.
 * Written by: Brian Schlining <brian@mbari.org>
 */

package org.mbari.scommons.etc.sdk

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

type IO[A, B]      = A => Either[Throwable, B]
type AsyncIO[A, B] = A => Future[B]

object IO:

    extension [A, B](io: IO[A, B])

        def unit: IO[A, Unit] = a => Right(())

        def flatMap[C](f: IO[B, C]): IO[A, C] = a => io(a).flatMap(b => f(b))

        def map[C](f: B => C): IO[A, C] = a => io(a).map(f)

        def foreach(f: B => Unit): IO[A, Unit] = a =>
            for b <- io(a)
            yield f(b)

        def async(using executionContext: ExecutionContext): AsyncIO[A, B] = a =>
            Future(io(a)).map {
                case Right(b) => b
                case Left(e)  => throw e
            }
