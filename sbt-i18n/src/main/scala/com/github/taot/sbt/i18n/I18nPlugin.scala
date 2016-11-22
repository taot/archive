package com.github.taot.sbt.i18n

import sbt._
import Keys._

object I18nPlugin extends Plugin {

  import PluginKeys._

  val Settings = Seq(

    messagesPrefix := "messages",

    sourceGenerators in Compile <+= (resourceDirectory in Compile, messagesPrefix, moduleName, sourceManaged in Compile) map {
      (srcDir, messagesPrefix, moduleName, destDir) => { new Generator(srcDir, messagesPrefix, moduleName, destDir).generate() }
    }
  )
}
