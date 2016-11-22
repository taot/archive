package com.taot.pms.domain.model.transaction

import com.taot.pms.domain.model.account.Account

trait TransactionStrategy {

  def process(account: Account, tran: Transaction): Unit
}
