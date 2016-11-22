package com.taot.pms.domain.transactionclass

import com.taot.pms.domain.assetclass.AssetClass

case class TransactionClass(
  code: String,
  assetClass: AssetClass,
  id: Long = -1
)