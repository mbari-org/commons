package org.mbari.scommons.util

import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.time.Instant
import scala.util.Try

object ISO8601:

  private val utcZone                           = ZoneId.of("UTC")
    val TimeFormatter: DateTimeFormatter          = DateTimeFormatter.ISO_DATE_TIME.withZone(utcZone)
    val CompactTimeFormatter: DateTimeFormatter   = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX").withZone(utcZone)
    val CompactTimeFormatterMs: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSSX").withZone(utcZone)
    val CompactTimeFormatterNs: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSSSSSX").withZone(utcZone)

    def parseIso8601(s: String): Either[Throwable, Instant] =
        val tried = Try(Instant.from(CompactTimeFormatter.parse(s))) orElse
            Try(Instant.from(TimeFormatter.parse(s))) orElse
            Try(Instant.from(CompactTimeFormatterMs.parse(s))) orElse
            Try(Instant.from(CompactTimeFormatterNs.parse(s)))
        tried.toEither



  /**
   * Parse a string in common ISO8601 formats into an Instant
   * @param timestamp
   * @return
   */
  def parse(timestamp: String): Option[Instant] =
    Try(formatMillis.parse(timestamp))
      .orElse(Try(formatSeconds.parse(timestamp)))
      .orElse(Try(DateTimeFormatter.ISO_INSTANT.parse(timestamp)))
      .toOption
      .map(Instant.from)
