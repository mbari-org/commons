package org.mbari.scommons.etc.jdk

import java.net.URI

object Uris:

    def filename(uri: URI): String = uri.getPath.split('/').last
