package com.taot.pms.domain.model.asset

import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

import com.taot.pms.domain.model.account.Account
import com.taot.pms.domain.assetclass.{AssetClassCache, AssetClass}
import org.joda.time.LocalDate
import com.taot.pms.persist.DAL
import com.taot.pms.persist.{ Position => DbPosition }
import com.taot.pms.domain.model.asset.cash.CashPosition
import com.taot.pms.domain.BusinessException
import com.taot.pms.persist.{ PositionHist => DbPositionHist }
import com.taot.pms.marketdata.model.Security

class PositionRepositoryImpl(dal: DAL, assetClassCache: AssetClassCache) extends PositionRepository {

  import dal.profile.simple._
  import dal.localDateColumnType

  override def load[T <: Position](account: Account, assetClass: AssetClass, asOfDate: LocalDate): List[T] = {
    val assetClasses = assetClassCache.getAllLeafDescendents(assetClass)
    val assetClassIds = assetClasses.map(_.id)
    val query = for {
      p <- dal.positions if p.accountId === account.id && p.assetClassId.inSet(assetClassIds)
      ph <- dal.positionHists if ph.positionId === p.id && ph.asOfDate === asOfDate
    } yield (p, ph)
    val allRecords = query.list()
    convertRecordsToModels[T](allRecords)
  }

  override def load[T <: SecurityPosition](account: Account, assetClass: AssetClass, security: Security, asOfDate: LocalDate): List[T] = {
    val assetClasses = assetClassCache.getAllLeafDescendents(assetClass)
    val assetClassIds = assetClasses.map(_.id)
    val query = for {
      p <- dal.positions if p.accountId === account.id && p.assetClassId.inSet(assetClassIds) && p.securityIdOpt.isNotNull && p.securityIdOpt.get === security.id
      ph <- dal.positionHists if ph.positionId === p.id && ph.asOfDate === asOfDate
    } yield (p, ph)
    val allRecords = query.list()
    convertRecordsToModels[T](allRecords)
  }

  private def convertRecordsToModels[T](records: List[(DbPosition, DbPositionHist)]): List[T] = {
    val returns = records.groupBy { case (p, ph) => p.id } map { case (positionId, list) =>
      val pos = list.head._1
      val hists = list.map(_._2)
      val assetClass = assetClassCache.getById(pos.assetClassId)
      val converter = RecordConverterFactory.get(assetClass)
      val position = converter.fromRecords(pos, hists)
      position.asInstanceOf[T]
    }
    returns.toList
  }

  override def save[T <: Position](position: T): T = {
    val assetClass = position.assetClass
    val converter = RecordConverterFactory.get(assetClass)
    val positionHists = converter.toRecords(position)

    checkRecords(positionHists)

    val ph0 = positionHists.head
    val accountId = position.accountId
    val asOfDate = position.asOfDate
    val (securityIdOpt, currencyCodeOpt) = position match {
      case sp: SecurityPosition => (Some(sp.security.id), None)
      case cp: CashPosition => (None, Some(cp.currencyCode))
    }

    val positionId = if (ph0.positionId < 0) {
      // need to create the position
      val pos = DbPosition(accountId, assetClass.id, assetClass.code, securityIdOpt, currencyCodeOpt)
      val pid = (dal.positions returning dal.positions.map(_.id)) += pos
      pid
    } else {
      val pos = dal.positions.filter { _.id === ph0.positionId}.firstOption.getOrElse {
        throw new BusinessException("Failed to find position by id " + ph0.positionId)
      }
      pos.id
    }

    for (ph <- positionHists) {
      val toSave = if (ph.positionId < 0) ph.copy(positionId = positionId) else ph
      val q = dal.positionHists.filter { h => h.positionId === positionId && h.asOfDate === asOfDate && h.ledgerId === ph.ledgerId }
      q.firstOption match {
        case Some(h) => dal.positionHists.update(toSave)
        case None => dal.positionHists += toSave
      }
    }

    position
  }

  private def checkRecords(records: List[DbPositionHist]): Unit = {
    // positionId, accountId, asOfDate should be same
    val (positionId, accountId, asOfDate) = records.headOption match {
      case Some(h) => (h.positionId, h.accountId, h.asOfDate)
      case None => throw new BusinessException("Position hist records is empty")
    }
    for (ph <- records) {
      if (ph.positionId != positionId) {
        throw new BusinessException("Record check failed. Position Ids are not same")
      }
      if (ph.accountId != accountId) {
        throw new BusinessException("Record check failed. Account Ids are not same")
      }
      if (ph.asOfDate != asOfDate) {
        throw new BusinessException("Record check failed. As of Dates are not same")
      }
    }
  }
}
