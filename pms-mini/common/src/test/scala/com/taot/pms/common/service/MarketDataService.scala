package com.taot.pms.common.service

trait MarketDataService extends Service with Refreshable {

  def getData(symbol: String): BigDecimal
}
