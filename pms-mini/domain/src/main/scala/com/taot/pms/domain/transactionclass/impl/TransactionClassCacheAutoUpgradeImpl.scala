package com.taot.pms.domain.transactionclass.impl

import com.taot.pms.persist.DAL
import com.taot.pms.domain.assetclass.AssetClassCache
import com.taot.pms.domain.transactionclass.TransactionClass

class TransactionClassCacheAutoUpgradeImpl(dal: DAL, resourceName: String, assetClassCache: AssetClassCache) extends TransactionClassCacheImpl(dal, resourceName, assetClassCache) {

  override def load(): List[TransactionClass] = {
    val provisioner = new TransactionClassProvisioner(dal, assetClassCache, resourceName)
    provisioner.run()
    super.load()
  }
}
