package com.taot.pms.domain.model.asset.cash

import com.taot.pms.domain.model.asset.Position
import org.joda.time.LocalDate
import com.taot.pms.common.BigDecimalConstants

abstract class CashPosition(_id: Long, _accountId: Long, _currencyCode: String, _asOfDate: LocalDate, _quantity: BigDecimal)
  extends Position {

  protected var quantity_ = _quantity

  protected var id_ = _id

  val id = id_

  val accountId = _accountId

  val currencyCode = _currencyCode

  val asOfDate = _asOfDate

  def quantity = quantity_

  def change(value: BigDecimal): Unit = {
    this.quantity_ += value
  }

  def reset(): Unit = {
    this.quantity_ = BigDecimalConstants.ZERO
  }
}