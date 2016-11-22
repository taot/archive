package com.taot.pms.common.service

import org.joda.time.LocalDate

trait CalendarService extends Service {

  def isTradeDay(date: LocalDate): Boolean
}
