package com.taot.pms.gui

import org.springframework.scala.context.function.FunctionalConfiguration
import com.taot.pms.common.config.impl.ReloadableConfigurator
import com.taot.pms.common.RichDomainObjectFactory

class GUIConfiguration extends FunctionalConfiguration {

  val configurator = singleton() {
    new ReloadableConfigurator()
  }

  val richDomainObjectFactory = singleton() {
    RichDomainObjectFactory
  }

  val thriftClient = singleton() {
    ThriftClientFactory.create()
  }
}
