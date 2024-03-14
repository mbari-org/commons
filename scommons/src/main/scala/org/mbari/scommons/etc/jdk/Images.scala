package org.mbari.scommons.etc.jdk

import java.awt.image.{BufferedImage, RenderedImage}
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, IOException}
import java.net.URL
import java.nio.file.{Files, Path}
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import javax.imageio.{IIOImage, ImageIO, ImageTypeSpecifier}

object Images:

    /**
     * Saves the image to the target file. The format of the saved file is determined by it's extension. Will create the
     * parent directories if they do not exist.
     *
     * @param image
     *   The image to save
     * @param target
     *   The file to save the image to.
     *
     * @throws IOException
     */
    @throws(classOf[IOException])
    def saveImage(image: RenderedImage, target: Path): Unit =
        // Extract the type from the extension
        val parent = target.getParent
        if Files.notExists(parent) then Files.createDirectories(parent)
        val path   = target.toAbsolutePath.normalize.toString
        val dotIdx = path.lastIndexOf(".")
        val ext    = path.substring(dotIdx + 1)
        ImageIO.write(image, ext, target.toFile)

    /**
     * Convert a buffered image to an array of bytes in JPEG format
     * @param image
     * @return
     */
    def toJpegByteArray(image: BufferedImage): Array[Byte] = toImageByteArray(image, "jpg")

    /**
     * Convert a buffered image to an array of bytes in PNG format
     * @param image
     * @return
     */
    def toPngByteArray(image: BufferedImage): Array[Byte] = toImageByteArray(image, "png")

    private def toImageByteArray(image: BufferedImage, format: String): Array[Byte] =
        val os0        = new ByteArrayOutputStream
        ImageIO.write(image, format, os0)
        val imageBytes = os0.toByteArray
        os0.close()
        imageBytes
