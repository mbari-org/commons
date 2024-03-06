package org.mbari.scommons.etc.jdk

import java.nio.file.Path

object Paths:

    def nameWithoutExtension(path: Path): String =
        nameWithoutExtension(path.getFileName().toString())

    def nameWithoutExtension(filename: String): String =
        val i = filename.lastIndexOf('.')
        if i > 0 then filename.substring(0, i) else filename
