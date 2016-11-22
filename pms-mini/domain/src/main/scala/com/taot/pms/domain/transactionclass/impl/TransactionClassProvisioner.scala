package com.taot.pms.domain.transactionclass.impl

import com.taot.pms.persist.DAL
import com.taot.pms.domain.assetclass.AssetClassCache
import com.taot.pms.domain.transactionclass.TransactionClass
import java.util.concurrent.atomic.AtomicLong

class TransactionClassProvisioner(dal: DAL, assetClassCache: AssetClassCache, resourceName: String) {

  def run(): Unit = {
    val xmlLoader = new XmlTransactionClassLoader(resourceName, assetClassCache)
    val xmlTransactionClasses = xmlLoader.load()
    val dbLoader = new DbTransactionClassLoader(dal, assetClassCache)
    val dbTransactionClasses = dbLoader.load()

    val merged = TransactionClassMerger.merge(dbTransactionClasses, xmlTransactionClasses)
    val maxId = findMaxId(merged)
    val nextId = if (maxId < 0) 1 else maxId + 1
    val assigned = assignIds(merged, new AtomicLong(nextId))

    val writer = new DbTransactionClassWriter(dal)
    writer.write(assigned)
  }
  
  private def assignIds(list: List[TransactionClass], nextId: AtomicLong): List[TransactionClass] = {
    list.map { tc =>
      if (tc.id > 0) {
        tc
      } else {
        tc.copy(id = nextId.getAndIncrement)
      }
    }
  }

  private def findMaxId(list: List[TransactionClass]): Long = {
    list.map(_.id).max
  }
}
