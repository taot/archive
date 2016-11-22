package com.github.taot.sbt.config

import sbt.{File, IO}

object SampleConfig {

  val config =
    """a {
      |    b {
      |        c = 1
      |        d = 2.3
      |        e = "hello"
      |        f = true
      |        g = [1, 2, 3]
      |        h = ["a", "b", "c"]
      |    }
      |}
      |b {
      |    c {
      |        d = "<Int>"
      |        e = "<Double>"
      |    }
      |}""".stripMargin

  def write(): File = {
    val tmpFile = new File(System.getProperty("java.io.tmpdir"), "test_sample.conf")
    println(tmpFile.getAbsolutePath)
    IO.write(tmpFile, config)
    tmpFile
  }
}
