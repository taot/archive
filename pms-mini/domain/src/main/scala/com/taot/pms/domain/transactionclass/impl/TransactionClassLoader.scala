package com.taot.pms.domain.transactionclass.impl

import com.taot.pms.domain.transactionclass.TransactionClass

trait TransactionClassLoader {

  def load(): List[TransactionClass]
}
