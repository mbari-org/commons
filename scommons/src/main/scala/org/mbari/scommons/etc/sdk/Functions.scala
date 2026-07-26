/*
 * Copyright (c) Monterey Bay Aquarium Research Institute 2023
 *
 * vars-cli code is non-public software. Unauthorized copying of this file,
 * via any medium is strictly prohibited. Proprietary and confidential.
 */

package org.mbari.scommons.etc.sdk

import java.time
import java.util.concurrent.{Executors, TimeUnit}
import scala.concurrent.duration.*
import scala.concurrent.{ExecutionContext, Future, Promise}
import java.util.concurrent.ThreadLocalRandom

/**
 * A simplified operation monad, representing a function that can fail. Designed for simplicity, not full IO monad
 * features. Uses synchronous operations, intended for use with virtual threads.
 * @tparam A
 *   The input type
 * @tparam B
 *   The output type
 */
type SyncOp[A, B] = A => Either[Throwable, B]

/**
 * An asynchronous version of SyncOp, returning a Future.
 * @tparam A
 *   The input type
 * @tparam B
 *   The output type
 */
type AsyncOp[A, B] = A => Future[B]

/**
 * Companion object for SyncOp, providing utility functions and extensions.
 *
 * Contains an extension method to convert Either to IO and IO to AsyncIO. Usage examples:
 *
 * ```scala
 * import org.mbari.vars.cli.etc.sdk.SyncOp
 * import org.mbari.vars.cli.etc.sdk.SyncOp.* // import the extension methods
 * import org.mbari.vars.cli.etc.sdk.AsyncOp
 * import org.mbari.vars.cli.etc.sdk.AsyncOp.* // import the extension methods
 *
 * val op: SyncOp[Int, String]       = SyncOp.pure("Hello")
 * val asyncOp: AsyncOp[Int, String] = op.async
 * ```
 */
object SyncOp:

    /**
     * Creates an SyncOp that always succeeds with Unit.
     */
    def unit[A]: SyncOp[A, Unit] = _ => Right(())

    /**
     * Creates an SyncOp that always fails with the given error.
     */
    def fail[A, B](error: Throwable): SyncOp[A, B] = _ => Left(error)

    /**
     * Creates an SyncOp that always succeeds with the given value.
     */
    def pure[A, B](b: B): SyncOp[A, B] = _ => Right(b)

    /**
     * Creates an SyncOp from a by-name Either, allowing for lazy evaluation.
     */
    def fromEither[A, B](either: => Either[Throwable, B]): SyncOp[A, B] = _ => either

    def attempt[A, B](f: A => B): SyncOp[A, B] =
        a =>
            try Right(f(a))
            catch case t: Throwable => Left(t)

    /**
     * Creates an SyncOp from an Option, using the provided error if the Option is None.
     */
    def fromOption[A, B](option: Option[B], error: => Throwable): SyncOp[A, B] =
        _ => option.toRight(error)

    /**
     * Traverses a list, applying the given SyncOp function to each element, ignoring the results. (The trailing
     * underscore in a function name means "ignore results"
     * @param list
     * @param f
     * @tparam A
     * @return
     */
    def traverse_[A](list: List[A])(f: SyncOp[A, Unit]): SyncOp[Any, Unit] =
        _ =>
            list.zipWithIndex.foldLeft(Right(()): Either[Throwable, Unit]) { case (acc, (a, idx)) =>
                for
                    _ <- acc
                    _ <- f(a).left.map(e => RuntimeException(s"Failed at index $idx: ${e.getMessage}", e))
                yield ()
            }

    extension [A, B](op: SyncOp[A, B])

        /**
         * Runs the SyncOp, producing the Either result.
         */
        def run(a: A): Either[Throwable, B] = op(a)

        /**
         * Chains this SyncOp with another, executing the second only if the first succeeds.
         */
        def flatMap[C](f: B => SyncOp[A, C]): SyncOp[A, C] = 
            a =>
                op(a) match
                    case Right(b) => f(b)(a)
                    case Left(e)  => Left(e)

        /**
         * Transforms the successful result of this SyncOp.
         */
        def map[C](f: B => C): SyncOp[A, C] =
            a =>
                op(a) match
                    case Right(b) =>
                        try Right(f(b))
                        catch case t: Throwable => Left(t)
                    case Left(e) =>
                        Left(e)

        def map2[C, D](op2: SyncOp[A, C])(f: (B, C) => D): SyncOp[A, D] = a =>
            for
                b <- op(a)
                c <- op2(a)
            yield f(b, c)

        /**
         * Performs a side effect with the successful result of this SyncOp.
         */
        def foreach(f: B => Unit): SyncOp[A, Unit] = a =>
            op(a) match
                case Right(b) =>
                    try { 
                        f(b)
                        Right(()) } 
                    catch 
                        case t: Throwable => Left(t)
                case Left(e)  => Left(e)

        /**
         * Converts this SyncOp to an AsyncOp (a function returning a Future).
         */
        def async(using executionContext: ExecutionContext): AsyncOp[A, B] = a =>
            Future {
                op(a) match
                    case Right(b) => b
                    case Left(e)  => throw e // Use throw e to reject the Future
            }

        /**
         * Handles the errors, and returns an SyncOp
         */
        def handleError(handler: Throwable => B): SyncOp[A, B] = a =>
            op(a) match
                case Left(e) => Right(handler(e))
                case r       => r

        /**
         * Handles the errors, and returns an SyncOp
         */
        def handleErrorWith(handler: Throwable => SyncOp[A, B]): SyncOp[A, B] = a =>
            op(a) match
                case Left(e) => handler(e)(a)
                case r       => r

        def withFilter(p: B => Boolean): SyncOp[A, B] = a =>
            op(a) match
                case Right(b) if p(b) => Right(b)
                case Right(_)         => Left(new NoSuchElementException("Filter failed"))
                case Left(e)          => Left(e)

object AsyncOp:

    /**
     * Creates an AsyncOp that always succeeds with the provided value.
     */
    def pure[B](b: B): AsyncOp[Any, B] = _ => Future.successful(b)

    def fromFuture[B](f: => Future[B]): AsyncOp[Any, B] = _ => f

    private lazy val scheduler =
        val exec = Executors.newSingleThreadScheduledExecutor()
        Runtime.getRuntime.addShutdownHook(Thread(() => exec.shutdown()))
        exec

    /**
     * Helper function to create a delayed Future.
     *
     * @param delay
     *   The delay duration.
     * @param block
     *   The block of code to execute after the delay.
     * @return
     *   A Future that completes after the delay with the result of the block.
     */
    private def after[T](delay: FiniteDuration)(block: => Future[T]): Future[T] =

        val promise = Promise[T]()
        scheduler.schedule(() => promise.completeWith(block), delay.toMillis, TimeUnit.MILLISECONDS)
        promise.future

    /**
     * Calculates backoff time with jitter.
     *
     * @param retries
     *   The number of retries that have been attempted
     * @param base
     *   The base backoff time in milliseconds (e.g. 1000ms)
     * @param cap
     *   The maximum backoff time in milliseconds (e.g. 60000ms)
     * @return
     *   The backoff time in milliseconds
     */
    private def calculateBackOffWithJitter(retries: Int, base: Int, cap: Int): Int =
        val exp        = 1 << retries // 2^retries
        val maxBackoff = math.min(cap, base * exp)
        ThreadLocalRandom.current().nextInt(maxBackoff)

    extension [A, B](op: AsyncOp[A, B])

        /**
         * Retries the AsyncOp operation a specified number of times with exponential backoff and jitter.
         * @param attemptsLeft
         *   The number of retry attempts to use
         * @param executionContext
         *   The execution context for running the Future
         * @return
         *   A new AsyncOp that will retry the original operation, if it fails, up to the specified number of atempts
         */
        def retry(attemptsLeft: Int)(using executionContext: ExecutionContext): AsyncOp[A, B] = 
            def loop(attemptsLeft: Int, attemptNum: Int)(using ec: ExecutionContext): AsyncOp[A, B] = a =>
                op(a).recoverWith {
                    case e if attemptsLeft == 0 => Future.failed(e)
                    case e                      =>
                        val delay = calculateBackOffWithJitter(attemptNum, 1000, 10000).millis
                        after(delay)(loop(attemptsLeft - 1, attemptNum + 1)(a))
                }
            loop(attemptsLeft, 1)

        private def retry(attemptsLeft: Int, attemptNum: Int = 1)(using ec: ExecutionContext): AsyncOp[A, B] = a =>
            op(a).recoverWith {
                case e if attemptsLeft == 0 => Future.failed(e)
                case e                      =>
                    val delay = calculateBackOffWithJitter(attemptNum, 1000, 10000).millis
                    after(delay)(op.retry(attemptsLeft - 1, attemptNum + 1)(a))
            }

        /**
         * Performs a side effect with the successful result of this AsyncOp.
         */
        def foreach(f: B => Unit)(using executionContext: ExecutionContext): AsyncOp[A, Unit] = a =>
            op(a).map { b =>
                f(b)
            }

        /**
         * Chains this AsyncOp with another, executing the second only if the first succeeds.
         * @param f
         *   A function that takes the result of the first AsyncOp and returns another AsyncOp
         * @tparam C
         *   The output type of the second AsyncOp
         * @return
         *   A new AsyncIO that represents the chained operations
         */
        def flatMap[C](f: B => AsyncOp[A, C])(using ExecutionContext): AsyncOp[A, C] = a => op(a).flatMap(b => f(b)(a))

        /**
         * Transforms the successful result of this AsyncOp.
         * @param f
         *   A function that transforms the result
         * @tparam C
         *   The output type after transformation
         * @return
         *   A new AsyncOp with the transformed result
         */
        def map[C](f: B => C)(using ExecutionContext): AsyncOp[A, C] = a => op(a).map(f)

        /**
         * Converts this AsyncOp to a synchronous SyncOp by blocking until the Future completes or the timeout is
         * reached.
         * @param timeout
         *   The maximum duration to wait for the Future to complete
         * @param executionContext
         *   The execution context for running the Future
         * @return
         *   An IO that, when run, will block until the Future completes or the timeout is reached
         */
        def join(timeout: time.Duration = time.Duration.ofSeconds(10))(using
            executionContext: ExecutionContext
        ): SyncOp[A, B] = a => Futures.safeRunSync(op(a), timeout)

        /**
         * Runs the AsyncOp, producing a Future result.
         * @param a
         *   The input value
         * @param executionContext
         *   The execution context for running the Future
         * @return
         *   A Future representing the result of the AsyncOp
         */
        def run(a: A)(using executionContext: ExecutionContext): Future[B] = op(a)

        def handleError(handler: Throwable => B)(using executionContext: ExecutionContext): AsyncOp[A, B] = a =>
            op(a).recover { case e => handler(e) }

        def handleErrorWith(handler: Throwable => AsyncOp[A, B])(using
            executionContext: ExecutionContext
        ): AsyncOp[A, B] = a => op(a).recoverWith { case e => handler(e)(a) }

