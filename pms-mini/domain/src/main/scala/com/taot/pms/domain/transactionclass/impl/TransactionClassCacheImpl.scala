package com.taot.pms.domain.transactionclass.impl

import com.taot.pms.domain.transactionclass.{TransactionClass, TransactionClassCache}
import com.taot.pms.persist.DAL
import com.taot.pms.domain.assetclass.AssetClassCache
import com.taot.pms.domain.BusinessException

class TransactionClassCacheImpl(dal: DAL, resourceName: String, assetClassCache: AssetClassCache) extends TransactionClassCache {

  private val list = load()

  override def getAll(): List[TransactionClass] = list

  override def getByLongCode(code: String): TransactionClass = {
    val parts = code.split("|")
    if (parts.size != 2) {
      throw new BusinessException("Invalid transaction long code: " + code)
    }
    val assetClassLongCode = parts(0)
    val transactionClassCode = parts(1)
    list.find { tc =>
      val assetClass = assetClassCache.getByLongCode(assetClassLongCode)
      tc.code == transactionClassCode && tc.assetClass == assetClass
    }.getOrElse {
      throw new BusinessException("Cannot find transaction class by long code: " + code)
    }
  }

  override def getById(id: Long): TransactionClass = {
    list.find(_.id == id).getOrElse {
      throw new BusinessException("Cannot find transaction class by id: " + id)
    }
  }

  protected def load(): List[TransactionClass] = {
    val dbLoader = new DbTransactionClassLoader(dal, assetClassCache)
    val xmlLoader = new XmlTransactionClassLoader(resourceName, assetClassCache)
    val dbList = dbLoader.load()
    val xmlList = xmlLoader.load()
    TransactionClassChecker.check(dbList, xmlList)
    dbList
  }
}
