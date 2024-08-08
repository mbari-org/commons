package org.mbari.scommons.etc.jdk

import java.time.Duration as JDuration
import java.util.concurrent.{Executors, TimeUnit}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration as SDuration
import scala.util.Try
import scala.jdk.DurationConverters.*

object Futures:
    val DefaultTimeout = JDuration.ofSeconds(10)

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
    def safeRunSync[T](f: => Future[T], timeout: JDuration)(using ec: ExecutionContext): Either[Throwable, T] =
        Try(Await.result(f, SDuration(timeout.toMillis, TimeUnit.MILLISECONDS))).toEither

    extension [T](f: Future[T])
        def join: T                                                                                            = join(DefaultTimeout)
        def join(duration: JDuration): T                                                                       = join(duration.toScala)
        def join(duration: SDuration): T                                                                       = Await.result(f, duration)
        def safeRunSync(timeout: JDuration = DefaultTimeout)(using ec: ExecutionContext): Either[Throwable, T] =
            Futures.safeRunSync(f, timeout)
