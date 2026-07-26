package org.mbari.scommons.etc.jdk

import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.charset.StandardCharsets
import Loggers.given
import scala.util.control.NonFatal
import java.time.Duration

object Uris:

    private val log = System.getLogger(getClass.getName)

    /**
     * Encodes a string for use in a URI.
     *
     * @param s
     *   the string to encode
     * @return
     *   the encoded string
     */
    def encodeURIComponent(s: String): String =
        java.net
            .URLEncoder
            .encode(s, "UTF-8")
            .replace("+", "%20")
            .replace("*", "%2A")
            .replace("%7E", "~")

    def filename(uri: URI): String = uri.getPath.split('/').last

    def exists(uri: URI, timeout: Duration = DefaultTimeout): Boolean =
        try
            val request = HttpRequest
                .newBuilder()
                .uri(uri)
                .HEAD()
                .timeout(timeout)
                .build()
            clientSupport
                .client
                .send(request, BodyHandlers.discarding())
                .statusCode() == 200
        catch
            case NonFatal(e) =>
                log
                    .atWarn
                    .withCause(e)
                    .log(s"Failed to connect to $uri")
                false

    def encode(uri: URI): String =
        URLEncoder.encode(uri.toString(), StandardCharsets.UTF_8.toString())

