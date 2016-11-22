package com.taot.pms.domain.transactionclass.impl

import com.taot.pms.domain.transactionclass.TransactionClass
import scala.collection.mutable

object TransactionClassMerger {

  def merge(dbTrasnactionClasses: List[TransactionClass], configTransactionClasses: List[TransactionClass]): List[TransactionClass] = {
    val buffer = mutable.ListBuffer.empty[TransactionClass]

    val dbSet = mutable.Set.empty[TransactionClass] ++ dbTrasnactionClasses
    for (tc <- configTransactionClasses) {
      dbTrasnactionClasses.find { c => c.code == tc.code } match {
        case Some(c) =>
          buffer.append(c)
          dbSet.remove(c)
        case None =>
          buffer.append(tc)
      }
    }

    buffer.toList
  }
}
