package com.taot.pms.persist

import com.taot.pms.common.logging.Logging

class TestWorker(implicit dal: DAL) extends Logging {

  /*
  import dal._
  import dal.profile.simple._

  val accountDao = new AccountRepositoryImpl(dal)

  def run(db: Database): Unit = {
    runSelectCompiled(db)
    printEmptyLines(10)
    runSelectCompiled(db)
  }

  private def runInsert(db: Database): Unit = {
    db.withTransaction { implicit session: Session =>
      val accountId = (accounts returning accounts.map(_.id)) += Account("Test Account")
      ("account id: " + accountId)
    }
  }

  private def runSelect(db: Database): Unit = {
    db.withTransaction { implicit session: Session =>
      val list: List[Account] = accounts.list
      logger.info("{}", list)
    }
  }

  def selectAccount(min: Column[Long]) =
    for {
      u <- accounts if u.id >= min
    } yield u.name

  //  val selectAccountsCompiled = Compiled(selectAccount _)

//  val selectAccountsCompiled = Compiled(
//  { for {
//    u <- accounts
//  } yield u }
//  )
//
//  val selectAccountsCompiled2 = Compiled(
//  { for (a <- accounts) yield a }
//  )

  private def runSelectCompiled(db: Database): Unit = {
    logger.info("start selecting account")
    db.withTransaction { implicit session: Session =>
      val v: Seq[Account] = accountDao.findAll
      println(v)
    }
  }

  private def printEmptyLines(n: Int): Unit = (0 until n) foreach { _ => println }
  */
}
