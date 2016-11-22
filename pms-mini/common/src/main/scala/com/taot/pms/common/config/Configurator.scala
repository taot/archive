package com.taot.pms.common.config

import com.typesafe.config.Config

trait Configurator {

  def getConfig(): Config

  def addListener(listener: ConfigChangeListener, paths: Seq[String]): Unit

  def removeListener(listener: ConfigChangeListener): Unit
}
