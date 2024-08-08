/*
 * Copyright (c) Monterey Bay Aquarium Research Institute 2021
 *
 * FathomNet code is non-public software. Unauthorized copying of this file,
 * via any medium is strictly prohibited. Proprietary and confidential.
 * Written by: Brian Schlining <brian@mbari.org>
 */

package org.mbari.scommons.etc.sdk

import java.util.Optional

object Eithers:

    private val emptyOptionalError = new NoSuchElementException("Optional is empty")

    /**
     * Helper to traverse a sequence of items that can fail
     *
     * @param seq
     *   The sequence of items
     * @param f
     *   The function to apply to each item in the sequence
     * @return
     *   A sequence of items that have been transformed by the function
     */
    def traverse[A, B](seq: Seq[A])(f: A => Either[Throwable, B]): Either[Throwable, Seq[B]] =
        seq.foldLeft(Right(Seq.empty): Either[Throwable, Seq[B]]) { (acc, a) =>
            for
                xs <- acc
                x  <- f(a)
            yield xs :+ x
        }

    extension [B](opt: Optional[B])
        def toEither: Either[Throwable, B] =
            if opt.isPresent then Right(opt.get)
            else Left(emptyOptionalError)
