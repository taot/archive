package com.taot.pms.domain.assetclass.impl

import com.taot.pms.persist.DAL
import com.taot.pms.domain.assetclass.AssetClass

class AssetClassCacheAutoUpgradeImpl(dal: DAL, resourceName: String) extends AssetClassCacheImpl(dal, resourceName) {

  override protected def loadAssetClassHierarchy(): (AssetClass, Map[Long, AssetClass]) = {
    val provisioner = new AssetClassProvisioner(dal, resourceName)
    provisioner.run()
    super.loadAssetClassHierarchy()
  }
}
