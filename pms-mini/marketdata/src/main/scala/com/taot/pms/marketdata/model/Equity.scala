package com.taot.pms.marketdata.model

case class Equity(
  override val id: Long,
  override val symbol: String
) extends Security