package com.taot.pms.domain.model.asset

import org.joda.time.LocalDate
import com.taot.pms.domain.assetclass.AssetClass

trait Position {

  def id: Long

  def accountId: Long

  def assetClass: AssetClass

  def asOfDate: LocalDate

  def renewDaily(asOfDate: LocalDate): Position
}