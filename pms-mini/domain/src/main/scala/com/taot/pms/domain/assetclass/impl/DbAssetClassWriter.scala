package com.taot.pms.domain.assetclass.impl

import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

import com.taot.pms.common.logging.Logging
import com.taot.pms.domain.BusinessException
import com.taot.pms.domain.assetclass.AssetClass
import com.taot.pms.persist.{DAL, AssetClass => DbAssetClass}
import com.taot.pms.persist.dsl.transaction

class DbAssetClassWriter(dal: DAL, parentMap: Map[Long, AssetClass]) extends AssetClassWriter with Logging {

  private var nAdded = 0

  private var nRemains = 0

  private var nUpdated = 0

  override def write(assetClassRoot: AssetClass): Unit = transaction {
    logger.info("Updating assetClasss in database")
    writeToDb(assetClassRoot)
    logger.info("Asset classs updated in database. {} added, {} updated, {} remains", nAdded, nUpdated, nRemains)
  }

  private def writeToDb(node: AssetClass): Unit = {
    import dal.profile.simple._

    dal.assetClasses.filter { l => l.id === node.id }.firstOption match {
      case Some(dbAssetClass) =>
        if (dbAssetClass.code != node.code) {
          throw new BusinessException("Cannot update asset class code, original = " + dbAssetClass.code + ", updated = " + node.code)
        }
        if (dbAssetClass.parentId != parentMap.get(node.id).map(_.id)) {
          throw new BusinessException("Cannot update asset class's parent, original = " + dbAssetClass.parentId + ", updated = " + parentMap.get(node.id))
        }
        if (dbAssetClass.desc != node.desc) {
          val u = dbAssetClass.copy(desc = node.desc)
          dal.assetClasses.filter { l => l.id === node.id && l.code === node.code }.update(u)
          logger.info("Update asset class {}", node)
          nUpdated += 1
        } else {
          nRemains += 1
        }

      case None =>
        val parentIdOpt = parentMap.get(node.id).map(_.id)
        dal.assetClasses += DbAssetClass(node.id, node.code, node.desc, parentIdOpt)
        logger.info("Add assetClass {}", node)
        nAdded += 1
    }

    for (n <- node.children) {
      writeToDb(n)
    }
  }
}
