package org.mbari.scommons.etc.jdk

import java.net.URI
import Loggers.given
import scala.util.control.NonFatal
import java.net.HttpURLConnection

object Uris:

    private val log = System.getLogger(getClass.getName)

    def filename(uri: URI): String = uri.getPath.split('/').last

    def exists(uri: URI): Boolean = try
        HttpURLConnection.setFollowRedirects(false)
        val connection = uri.toURL.openConnection().asInstanceOf[HttpURLConnection]
        connection.setRequestMethod("HEAD")
        connection.getResponseCode == HttpURLConnection.HTTP_OK
    catch
        case NonFatal(e) =>
            log
                .atWarn
                .withCause(e)
                .log(s"Failed to connect to $uri")
            false

