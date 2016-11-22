package com.taot.pms.domain.model.asset.cash

import com.taot.pms.persist.{PositionHist => DbPositionHist, Position => DbPosition }
import com.taot.pms.domain.BusinessException
import com.taot.pms.common.RichDomainObjectFactory
import org.joda.time.LocalDate
import com.taot.pms.domain.model.asset.{Position, RecordConverter}

trait CashPositionRecordConverter[T <: CashPosition] extends RecordConverter {

  private val POSITION_LEDGER_ID: Long = 1L

  protected def createPositionObject(positionId: Long, accountId: Long, currencyCode: String, asOfDate: LocalDate, amount: BigDecimal): T

  override def fromRecords(positionRec: DbPosition, records: List[DbPositionHist]): T = {
    val asOfDate = records.head.asOfDate
    val positionId = positionRec.id
    val accountId = positionRec.accountId
    val currencyCode = positionRec.currencyCodeOpt.getOrElse {
      throw new BusinessException("Invalid cash position " + positionId + ". No currency code")
    }

    val position = records.find(_.ledgerId == POSITION_LEDGER_ID) match {
      case Some(ph) =>
        createPositionObject(positionId, accountId, currencyCode, asOfDate, ph.amount)
      case None =>
        throw new BusinessException(s"Ledger ${POSITION_LEDGER_ID} not found, failed to convert from records")
    }

    RichDomainObjectFactory.autowire(position)
  }

  override def toRecords(position: Position): List[DbPositionHist] = {
    val cashP = position.asInstanceOf[CashPosition]
    val ph = DbPositionHist(cashP.id, cashP.accountId, cashP.asOfDate, POSITION_LEDGER_ID, cashP.quantity)
    ph :: Nil
  }
}
