package com.taot.pms.common.service

import org.joda.time.LocalDate
import com.taot.pms.common.logging.Logging

class CalendarServiceImpl extends CalendarService with Logging {

  def init(): Unit = {
    logger.info("Initializing CalendarServiceImpl")
  }

  override def destroy(): Unit = {
    logger.info("Destroying CalendarServiceImpl")
  }

  def isTradeDay(date: LocalDate): Boolean = {
    logger.info("Calling CalendarServiceImpl.isTradeDay")
    true
  }
}