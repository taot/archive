package com.taot.pms.domain.model.transaction

import com.taot.pms.domain.transactionclass.TransactionClass
import org.joda.time.{LocalDateTime, LocalDate}

case class CashTransaction(
  id: Long,
  transactionClass: TransactionClass,
  accountId: Long,
  asOfDate: LocalDate,
  executionTime: LocalDateTime,
  currencyCode: String,
  amount: BigDecimal
) extends Transaction