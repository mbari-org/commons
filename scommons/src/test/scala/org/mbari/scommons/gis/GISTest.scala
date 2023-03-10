package org.mbari.scommons.gis

class GISTest extends munit.FunSuite:

  private[this] val tolerance = 0.5

  private[this] val lat = Array(40.3154333, 46.283900, 37.577833, 28.645650, 38.855550, 25.061783)
  private[this] val lon = Array(-3.4857166, 7.8012333, -119.95525, -17.759533, -94.7990166, 121.640266)

  private[this] val expectedEasting  = Array[Double](458731, 407653, 239027, 230253, 343898, 362850)
  private[this] val expectedNorthing = Array[Double](4462881, 5126290, 4163083, 3171843, 4302285, 2772478)

  test("deg2utm") {
    for (i <- 0 until lat.size)
      val la                        = lat(i)
      val lo                        = lon(i)
      val (easting, northing, zone) = GIS.deg2utm(la, lo)
      assertEqualsDouble(expectedEasting(i), easting, tolerance)
      assertEqualsDouble(expectedNorthing(i), northing, tolerance)
  }
