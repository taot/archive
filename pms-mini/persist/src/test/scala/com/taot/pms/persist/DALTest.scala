package com.taot.pms.persist

import org.scalatest.{WordSpec, ShouldMatchers}
import scala.slick.driver.MySQLDriver
import scala.slick.jdbc.JdbcBackend.Database


class DALTest extends WordSpec with ShouldMatchers {
/*
  "DAL" should {

    "CRUD" in {
      val db = Database.forURL("jdbc:mysql://localhost:3306/pmsdb", driver = "com.mysql.jdbc.Driver", user = "root")
      val dal = new DAL(MySQLDriver)

      val repository = new AccountRepositoryImpl(dal)
      db.withDynTransaction {
        val a = repository.findById(1)
        println("Account: " + a.name)
      }


      db.withDynTransaction {
//        val accountId = (accounts returning accounts.map(_.id)) += Account("Test Account")
        val insertInvoker = (accounts returning accounts.map(_.id)).insertInvoker
        val accountId = insertInvoker += Account("Test Account")
        println("account id: " + accountId)
        val positionId = (positions returning positions.map(_.id)) += Position(accountId)
        println("position id: " + positionId)
        val ledgerId = 2L
        positionLedgers += PositionLedger(positionId, ledgerId, Some(2L), Some("CNY"))
        positionHists += PositionHist(positionId, ledgerId, LocalDate.now, BigDecimalConstants.ONE)
      }
    }
  }
*/
}
