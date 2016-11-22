package com.github.taot.config

import com.typesafe.config.ConfigFactory

object TypesafeConfig {

  private[config] val config = ConfigFactory.load()
}
