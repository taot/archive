package com.taot.pms.domain.model.asset.cash

import org.joda.time.LocalDate
import com.taot.pms.domain.assetclass.AssetClassConstants
import com.taot.pms.common.RichDomainObjectFactory

class DepositPosition(_id: Long, _accountId: Long, _currencyCode: String, _asOfDate: LocalDate, _quantity: BigDecimal)
  extends CashPosition(_id, _accountId, _currencyCode, _asOfDate, _quantity) {

  override val assetClass = AssetClassConstants.ASSET_CASH_DEPOSIT

  override def renewDaily(newAsOfDate: LocalDate): DepositPosition = {
    new DepositPosition(id, accountId, currencyCode, newAsOfDate, quantity_)
  }
}


object DepositPosition extends CashPositionRecordConverter[DepositPosition] {

  def apply(accountId: Long, currencyCode: String, asOfDate: LocalDate, quantity: BigDecimal): DepositPosition = {
    val p = new DepositPosition(-1L, accountId, currencyCode, asOfDate, quantity)
    RichDomainObjectFactory.autowire(p)
  }

  override protected def createPositionObject(positionId: Long, accountId: Long, currencyCode: String,
                                              asOfDate: LocalDate, amount: BigDecimal): DepositPosition = {

    new DepositPosition(positionId, accountId, currencyCode, asOfDate, amount)
  }
}