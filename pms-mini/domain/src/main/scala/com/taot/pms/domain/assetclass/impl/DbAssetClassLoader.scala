package com.taot.pms.domain.assetclass.impl

import com.taot.pms.domain.assetclass.AssetClass
import com.taot.pms.persist.{ AssetClass => DbAssetClass }
import com.taot.pms.persist.DAL
import com.taot.pms.persist.dsl.transaction
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
import scala.collection.mutable
import com.taot.pms.domain.BusinessException


class DbAssetClassLoader(dal: DAL) extends AssetClassLoader {

  type AssetClassParentMap = mutable.Map[Long, mutable.ListBuffer[DbAssetClass]]

  override def load(): Option[AssetClass] = {
    import dal.profile.simple._
    val assetClasses = transaction {
      (for (l <- dal.assetClasses) yield l).list()
    }

    if (assetClasses.isEmpty) {
      return None
    }

    val (rootAssetClass, parentMap) = findRootAndParentMap(assetClasses)
    val root = makeTreeRecursive(rootAssetClass, parentMap)
    checkHierarchy(root, parentMap)

    Some(root)
  }

  private def makeTreeRecursive(anAssetClass: DbAssetClass, parentMap: AssetClassParentMap): AssetClass = {

    val children = parentMap.get(anAssetClass.id) match {
      case Some(buffer) =>
        buffer.map { l =>
          makeTreeRecursive(l, parentMap)
        }.toList

      case None =>
        List.empty[AssetClass]
    }
    AssetClass(anAssetClass.code, anAssetClass.desc, children, anAssetClass.id)
  }

  private def findRootAndParentMap(assetClasss: List[DbAssetClass]): (DbAssetClass, AssetClassParentMap) = {
    val rootAssetClass = assetClasss.filter(_.parentId.isEmpty) match {
      case l :: Nil => l
      case Nil => throw new BusinessException("AssetClass hierarchy incorrect. No root assetClass")
      case ls => throw new BusinessException("AssetClass hierarchy incorrect. Multiple roots")
    }

    val parentMap = mutable.Map.empty[Long, mutable.ListBuffer[DbAssetClass]]

    for (l <- assetClasss) {
      l.parentId match {
        case Some(pid) =>
          val buffer = parentMap.get(pid).getOrElse {
            val b = mutable.ListBuffer.empty[DbAssetClass]
            parentMap.put(pid, b)
            b
          }
          buffer.append(l)

        case None => // ignore root assetClasss
      }
    }

    (rootAssetClass, parentMap)
  }

  private def checkHierarchy(root: AssetClass, parentMap: AssetClassParentMap) = {
    val hierCount = countRecursive(root)
    val parentMapCount = parentMap.values.foldLeft(0) { (accu, buffer) => accu + buffer.size }
    if (hierCount != parentMapCount + 1) {
      throw new BusinessException("AssetClass hierarchy incorrect. Node count does not match")
    }
  }

  private def countRecursive(assetClass: AssetClass): Int = {
    val childCount = if (assetClass.isLeaf) {
      0
    } else {
      assetClass.children.foldLeft(0) { (accu, child) => accu + countRecursive(child) }
    }

    childCount + 1
  }
}
