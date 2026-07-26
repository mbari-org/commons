/*
 * Copyright (c) Monterey Bay Aquarium Research Institute 2023
 *
 * scommons code is non-public software. Unauthorized copying of this file,
 * via any medium is strictly prohibited. Proprietary and confidential.
 */

package org.mbari.scommons.etc.sdk

import java.time.Duration
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration as SDuration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Try

/**
 * Concurrency utilities for simplifying the use of Futures for common cases.
 */
object Futures:

    val DefaultTimeout = Duration.ofSeconds(10)

    /**
     * This is similar to `Future.traverse` but it will not fail if one of the Futures fails. Instead, it will return a
     * sequence of results, omitting any that failed.
     * @param inputs
     *   The sequence of inputs to traverse
     * @param f
     *   A function that takes an input of type A and returns a Future of type B
     * @param ec
     *   The ExecutionContext to use for running the Futures
     * @tparam A
     *   The type of the input elements
     * @tparam B
     *   The type of the output elements
     * @return
     *   A Future that completes with a sequence of results of type B, omitting any that failed
     */
    def safeTraverse[A, B](inputs: Seq[A])(f: A => Future[B])(using ec: ExecutionContext): Future[Seq[B]] =
        val safeFutures: Seq[Future[Option[B]]] = inputs.map { a =>
            f(a).map(Some(_)).recover { case _ => None }
        }
        Future.sequence(safeFutures).map(_.flatten)

    /**
     * Run a Future and return the result or an Exception if the Future fails or does not complete within the timeout
     *
     * @param f
     *   A function that returns a Future
     * @param timeout
     *   The maximum amount of time to wait for the Future to complete
     * @tparam T
     *   The type that the Future returns
     * @return
     *   The result of the Future or an Exception
     */
    def safeRunSync[T](f: => Future[T], timeout: Duration)(using ec: ExecutionContext): Either[Throwable, T] =
        Try(Await.result(f, SDuration(timeout.toMillis, TimeUnit.MILLISECONDS))).toEither

    def join[T](f: => Future[T], timeout: Duration)(using ec: ExecutionContext): T =
        Await.result(f, SDuration(timeout.toMillis, TimeUnit.MILLISECONDS))

    extension [T](f: Future[T])
        def join(timeout: Duration = DefaultTimeout)(using ec: ExecutionContext): T                           = Futures.join(f, timeout)
        def safeRunSync(timeout: Duration = DefaultTimeout)(using ec: ExecutionContext): Either[Throwable, T] =
            Futures.safeRunSync(f, timeout)
        def handleError(default: => T)(using ec: ExecutionContext): Future[T]                                 = f.recover { case _ => default }
