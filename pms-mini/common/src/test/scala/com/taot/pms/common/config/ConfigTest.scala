package com.taot.pms.common.config

import org.scalatest.{ShouldMatchers, WordSpec}
import com.taot.pms.common.logging.Logging
import com.typesafe.config.ConfigFactory


class ConfigTest extends WordSpec with ShouldMatchers with Logging {

  "Typesafe config" should {
    "load config from reference.conf" in {
      val config = ConfigFactory.load()
      val sth = config.getString("test.something")
      sth should equal ("abc")
    }
  }
}