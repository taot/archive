package com.taot.pms.domain.model.transaction

import com.taot.pms.domain.transactionclass.TransactionClass
import org.joda.time.{LocalDateTime, LocalDate}
import com.taot.pms.marketdata.model.Security

case class SecurityTransaction(
  id: Long,
  transactionClass: TransactionClass,
  accountId: Long,
  asOfDate: LocalDate,
  executionTime: LocalDateTime,
  security: Security,
  amount: BigDecimal,
  price: BigDecimal,
  interest: BigDecimal
) extends Transaction