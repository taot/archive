package com.taot.pms.domain.model.asset.cash

import com.taot.pms.domain.model.account.Account
import org.joda.time.LocalDate
import com.taot.pms.domain.model.asset.PositionRepository
import com.taot.pms.domain.assetclass.AssetClassConstants
import com.taot.pms.domain.model.Currencies
import com.taot.pms.domain.BusinessException

class CashPositionRepositoryImpl(positionRepository: PositionRepository) extends CashPositionRepository {

  override def update(position: CashPosition): Unit = positionRepository.save(position)

  override def findReceivablePosition(account: Account, asOfDate: LocalDate): ReceivablePosition = {
    val list = positionRepository.load[ReceivablePosition](account, AssetClassConstants.ASSET_CASH_RECEIVABLE, asOfDate)
    if (list.isEmpty) {
      val p = ReceivablePosition(account.id, Currencies.CNY, asOfDate, 0)
      positionRepository.save(p)
      p
    } else if (list.size > 1) {
      throw new BusinessException("More than one receivable position is loaded")
    } else {
      list(0)
    }
  }

  override def findPayablePosition(account: Account, asOfDate: LocalDate): PayablePosition = {
    val list = positionRepository.load[PayablePosition](account, AssetClassConstants.ASSET_CASH_PAYABLE, asOfDate)
    if (list.isEmpty) {
      val p = PayablePosition(account.id, Currencies.CNY, asOfDate, 0)
      positionRepository.save(p)
      p
    } else if (list.size > 1) {
      throw new BusinessException("More than one receivable position is loaded")
    } else {
      list(0)
    }
  }

  override def findFeePosition(account: Account, asOfDate: LocalDate): FeePosition = {
    val list = positionRepository.load[FeePosition](account, AssetClassConstants.ASSET_CASH_FEE, asOfDate)
    if (list.isEmpty) {
      val p = FeePosition(account.id, Currencies.CNY, asOfDate, 0)
      positionRepository.save(p)
      p
    } else if (list.size > 1) {
      throw new BusinessException("More than one receivable position is loaded")
    } else {
      list(0)
    }
  }

  override def findDepositPosition(account: Account, asOfDate: LocalDate): DepositPosition = {
    val list = positionRepository.load[DepositPosition](account, AssetClassConstants.ASSET_CASH_DEPOSIT, asOfDate)
    if (list.isEmpty) {
      val p = DepositPosition(account.id, Currencies.CNY, asOfDate, 0)
      positionRepository.save(p)
      p
    } else if (list.size > 1) {
      throw new BusinessException("More than one receivable position is loaded")
    } else {
      list(0)
    }
  }
}