package com.taot.pms.domain.assetclass.impl

import com.taot.pms.domain.assetclass.AssetClass
import scala.collection.mutable


object AssetClassMerger {

  /*
   * Merge the DB asset classes and asset classes from config.
   * Config asset classes must be a superset of DB asset classes.
   */
  def merge(dbAssetClassRootOpt: Option[AssetClass], configAssetClassRoot: AssetClass): AssetClass = {
    dbAssetClassRootOpt match {
      case Some(dbAssetClassRoot) => mergeRecursive(dbAssetClassRoot, configAssetClassRoot)
      case None => configAssetClassRoot
    }

  }

  private def mergeRecursive(dbNode: AssetClass, configNode: AssetClass): AssetClass = {
    if (dbNode.id < 0) {
      throw new IllegalStateException("AssetClass from DB cannot have a negative id: " + dbNode.id)
    }

    if (dbNode.children.isEmpty != configNode.children.isEmpty) {
      throw new IllegalStateException("AssetClass hierarchy changed drastically and cannot be updated automatically.")
    }

    var nMatched = 0
    val buffer = mutable.ListBuffer.empty[AssetClass]

    for (node <- configNode.children) {
      val matched = findMatch(node, dbNode.children)
      if (matched != null) {
        nMatched += 1
        val newChild = mergeRecursive(matched, node)
        buffer.append(newChild)
      } else {
        buffer.append(node)
      }
    }

    if (nMatched != dbNode.children.size) {
      throw new IllegalStateException("AssetClass hierarchy in config is not superset to hierarchy in DB, and cannot be updated automatically.")
    }

    AssetClass(configNode.code, configNode.desc, buffer.toList, id = dbNode.id)
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
