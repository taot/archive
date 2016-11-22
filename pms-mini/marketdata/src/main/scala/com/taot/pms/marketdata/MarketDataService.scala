package com.taot.pms.marketdata

import org.joda.time.LocalDate
import com.taot.pms.marketdata.model.Security

trait MarketDataService {

  def getSecurityById(securityId: Long): Security

  def getSecurityBySymbol(symbol: String): Security

  def getPrice(securityId: Long, asOfDate: LocalDate): BigDecimal
}
