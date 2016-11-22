package com.taot.pms.domain.assetclass.impl

import com.taot.pms.domain.assetclass.AssetClass

trait AssetClassLoader {

  def load(): Option[AssetClass]
}
