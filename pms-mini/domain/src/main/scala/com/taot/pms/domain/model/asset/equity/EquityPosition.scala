package com.taot.pms.domain.model.asset.equity

import com.taot.pms.common.{BigDecimalConstants, RichDomainObjectFactory}
import com.taot.pms.domain.BusinessException
import com.taot.pms.domain.assetclass.AssetClassConstants
import com.taot.pms.domain.model.asset.{Position, RecordConverter, SecurityPosition}
import com.taot.pms.marketdata.model.Security
import org.joda.time.LocalDate
import com.taot.pms.marketdata.MarketDataService
import com.taot.pms.persist.{PositionHist => DbPositionHist, Position => DbPosition }


class EquityPosition(_id: Long, _accountId: Long, _security: Security, _asOfDate: LocalDate, _quantity: Long, _carryingValue: BigDecimal)
  extends SecurityPosition(_id, _accountId, _security, _asOfDate, _quantity, _carryingValue) {

  override val assetClass = AssetClassConstants.ASSET_SECURITY_EQUITY

  def buy(amount: Long, value: BigDecimal): Unit = {
    quantity_ += amount
    carryingValue_ += value
  }

  def sell(amount: Long, value: BigDecimal): Unit = {
    if (amount > quantity_) {
      throw new BusinessException("Holding quantity " + quantity_ + " is smaller than sell amount " + amount)
    }
    carryingValue_ -= (carryingValue_ * amount / quantity_)
    quantity_ -= amount
  }

  override def renewDaily(newAsOfDate: LocalDate): EquityPosition = {
    new EquityPosition(id, accountId, security, newAsOfDate, quantity_, carryingValue_)
  }
}

object EquityPosition extends RecordConverter {

  def apply(accountId: Long, security: Security, asOfDate: LocalDate, quantity: Long, carryingValue: BigDecimal): EquityPosition = {
    val p = new EquityPosition(-1, accountId, security, asOfDate, quantity, carryingValue)
    RichDomainObjectFactory.autowire(p)
  }


  private val EQUITY_LEDGER = 1L

  private lazy val marketDataService = RichDomainObjectFactory.getInstance(classOf[MarketDataService])

  override def fromRecords(positionRec: DbPosition, hists: List[DbPositionHist]): Position = {
    val asOfDate = hists.head.asOfDate
    val positionId = positionRec.id
    val accountId = positionRec.accountId
    val position = hists.find(_.ledgerId == EQUITY_LEDGER) match {
      case Some(ph) =>
        val securityId = positionRec.securityIdOpt.getOrElse {
          throw new BusinessException("Invalid position hist record for equity, missing security id")
        }
        val security = marketDataService.getSecurityById(securityId)
        // TODO fulfill the carrying value logic
        new EquityPosition(positionId, accountId, security, asOfDate, ph.amount.toLong, BigDecimalConstants.ZERO)
      case None =>
        throw new BusinessException("Equity ledger not found, failed to convert from records")
    }

    RichDomainObjectFactory.autowire(position)
  }

  override def toRecords(position: Position): List[DbPositionHist] = {
    val equityP = position.asInstanceOf[EquityPosition]
    val ph = DbPositionHist(equityP.id, equityP.accountId, equityP.asOfDate, EQUITY_LEDGER, equityP.quantity)
    ph :: Nil
  }
}
