package com.taot.pms.domain.model.asset.cash

import com.taot.pms.common.RichDomainObjectFactory
import com.taot.pms.domain.assetclass.AssetClassConstants
import org.joda.time.LocalDate

class CommissionPosition(_id: Long, _accountId: Long, _currencyCode: String, _asOfDate: LocalDate, _quantity: BigDecimal)
  extends CashPosition(_id, _accountId, _currencyCode, _asOfDate, _quantity) {

  override val assetClass = AssetClassConstants.ASSET_CASH_COMMISSION

  override def renewDaily(newAsOfDate: LocalDate): CommissionPosition = {
    new CommissionPosition(id, accountId, currencyCode, newAsOfDate, quantity_)
  }
}

object CommissionPosition extends CashPositionRecordConverter[CommissionPosition] {

  def apply(accountId: Long, currencyCode: String, asOfDate: LocalDate, quantity: BigDecimal): CommissionPosition = {
    val p = new CommissionPosition(-1L, accountId, currencyCode, asOfDate, quantity)
    RichDomainObjectFactory.autowire(p)
  }

  override protected def createPositionObject(positionId: Long, accountId: Long, currencyCode: String,
                                              asOfDate: LocalDate, amount: BigDecimal): CommissionPosition = {

    new CommissionPosition(positionId, accountId, currencyCode, asOfDate, amount)
  }
}