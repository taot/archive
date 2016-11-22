package com.taot.pms.common.service

import com.taot.pms.common.{ReadWriteLockSupport, BigDecimalConstants}
import org.joda.time.LocalDate
import com.taot.pms.common.logging.Logging
import com.taot.pms.common.config.impl.ReloadableConfigurator

class MarketDataServiceImpl(calendarService: CalendarService, configurator: ReloadableConfigurator) extends MarketDataService with Refreshable with ReadWriteLockSupport with Logging {

  val n = 1000
  logger.info("Initializing MarketDataServiceImpl taking " + n + " ms")
  Thread.sleep(n)
  logger.info("Initializing MarketDataServiceImpl finished")

  def getData(name: String): BigDecimal = lockRead {
    logger.info("Calling MarketDataServiceImpl.getData")
    logger.info("Is trade day: " + calendarService.isTradeDay(LocalDate.now))
    BigDecimalConstants.ONE
  }

  def destroy(): Unit = lockWrite {
    val n = 1000
    logger.info("Destroying MarketDataServiceImpl taking " + n + " ms")
    Thread.sleep(n)
    logger.info("Destroying MarketDataServiceImpl finished")
  }

  override def refresh(): Unit = lockWrite {
    val n = 2000
    logger.info("Refresh MarketDataServiceImpl taking " + n + " ms")
    Thread.sleep(n)
    logger.info("Refreshing MarketDataServiceImpl finished")
  }
}
