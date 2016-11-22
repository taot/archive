package com.taot.pms.domain.assetclass.impl

import com.taot.pms.domain.assetclass.AssetClass

trait AssetClassWriter {

  def write(assetClassRoot: AssetClass): Unit
}
