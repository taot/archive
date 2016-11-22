package com.github.taot.sbt.i18n

import sbt._

object PluginKeys {

  lazy val messagesPrefix = settingKey[String]("Prefix of the messages file. Typical messages files would be something like messages.zh or messages.en")
}
