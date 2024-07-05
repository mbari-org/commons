package org.mbari.scommons.util

import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.time.Instant
import scala.util.Try
import org.mbari.scommons.etc.jdk.Instants

@deprecated("Use org.mbari.scommons.Instants instead", "0.0.7")
object ISO8601:

    def parseIso8601(s: String): Either[Throwable, Instant] =
        Instants.parseIso8601(s) match
            case Some(i) => Right(i)
            case None    => Left(new IllegalArgumentException(s"Could not parse $s as an ISO8601 timestamp"))

    /**
     * Parse a string in common ISO8601 formats into an Instant
     * @param timestamp
     * @return
     */
    def parse(timestamp: String): Option[Instant] = Instants.parseIso8601(timestamp)
