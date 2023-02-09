package org.mbari.scommons.util

import java.time.Instant

class ISO8601Test extends munit.FunSuite:

  private def minify(ts: String): String =
    ts.replace("-", "")
      .replace(":", "")

  private val t0      = "1968-09-22T01:23:45Z"
  private val seconds = minify(t0)

  private val t1     = "1968-09-22T01:23:45.678Z"
  private val millis = minify(t1)

  test(s"should parse $seconds") {
    val t = ISO8601.parse(seconds)
    assert(t.isDefined)
    assertEquals(t.get, Instant.parse(t0))
  }

  test(s"should parse $millis") {
    val t = ISO8601.parse(millis)
    assert(t.isDefined)
    assertEquals(t.get, Instant.parse(t1))
  }
