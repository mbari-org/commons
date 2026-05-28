package org.mbari.scommons.etc.jdk

import java.time.Duration

object DurationConverter:

    /**
     * Parse a duration from a string of "hh:mm:ss.sss"
     */
    def fromHMS(hms: String): Duration =
        val parts = hms.split(":")
        val h     = parts(0).toInt
        val m     = parts(1).toInt
        val ms    = math.round(parts(2).toDouble * 1000L)
        Duration
            .ofMillis(ms)
            .plusHours(h)
            .plusMinutes(m)

    extension (d: Duration)
        def asScala: scala.concurrent.duration.Duration =
            scala.concurrent.duration.Duration.fromNanos(d.toNanos)

        /**
         * Format a duration as hh:mm:ss.sss
         */
        def toHMS: String =
            val h  = d.toHoursPart
            val m  = d.toMinutesPart
            val s  = d.toSecondsPart
            val ms = d.toMillisPart
            f"$h%02d:$m%02d:$s%02d.$ms%03d"

    extension (d: scala.concurrent.duration.Duration)
        def asJava: Duration =
            Duration.ofNanos(d.toNanos)
