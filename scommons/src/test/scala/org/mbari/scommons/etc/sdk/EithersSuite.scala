package org.mbari.scommons.etc.sdk

class EithersSuite extends munit.FunSuite {

    test("traverse") {
        val seq = Seq(1, 2, 3)
        val result = Eithers.traverse(seq) { i =>
            if (i % 2 == 0) Right(i * 2)
            else Left(new IllegalArgumentException(s"Invalid number: $i"))
        }
        assertEquals(result.left.map(_.getMessage), Left("Invalid number: 1"))
    }
  
}
