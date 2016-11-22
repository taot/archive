package com.taot.pms.tools.dbsetup

import com.taot.pms.common.config.impl.ReloadableConfigurator
import com.taot.pms.persist.{MySQLDAL, PersistInitializer}
import org.springframework.scala.context.function.FunctionalConfiguration

class DBSetupConfiguration extends FunctionalConfiguration {

  val configurator = singleton() {
    new ReloadableConfigurator()
  }

  val persistInitializer = singleton() {
    new PersistInitializer(configurator)
  }

  val dal = singleton() {
    new MySQLDAL
  }
}
