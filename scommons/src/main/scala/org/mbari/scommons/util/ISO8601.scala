package org.mbari.scommons.util

import java.time.format.DateTimeFormatter
import java.time.ZoneId
import java.time.Instant
import scala.util.Try

object ISO8601:

  private[this] val formatMillis = DateTimeFormatter
    .ofPattern("yyyyMMdd'T'HHmmss.SSSX")
    .withZone(ZoneId.of("UTC"))

  private[this] val formatSeconds = DateTimeFormatter
    .ofPattern("yyyyMMdd'T'HHmmssX")
    .withZone(ZoneId.of("UTC"))

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
