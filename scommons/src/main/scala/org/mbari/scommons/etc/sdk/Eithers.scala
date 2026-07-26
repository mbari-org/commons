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
     * Helper to traverse a sequence of items that can fail. If any item fails, the entire operation fails.
     *
     * @param seq
     *   The sequence of items
     * @param f
     *   The function to apply to each item in the sequence
     * @return
     *   A sequence of items that have been transformed by the function
     */
    def traverse[A, E, B](seq: Seq[A])(f: A => Either[E, B]): Either[E, Seq[B]] =
        seq.foldLeft(Right(Seq.empty): Either[E, Seq[B]]) { (acc, a) =>
            for
                xs <- acc
                x  <- f(a)
            yield xs :+ x
        }

    /**
     * Helper to safely traverse a sequence of items that can fail. Only the successful items are returned.
     * @param seq
     *   The sequence of items
     * @param f
     *   The function to apply to each item in the sequence
     * @param failHandler
     *   A function to handle failures
     * @return
     *   A sequence of successfully transformed items
     */
    def safeTraverse[A, B](seq: Seq[A], failHandler: Throwable => Unit = _ => ())(
        f: A => Either[Throwable, B]
    ): Seq[B] =
        val results = List.newBuilder[B]
        for x <- seq
        do
            f(x) match
                case Right(b)    => results += b
                case Left(error) => failHandler(error)
        results.result()

    extension [B](opt: Optional[B])
        /**
         * Convert an Optional to an Either. If the Optional is empty, the Either will be a Left with a
         * NoSuchElementException.
         *
         * @return
         *   An Either with the value of the Optional if it is present, or a NoSuchElementException if it is empty
         */
        def toEither: Either[Throwable, B] =
            if opt.isPresent then Right(opt.get)
            else Left(emptyOptionalError)

    extension [B](opt: Either[Throwable, B])

        /**
         * Allows you to filter on an Either in a for-comprehension.
         * @param p
         *   The predicate to filter on.
         * @return
         *   The filtered Either. If the Either is a Left, it will be returned as is. If the Either is a Right, it will
         *   be filtered. If the filter fails, a Left(NoSuchElementException) will be returned.
         */
        def withFilter(p: B => Boolean): Either[Throwable, B] =
            opt match
                case Right(b) if p(b) => Right(b)
                case Right(b)         => Left(new NoSuchElementException("Predicate does not hold for " + b))
                case Left(e)          => Left(e)

