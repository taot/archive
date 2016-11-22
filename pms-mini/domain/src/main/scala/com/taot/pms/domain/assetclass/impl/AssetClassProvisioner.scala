package com.taot.pms.domain.assetclass.impl

import java.util.concurrent.atomic.AtomicLong

import scala.collection.mutable

import com.taot.pms.domain.BusinessException
import com.taot.pms.domain.assetclass.AssetClass
import com.taot.pms.persist.DAL

class AssetClassProvisioner(dal: DAL, resourceName: String) {

  def run(): Unit = {
    val xmlLoader = new XmlAssetClassLoader(resourceName)
    val xmlAssetClass = xmlLoader.load().getOrElse {
      throw new BusinessException("Failed to load asset classes from resource " + resourceName)
    }
    val dbLoader = new DbAssetClassLoader(dal)
    val dbAssetClassOpt = dbLoader.load()

    val merged = AssetClassMerger.merge(dbAssetClassOpt, xmlAssetClass)
    val maxId = findMaxIdRecursive(merged)
    val nextId = if (maxId < 0) 1 else maxId + 1
    val assigned = assignIdsRecursive(merged, new AtomicLong(nextId))
    val pMap = mutable.Map.empty[Long, AssetClass]
    createParentMapRecursive(assigned, null, pMap)
    val pMap2 = pMap.toMap

    val dbWriter = new DbAssetClassWriter(dal, pMap2)
    dbWriter.write(assigned)
  }

  /*
   * Use DFS to assign the assetClass ids
   */
  private def assignIdsRecursive(node: AssetClass, nextId: AtomicLong): AssetClass = {
    val newId = if (node.id < 0) nextId.getAndIncrement else node.id
    val children = for {
      c <- node.children
      child = assignIdsRecursive(c, nextId)
    } yield child

    node.copy(id = newId, children = children)
  }

  private def createParentMapRecursive(node: AssetClass, parent: AssetClass, map: mutable.Map[Long, AssetClass]): Unit = {
    if (parent != null) {
      map.put(node.id, parent)
    }
    for (l <- node.children) {
      createParentMapRecursive(l, node, map)
    }
  }
  
  private def findMaxIdRecursive(assetClass: AssetClass): Long = {
    (assetClass.id :: assetClass.children.map(findMaxIdRecursive(_))).max
  }
}
