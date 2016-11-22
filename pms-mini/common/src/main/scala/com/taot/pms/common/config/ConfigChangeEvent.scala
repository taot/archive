package com.taot.pms.common.config

import com.typesafe.config.{ConfigValue, Config}

case class ConfigChangeEvent(config: Config, changes: Seq[ConfigChange])

case class ConfigChange(path: String, oldValue: ConfigValue, newValue: ConfigValue)