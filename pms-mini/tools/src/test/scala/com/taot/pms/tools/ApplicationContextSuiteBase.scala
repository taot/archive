package com.taot.pms.tools

import org.scalatest.{BeforeAndAfterAll, Suite}
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import com.taot.pms.ServerConfiguration

trait ApplicationContextSuiteBase extends Suite with BeforeAndAfterAll {

  protected var applicationContext: ConfigurableApplicationContext = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    applicationContext = FunctionalConfigApplicationContext(classOf[ServerConfiguration])
  }

  override protected def afterAll(): Unit = {
    applicationContext.close()
    super.afterAll()
  }
}
