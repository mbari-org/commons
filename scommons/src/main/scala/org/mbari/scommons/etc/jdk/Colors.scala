package org.mbari.scommons.etc.jdk

import java.awt.Color

object Colors:

    /**
     * @param hex
     *   Hex representation of color. For example, #FF0000 is red. #FFFFFF is white. #FFFF0030 is a transparent yellow.
     * @return
     *   A java.awt.Color from a hex string.
     */
    def fromHex(hex: String): Color =
        val s = hex.replace("#", "")
        val r = Integer.parseInt(s.substring(0, 2), 16)
        val g = Integer.parseInt(s.substring(2, 4), 16)
        val b = Integer.parseInt(s.substring(4, 6), 16)

        if s.length == 8 then
            val a = Integer.parseInt(s.substring(6, 8), 16)
            new Color(r, g, b, a)
        else new Color(r, g, b)

    /**
     * Converts a string to a hex color. The color is based on the hash code of the string.
     * @param s
     *   the string to convert
     * @return
     *   the hex color
     */
    def stringToHex(s: String): String =
        val c = intToRGBA(s.hashCode)
        f"#${c.getRed}%02X${c.getGreen}%02X${c.getBlue}%02X" // ignore alpha. Sharktopoda won't parse colors with alpha

    def intToRGBA(i: Int): Color =
        val a = brighten((i >> 24) & 0xff, 128)
        val r = brighten((i >> 16) & 0xff, 32)
        val g = brighten((i >> 8) & 0xff, 32)
        val b = brighten(i & 0xff, 32)

        new Color(r, g, b, a)

    /**
     * Brightens a color (increases its value) so that it is between min and 256.
     * @param v
     *   The value to brighten
     * @param min
     *   the minimum allowed color
     * @return
     *   the brightened color
     */
    def brighten(v: Int, min: Int): Int =
        val inRangeMin =
            if min > 256 then 256
            else if min < 0 then 0
            else min
        math.round(((v + min) / (256d + min)) * 256).toInt
