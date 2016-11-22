package com.taot.pms.marketdata.impl

import com.taot.pms.marketdata.{MarketDataException, MarketDataService}
import com.taot.pms.marketdata.model.{Equity, Security}
import org.joda.time.LocalDate

class MarketDataServiceMockImpl extends MarketDataService {

  private val list = List(
    new Equity(1L, "GOOG")
  )

  override def getPrice(securityId: Long, asOfDate: LocalDate): BigDecimal = ???

  override def getSecurityBySymbol(symbol: String): Security = ???

  override def getSecurityById(securityId: Long): Security = {
    list.find(_.id == securityId).getOrElse {
      throw new MarketDataException("Failed to find security by id " + securityId)
    }
  }
}
