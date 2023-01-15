package org.mbari.scommons.util

import java.io.{BufferedWriter, File, FileWriter}
import java.util.Date

class ZipTest extends munit.FunSuite:
  test("zip files up") {
    val fs = Seq("target/file1.txt", "target/file2.txt")

    fs.foreach { f =>
      val out = new BufferedWriter(new FileWriter(f))
      out.write("Some text written on " + new Date)
      out.close()
    }

    val targetFile = new File("target", getClass.getSimpleName + "-test-output.zip")
    Zip(targetFile, fs)
  }
