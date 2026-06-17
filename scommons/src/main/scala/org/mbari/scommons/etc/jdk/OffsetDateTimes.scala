import java.time.format.DateTimeFormatter
import java.time.{OffsetDateTime, ZoneId}
import scala.util.Try

/**
 * Utility object for working with [[java.time.OffsetDateTime]] objects.
 */
object OffsetDateTimes:

    private val UTC: ZoneId = ZoneId.of("UTC")

    private val CompactTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX").withZone(UTC)

    private val CompactTimeFormatterMs: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSSX").withZone(UTC)

    private val CompactTimeFormatterNs: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSSSSSX").withZone(UTC)

    private val parsers = Seq(
        s => OffsetDateTime.parse(s),
        s => OffsetDateTime.parse(s, CompactTimeFormatter),
        s => OffsetDateTime.parse(s, CompactTimeFormatterMs),
        s => OffsetDateTime.parse(s, CompactTimeFormatterNs)
    )

    def parseIso8601(s: String): OffsetDateTime =
        parsers
            .view
            .map(p => Try(p(s)))
            .collectFirst { case scala.util.Success(dt) => dt }
            .getOrElse(throw new IllegalArgumentException(s"Invalid date: $s"))

    /**
     * A case class that represents a time range with a start and end date.
     */
    final case class Bounds(start: OffsetDateTime, end: OffsetDateTime):
        require(!start.isAfter(end), "Start date must be before end date")

        def contains(dateTime: OffsetDateTime): Boolean =
            dateTime.isEqual(start) || dateTime.isEqual(end) ||
                (dateTime.isAfter(start) && dateTime.isBefore(end))

        def overlaps(other: Bounds): Boolean =
            start.isEqual(other.start) ||
                end.isEqual(other.end) ||
                (start.isBefore(other.end) && end.isAfter(other.start)) ||
                (other.start.isBefore(end) && other.end.isAfter(start))

    /**
     * Creates a [[Bounds]] object with a start and end date. If the end date is null, it will be set to 6 hours after
     * the start date. If the start date is null, it will be set to 6 hours before the end date.
     *
     * @param start
     *   the start date (nullable)
     * @param end
     *   the end date (nullable)
     * @return
     *   a [[Bounds]] object with the specified start and end dates
     */
    def bounds(start: OffsetDateTime, end: OffsetDateTime): Bounds =
        val effectiveEnd =
            if end == null then if start != null then start.plusHours(6) else OffsetDateTime.now()
            else end

        val effectiveStart =
            if start == null then effectiveEnd.minusHours(6)
            else start

        Bounds(effectiveStart, effectiveEnd)

