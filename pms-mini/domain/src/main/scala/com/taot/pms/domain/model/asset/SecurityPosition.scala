package com.taot.pms.domain.model.asset

import com.taot.pms.marketdata.model.Security
import org.joda.time.LocalDate

abstract class SecurityPosition(_id: Long, _accountId: Long, _security: Security, _asOfDate: LocalDate, _quantity: Long, _carryingValue: BigDecimal) extends Position {

  protected var quantity_ = _quantity

  protected var carryingValue_ = _carryingValue


  val id = _id

  val accountId = _accountId

  val security = _security

  val asOfDate = _asOfDate

  def quantity = quantity_

  def carryingValue = carryingValue_
}