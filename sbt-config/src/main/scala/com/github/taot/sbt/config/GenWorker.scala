package com.github.taot.sbt.config

import java.io.PrintStream

import com.typesafe.config.ConfigFactory
import sbt.{File, Logger}

class GenWorker(rsrcDir: File, pkg: String, destDir: File, logger: Logger) {

  private val REFERENCE_CONF = "reference.conf"

  def run(): Seq[File] = {
    val file = new File(rsrcDir, REFERENCE_CONF)
    if (file.exists()) {
      logger.info(s"${REFERENCE_CONF} found. Generating to package ${pkg}")
      val pkgDir = createPkgDir(destDir, pkg)
      val outFile = new File(pkgDir, "S.scala")
      val stream = new PrintStream(outFile)
      try {
        val config = ConfigFactory.parseFile(file)
        new CodeGenerator(config, pkg, stream).generate()
      } finally {
        stream.close()
      }
      Seq(outFile)
    } else {
      logger.info(s"${REFERENCE_CONF} not found")
      Seq.empty
    }
  }

  private def createPkgDir(destDir: File, pkg: String): File = {
    val pkgs = pkg.split("\\.")
    var base = destDir
    for (p <- pkgs) {
      base = new File(base, p)
    }
    base.mkdirs()
    base
  }
}
