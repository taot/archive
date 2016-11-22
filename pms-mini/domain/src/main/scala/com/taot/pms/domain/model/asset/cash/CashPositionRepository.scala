package com.taot.pms.domain.model.asset.cash

import com.taot.pms.domain.model.account.Account
import org.joda.time.LocalDate

trait CashPositionRepository {

  def findDepositPosition(account: Account, asOfDate: LocalDate): DepositPosition

  def findFeePosition(account: Account, asOfDate: LocalDate): FeePosition

  def findPayablePosition(account: Account, asOfDate: LocalDate): PayablePosition

  def findReceivablePosition(account: Account, asOfDate: LocalDate): ReceivablePosition

  def update(position: CashPosition): Unit
}
