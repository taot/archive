package com.taot.pms.domain.assetclass.impl

import scala.collection.mutable

import com.taot.pms.domain.BusinessException
import com.taot.pms.domain.assetclass.{AssetClass, AssetClassCache}
import com.taot.pms.persist.DAL

class AssetClassCacheImpl(dal: DAL, resourceName: String) extends AssetClassCache {

  private val (assetClassRoot, parentMap) = loadAssetClassHierarchy()

  private val idMap = createIdMap(assetClassRoot)

  override def getAllLeafDescendents(assetClass: AssetClass): Set[AssetClass] = {
    val leaves = getLeafDescendetsRecursive(assetClass)
    leaves
  }

  override def isDescendentOf(assetClass: AssetClass): Boolean = ???

  override def getParent(assetClass: AssetClass): Option[AssetClass] = getParent(assetClass.id)

  override def getParent(id: Long): Option[AssetClass] = {
    if (! idMap.contains(id)) {
      throw new BusinessException("Asset class not found by id " + id)
    }
    this.parentMap.get(id)
  }

  override def getByLongCode(longCode: Seq[String]): AssetClass = {
    if (longCode == null || longCode.isEmpty) {
      throw new BusinessException("Long code cannot be null or empty")
    }
    findByCodeRecursive(assetClassRoot, longCode.toList).getOrElse {
      throw new BusinessException("Cannot find asset class by long code: " + longCode.mkString("."))
    }
  }

  private def findByCodeRecursive(node: AssetClass, codes: List[String]): Option[AssetClass] = {
    codes match {
      case code :: Nil => Some(node)
      case x1 :: x2 :: xs =>
        node.children.find(_.code == x2) match {
          case Some(n) => findByCodeRecursive(n, x2 :: xs)
          case None => None
        }
      case _ => None
    }
  }

  override def getByLongCode(longCode: String): AssetClass = {
    val splitted = longCode.split("\\.")
    getByLongCode(splitted)
  }

  override def getById(id: Long): AssetClass = {
    idMap.get(id).getOrElse {
      throw new BusinessException("Asset class not found by id " + id)
    }
  }

  override def getAll(): List[AssetClass] = {
    idMap.values.toList
  }

  protected def loadAssetClassHierarchy(): (AssetClass, Map[Long, AssetClass]) = {
    val xmlLoader = new XmlAssetClassLoader(resourceName)
    val xmlassetClass = xmlLoader.load().getOrElse {
      throw new BusinessException("Failed to load asset classs from resource " + resourceName)
    }
    val dbLoader = new DbAssetClassLoader(dal)
    val dbAssetClass = dbLoader.load().getOrElse {
      throw new BusinessException("Failed to load asset classs from database")
    }

    AssetClassChecker.check(dbAssetClass, xmlassetClass)

    val pMap = mutable.Map.empty[Long, AssetClass]
    createParentMapRecursive(dbAssetClass, null, pMap)

    (dbAssetClass, pMap.toMap)
  }

  private def createParentMapRecursive(node: AssetClass, parent: AssetClass, map: mutable.Map[Long, AssetClass]): Unit = {
    if (parent != null) {
      map.put(node.id, parent)
    }
    for (l <- node.children) {
      createParentMapRecursive(l, node, map)
    }
  }

  private def createIdMap(node: AssetClass): Map[Long, AssetClass] = {
    val aMap = mutable.Map.empty[Long, AssetClass]
    createIdMapRecursive(node, aMap)
    aMap.toMap
  }

  private def createIdMapRecursive(node: AssetClass, map: mutable.Map[Long, AssetClass]): Unit = {
    map.put(node.id, node)
    for (l <- node.children) {
      createIdMapRecursive(l, map)
    }
  }

  private def getLeafDescendetsRecursive(assetClass: AssetClass): Set[AssetClass] = if (assetClass.isLeaf) {
    Set(assetClass)
  } else {
    assetClass.children.foldLeft(Set.empty[AssetClass]) { (set, ac) =>
      val leaves = getLeafDescendetsRecursive(ac)
      leaves ++ set
    }
  }
}
