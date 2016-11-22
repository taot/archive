package com.taot.pms.marketdata.model

case class TBondFuture(
  override val id: Long,
  override val symbol: String
) extends Security