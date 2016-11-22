package com.github.taot.sbt.config

import sbt._
import Keys._

object ConfigPlugin extends Plugin {

  lazy val pkg = settingKey[String]("Package of generated class")

  val Settings = Seq(

    pkg := moduleName.value,

    sourceGenerators in Compile <+= (resourceDirectory in Compile, pkg, sourceManaged in Compile, streams) map {
      (rsrcDir, pkg, destDir, streams) => {
        new GenWorker(rsrcDir, pkg, destDir, streams.log).run()
      }
    }
  )
}
