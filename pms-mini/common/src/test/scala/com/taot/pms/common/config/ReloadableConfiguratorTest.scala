package com.taot.pms.common.config

import org.scalatest.{BeforeAndAfterEach, BeforeAndAfter, ShouldMatchers, WordSpec}
import com.taot.pms.common.logging.Logging
import com.typesafe.config.Config
import java.io.{FileOutputStream, PrintWriter, File}
import com.typesafe.config.impl.ConfigInt
import com.taot.pms.common.config.impl.ReloadableConfigurator

class ReloadableConfiguratorTest extends WordSpec with ShouldMatchers with Logging {

  "ReloadableConfig" should {
    "load and reload config" in {
      System.setProperty("config.reload.interval", "1")
      saveTestProperty(1)
      val configurator = new ReloadableConfigurator("application")

      var updatedValue: Int = -1
      var firedValue: Int = -1

      configurator.addListener(new ConfigChangeListener {
        override def onConfigChange(e: ConfigChangeEvent): Unit = {
          println("Config changed")
          updatedValue = e.config.getInt("test.property")
          firedValue = e.changes.find(_.path == "test.property") match {
            case Some(c) =>
              c.newValue.unwrapped().asInstanceOf[Int]
            case None =>
              throw new RuntimeException
          }
        }
      }, Seq("test.property"))

      try {
        configurator.getConfig.getInt("test.property") should equal (1)

        saveTestProperty(2)
        val nSleep = 3000
        logger.info("Sleeping {} ms", nSleep)
        Thread.sleep(nSleep)

        updatedValue should equal (2)
        firedValue should equal (2)

      } finally {
        configurator.destroy()
      }
    }
  }



  private def saveTestProperty(value: Int): Unit = {
    logger.info("Saving test.property = " + value)
    val file = new File(this.getClass.getClassLoader.getResource("application.conf").getFile)
    val pw = new PrintWriter(new FileOutputStream(file))
    pw.println("test.property = " + value)
    pw.close()
  }
}
