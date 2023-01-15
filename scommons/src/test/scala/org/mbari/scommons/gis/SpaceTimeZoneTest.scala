package org.mbari.scommons.gis

import java.util.Date
import org.junit.Test
import org.junit.Assert._
import org.mbari.scommons.geometry.{Point4D, SpatialEnvelope}

/**
 * @author
 *   Brian Schlining
 * @since 2012-07-12
 */

class SpaceTimeZoneTest extends munit.FunSuite:

  private val envelope       = SpaceTimeZone(new SpatialEnvelope(0d, 0d, 10d, 10d))
  private val momentInterval = SpaceTimeZone(MomentInterval(new Date(0), new Date(1000000)))

  test("contains") {
    val inside = Point4D(1d, 2d, 3d, new Date(1000))
    assertTrue(envelope.contains(inside))
    assertTrue(momentInterval.contains(inside))

    val outside = Point4D(-1d, 12d, 100d, new Date(2000000))
    assertFalse(envelope.contains(outside))
    assertFalse(momentInterval.contains(outside))
  }

  test("contains 2") {
    val inside       = Point4D(2d, 2d, 2d, new Date(1000))
    val outsideSpace = Point4D(100d, 2d, 2d, new Date(1000))
    val outsideTime  = Point4D(2d, 2d, 2d, new Date(2000000))
    val zone         = envelope and momentInterval

    assertTrue(zone.contains(inside))
    assertFalse(zone.contains(outsideSpace))
    assertFalse(zone.contains(outsideTime))

  }
