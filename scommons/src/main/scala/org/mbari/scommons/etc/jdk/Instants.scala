package org.mbari.scommons.etc.jdk

import java.time.Instant
import java.time.format.DateTimeFormatter
import scala.util.Try
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

object Instants:

    val TimeFormatter: DateTimeFormatter =
      DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC)

    val CompactTimeFormatter: DateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX").withZone(ZoneOffset.UTC)

    val CompactTimeFormatterMs: DateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSSX").withZone(ZoneOffset.UTC)

    val CompactTimeFormatterNs: DateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSSSSSX").withZone(ZoneOffset.UTC)

    private val compactTimeFormatter = CompactTimeFormatter

    private val formatters = Seq(
      compactTimeFormatter,
      TimeFormatter,
      CompactTimeFormatterMs,
      CompactTimeFormatterNs,
      DateTimeFormatter.ISO_OFFSET_DATE_TIME,
      DateTimeFormatter.ISO_INSTANT)

    def parseIso8601(s: String): Option[Instant] =
      formatters.to(LazyList).flatMap(tryToParse(s, _)).headOption

    def compactFormat(instant: Instant): String = compactTimeFormatter.format(instant)

    private def tryToParse(s: String, formatter: DateTimeFormatter): Option[Instant] =
      Try(Instant.from(formatter.parse(s))).toOption

    def roundToHour(i: Instant): Instant =
      val z             = i.atZone(ZoneOffset.UTC)
      val minutes       = z.getMinute()
      val adjustedTime  = if (minutes >= 30) z.plusHours(1) else z
      val truncatedTime = adjustedTime.truncatedTo(ChronoUnit.HOURS)
      truncatedTime.toInstant()
