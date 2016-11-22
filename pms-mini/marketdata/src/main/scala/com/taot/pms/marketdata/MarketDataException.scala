package com.taot.pms.marketdata

class MarketDataException(msg: String, cause: Throwable) extends RuntimeException(msg, cause) {

  def this(msg: String) = this(msg, null)

  def this() = this(null, null)
}
