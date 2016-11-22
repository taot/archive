package com.taot.pms.persist

import java.sql.Date
import scala.slick.driver.JdbcProfile
import org.joda.time.LocalDate

/*
 * Profile - a trait that is used in the cake pattern
 */
trait Profile {

  val profile: JdbcProfile

  import profile.simple._

  implicit val localDateColumnType = MappedColumnType.base[LocalDate, Date](
    { ld => new Date(ld.toDate.getTime) },
    { d => LocalDate.fromDateFields(d) }
  )
}

/*
 * Account
 */
case class Account(name: String, openDate: LocalDate, id: Long = -1)

trait AccountComponent { this: Profile =>
  import profile.simple._

  class Accounts(tag: Tag) extends Table[Account](tag, "account") {
    def id = column[Long]("account_id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("account_name")
    def openDate = column[LocalDate]("open_date")
    def * = (name, openDate, id) <> (Account.tupled, Account.unapply)
  }

  val accounts = TableQuery[Accounts]
}


/*
 * AssetClass
 */
case class AssetClass(id: Long, code: String, desc: String, parentId: Option[Long])

trait AssetClassComponent { this: Profile =>
  import profile.simple._

  class AssetClasses(tag: Tag) extends Table[AssetClass](tag, "asset_class") {
    def id = column[Long]("asset_class_id", O.PrimaryKey)
    def code = column[String]("asset_class_code", O.NotNull)
    def desc = column[String]("asset_class_desc")
    def parentId = column[Option[Long]]("parent_id", O.Nullable)
    def * = (id, code, desc, parentId) <> (AssetClass.tupled, AssetClass.unapply)
  }

  val assetClasses = TableQuery[AssetClasses]
}


/*
 * TransactionClass
 */
case class TransactionClass(id: Long, code: String, desc: String, assetClassId: Long)

trait TransactionClassComponent { this: Profile =>
  import profile.simple._

  class TransactionClasses(tag: Tag) extends Table[TransactionClass](tag, "transaction_class") {
    def id = column[Long]("transaction_class_id", O.PrimaryKey)
    def code = column[String]("transaction_class_code", O.NotNull)
    def desc = column[String]("transaction_class_desc")
    def assetClassId = column[Long]("asset_class_id", O.NotNull)
    def * = (id, code, desc, assetClassId) <> (TransactionClass.tupled, TransactionClass.unapply)
  }

  val transactionClasses = TableQuery[TransactionClasses]
}


/*
 * Position
 */
case class Position(accountId: Long, assetClassId: Long, assetClassCode: String, securityIdOpt: Option[Long], currencyCodeOpt: Option[String], id: Long = -1)

trait PositionComponent { this: Profile =>
  import profile.simple._

  class Positions(tag: Tag) extends Table[Position](tag, "position") {
    def id = column[Long]("position_id", O.PrimaryKey, O.AutoInc)
    def accountId = column[Long]("account_id")
    def assetClassId = column[Long]("asset_class_id")
    def assetClassCode = column[String]("asset_class_code")
    def securityIdOpt = column[Option[Long]]("security_id")
    def currencyCodeOpt = column[Option[String]]("currency_code")
    def * = (accountId, assetClassId, assetClassCode, securityIdOpt, currencyCodeOpt, id) <> (Position.tupled, Position.unapply)
  }

  val positions = TableQuery[Positions]
}


/*
 * PositionHist
 */
case class PositionHist(positionId: Long, accountId: Long, asOfDate: LocalDate, ledgerId: Long, amount: BigDecimal)

trait PositionHistComponent { this: Profile =>
  import profile.simple._

  class PositionHists(tag: Tag) extends Table[PositionHist](tag, "position_hist") {
    def positionId = column[Long]("position_id")
    def accountId = column[Long]("account_id")
    def asOfDate = column[LocalDate]("as_of_date")
    def ledgerId = column[Long]("ledger_id")
    def amount = column[BigDecimal]("amount")
    def * = (positionId, accountId, asOfDate, ledgerId, amount) <> (PositionHist.tupled, PositionHist.unapply)
  }

  val positionHists = TableQuery[PositionHists]
}


/*
 * DAL
 */
class DAL(override val profile: JdbcProfile) extends Profile
  with AccountComponent
  with AssetClassComponent
  with TransactionClassComponent
  with PositionComponent
  with PositionHistComponent
