package com.github.taot.sbt.config

import com.typesafe.config.ConfigFactory
import org.scalatest.{ShouldMatchers, WordSpec}

class Test extends WordSpec with ShouldMatchers {

  "test" should {

    "test" in {
      val file = SampleConfig.write()
      val config = ConfigFactory.parseFile(file)


      new CodeGenerator(config, "test", System.out).generate()
    }
  }
}
