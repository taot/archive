package com.taot.pms.common.service

import org.springframework.scala.context.function.FunctionalConfiguration
import com.taot.pms.common.config.impl.ReloadableConfigurator

class TestServiceConfiguration extends FunctionalConfiguration {

  val configurator = singleton() {
    new ReloadableConfigurator()
  }

  val calendarService = singleton() {
    new CalendarServiceImpl
  }

  val marketDataService = singleton() {
    new MarketDataServiceImpl(calendarService, configurator)
  }
}