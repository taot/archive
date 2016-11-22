package com.taot.pms.domain.model.asset.equity

import com.taot.pms.domain.model.account.Account
import org.joda.time.LocalDate
import com.taot.pms.marketdata.model.Security

trait EquityPositionRepository {

  def find(account: Account, asOfDate: LocalDate, security: Security): EquityPosition

  def find(account: Account, asOfDate: LocalDate): List[EquityPosition]

  def save(position: EquityPosition)
}
