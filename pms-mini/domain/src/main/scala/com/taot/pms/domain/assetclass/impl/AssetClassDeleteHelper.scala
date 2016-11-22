package com.taot.pms.domain.assetclass.impl

import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

import com.taot.pms.domain.assetclass.AssetClass
import com.taot.pms.persist.{AssetClass => DbAssetClass, DAL}
import com.taot.pms.persist.dsl._

class AssetClassDeleteHelper(dal: DAL) {

  def deleteAll(): Unit = {
    val dbAssetClass = new DbAssetClassLoader(dal)
    dbAssetClass.load() map { assetClassRoot =>
      deleteRecursive(assetClassRoot)
    }
  }

  private def deleteRecursive(node: AssetClass): Unit = {
    for (child <- node.children) {
      deleteRecursive(child)
    }

    import dal.profile.simple._
    dal.assetClasses.filter { _.id === node.id }.delete
  }

  private def loadAll(): List[DbAssetClass] = transaction {
    import dal.profile.simple._
    (for (l <- dal.assetClasses) yield l).list()
  }
}
