package com.taot.pms.domain.model.asset

import com.taot.pms.domain.model.account.Account
import com.taot.pms.domain.assetclass.AssetClass
import org.joda.time.LocalDate
import com.taot.pms.marketdata.model.Security

trait PositionRepository {

  def load[T <: Position](account: Account, assetClass: AssetClass, asOfDate: LocalDate): List[T]

  def load[T <: SecurityPosition](account: Account, assetClass: AssetClass, security: Security, asOfDate: LocalDate): List[T]

  def save[T <: Position](position: T): T
}
