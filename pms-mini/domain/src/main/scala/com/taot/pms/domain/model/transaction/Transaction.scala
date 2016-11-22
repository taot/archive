package com.taot.pms.domain.model.transaction

import com.taot.pms.domain.transactionclass.TransactionClass
import org.joda.time.{LocalDate, LocalDateTime}

trait Transaction {

  def id: Long

  def transactionClass: TransactionClass

  def accountId: Long

  def asOfDate: LocalDate

  def executionTime: LocalDateTime
}
