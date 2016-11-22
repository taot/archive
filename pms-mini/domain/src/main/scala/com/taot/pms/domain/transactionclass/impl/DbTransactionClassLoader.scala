package com.taot.pms.domain.transactionclass.impl

import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

import com.taot.pms.domain.assetclass.AssetClassCache
import com.taot.pms.domain.transactionclass.TransactionClass
import com.taot.pms.persist.DAL
import com.taot.pms.persist.dsl.transaction

class DbTransactionClassLoader(dal: DAL, assetClassCache: AssetClassCache) extends TransactionClassLoader {

  override def load(): List[TransactionClass] = transaction {
    import dal.profile.simple._
    val list = (for (tc <- dal.transactionClasses) yield tc).list()
    for {
      tc <- list
      assetClass = assetClassCache.getById(tc.assetClassId)
    } yield TransactionClass(tc.code, assetClass, tc.id)
  }
}
