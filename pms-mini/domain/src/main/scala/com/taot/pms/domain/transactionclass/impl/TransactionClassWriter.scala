package com.taot.pms.domain.transactionclass.impl

import com.taot.pms.domain.transactionclass.TransactionClass

trait TransactionClassWriter {

  def write(list: List[TransactionClass]): Unit
}
