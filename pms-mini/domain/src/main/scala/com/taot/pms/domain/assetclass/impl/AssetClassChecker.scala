package com.taot.pms.domain.assetclass.impl

import com.taot.pms.domain.assetclass.AssetClass
import com.taot.pms.domain.BusinessException

object AssetClassChecker {

  def check(dbAssetClassRoot: AssetClass, configAssetClassRoot: AssetClass): Unit = {
    checkRecursive(dbAssetClassRoot, configAssetClassRoot)
  }

  private def checkRecursive(dbAssetClass: AssetClass, configAssetClass: AssetClass): Unit = {
    if (dbAssetClass.code != configAssetClass.code) {
      throw new BusinessException("AssetClass check failed. " + dbAssetClass.code + " in database does not match " + configAssetClass.code + " in config")
    }
    var nMatched = 0
    for (child <- dbAssetClass.children) {
      val matched = findMatch(child, configAssetClass.children)
      if (matched != null) {
        nMatched += 1
        checkRecursive(child, matched)
      } else {
        throw new BusinessException("Asset class " + child.code + " cannot find match in database")
      }
    }
    if (nMatched != configAssetClass.children.size) {
      throw new BusinessException("Numbers of children do not match under asset class " + dbAssetClass.code)
    }
  }

  private def findMatch(aNode: AssetClass, nodes: Seq[AssetClass]): AssetClass = {
    var matched: AssetClass = null
    for (n <- nodes) {
      if (n.code == aNode.code) {
        matched = n
      }
    }
    matched
  }
}
