package com.taot.pms.domain.assetclass.impl

import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

import com.taot.pms.persist.{AssetClass => DbLedger, MySQLDAL}
import com.taot.pms.persist.dsl._
import org.scalatest.{BeforeAndAfterAll, ShouldMatchers, WordSpec}
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.scala.context.function.FunctionalConfigApplicationContext

class AssetClassProvisionerTest extends WordSpec with ShouldMatchers with BeforeAndAfterAll {

  protected var applicationContext: ConfigurableApplicationContext = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    applicationContext = FunctionalConfigApplicationContext(classOf[AssetClassProvisionerTestConfiguration])
  }

  override protected def afterAll(): Unit = {
    applicationContext.close()
    super.afterAll()
  }

  "AssetClassProvisioner" should {

    "update asset classes in database" in {
      deleteAll()
      val dal = applicationContext.getBean(classOf[MySQLDAL])
      val provisionRunner = new AssetClassProvisioner(dal, "asset-class-test.xml")
      provisionRunner.run()
      loadAll().size should equal (6)

      val provisionRunner2 = new AssetClassProvisioner(dal, "asset-class-test-updated.xml")
      provisionRunner2.run()
      loadAll().size should equal (8)
    }
  }

  private def deleteAll(): Unit = transaction {
    val dal = applicationContext.getBean(classOf[MySQLDAL])
    import dal.profile.simple._
    dal.transactionClasses.delete
    val deleteHelper = new AssetClassDeleteHelper(dal)
    deleteHelper.deleteAll()
  }

  private def loadAll(): List[DbLedger] = transaction {
    val dal = applicationContext.getBean(classOf[MySQLDAL])
    import dal.profile.simple._
    (for (l <- dal.assetClasses) yield l).list()
  }

}
