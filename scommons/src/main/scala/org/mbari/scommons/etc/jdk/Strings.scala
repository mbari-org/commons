package org.mbari.scommons.etc.jdk

object Strings:

    def addTrailingSlash(s: String): String = if s.endsWith("/") then s else s + "/"

    def removeTrailingSlash(s: String): String =
        if s.endsWith("/") then s.substring(0, s.length - 1) else s
