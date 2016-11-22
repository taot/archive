package com.taot.pms.persist


import com.taot.pms.common.logging.Logging
import org.scalatest.{ShouldMatchers, WordSpec}


class BoneCPTest extends WordSpec with ShouldMatchers with Logging {

  "BoneCP" should {

    /*
    "provide connection pool" in {
      val ds = new BoneCPDataSource()
      ds.setJdbcUrl("jdbc:mysql://localhost:3306/pmsdb")
      ds.setUsername("root")
      ds.setPassword("");
      ds.setAcquireRetryAttempts(3)
      ds.setStatementsCacheSize(200)

      // setup Slick
      implicit val dal1 = new DAL(MySQLDriver)
      val db1 = dal1.profile.simple.Database.forDataSource(ds)
      val tester1 = new TestWorker()(dal1)
      tester1.run(db1)



      implicit val dal2 = new DAL(MySQLDriver)
//      val db2 = dal2.profile.simple.Database.forDataSource(ds)
      val tester2 = new TestWorker()(dal2)
      tester2.run(db1)
      tester2.run(db1)
    }
    */
  }

/*
  "BoneCP" should {
    "provide connection pool" in {
      Class.forName("com.mysql.jdbc.Driver")
      // config bonecp
      val ds = new BoneCPDataSource()
      ds.setJdbcUrl("jdbc:mysql://localhost:3306/pmsdb")
      ds.setUsername("root")
      ds.setPassword("");
      ds.setAcquireRetryAttempts(3)
      ds.setStatementsCacheSize(200)
      // setup Slick
      val dal = new DAL(MySQLDriver)
      val db = dal.profile.simple.Database.forDataSource(ds)
      runSelectCompiled(db)
      printEmptyLines(10)
      runSelectCompiled(db)
    }
  }

  private def runInsert(dal: DAL, db: scala.slick.jdbc.JdbcBackend.): Unit = {
    import dal._
    import dal.profile.simple._
    db.withTransaction { implicit session: Session =>
      val accountId = (accounts returning accounts.map(_.id)) += Account("Test Account")
      ("account id: " + accountId)
    }
  }

  private def runSelect(dal: DAL, db: scala.slick.jdbc.JdbcBackend.Database): Unit = {
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

  val selectAccountsCompiled = Compiled(
    { for {
      u <- accounts
    } yield u }
  )

  val selectAccountsCompiled2 = Compiled(
    { for (a <- accounts) yield a }
  )

  private def runSelectCompiled(db: dal.profile.backend.DatabaseDef): Unit = {
    logger.info("start selecting account")
    db.withTransaction { implicit session: Session =>
      val v: Seq[Account] = selectAccountsCompiled2.run
      println(v)
    }
  }

  private def printEmptyLines(n: Int): Unit = (0 until n) foreach { _ => println }
*/
}
