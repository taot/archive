package com.taot.pms.domain.transactionclass.impl

import com.taot.pms.domain.transactionclass.TransactionClass
import com.taot.pms.domain.BusinessException

object TransactionClassChecker {

  def check(dbTransactionClasses: List[TransactionClass], configTransactionClasses: List[TransactionClass]): Unit = {
    var nMatched = 0
    for (tc <- dbTransactionClasses) {
      configTransactionClasses.find(_.code == tc.code) match {
        case Some(c) => nMatched += 1
        case None =>
      }
    }
    if (nMatched != dbTransactionClasses.size || nMatched != configTransactionClasses.size) {
      throw new BusinessException("Transaction classes in database do not match those in config")
    }
  }
}
