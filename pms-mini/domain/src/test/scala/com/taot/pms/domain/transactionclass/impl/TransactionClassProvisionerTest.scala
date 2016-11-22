package com.taot.pms.domain.transactionclass.impl

import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

import org.scalatest._
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import com.taot.pms.persist.dsl.transaction
import com.taot.pms.persist.MySQLDAL
import com.taot.pms.persist.{ TransactionClass => DbTransactionClass }
import com.taot.pms.domain.assetclass.AssetClassCache
import com.taot.pms.domain.assetclass.impl.{AssetClassCacheAutoUpgradeImpl, AssetClassDeleteHelper}

class TransactionClassProvisionerTest extends WordSpec with ShouldMatchers with BeforeAndAfterAll {


  protected var applicationContext: ConfigurableApplicationContext = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    applicationContext = FunctionalConfigApplicationContext(classOf[TransactionClassProvisionerTestConfiguration])
    deleteAll()
  }

  override protected def afterAll(): Unit = {
    applicationContext.close()
    super.afterAll()
  }

  "TransactionClassProvisioner" should {

    "provision and update transaction class" in {
      val dal = applicationContext.getBean(classOf[MySQLDAL])
      val assetClassCache = new AssetClassCacheAutoUpgradeImpl(dal, "asset-class-test.xml")
      val provisioner = new TransactionClassProvisioner(dal, assetClassCache, "transaction-class-test.xml")
      provisioner.run()
      loadAll().size should equal (2)

      val provisioner2 = new TransactionClassProvisioner(dal, assetClassCache, "transaction-class-test-updated.xml")
      provisioner2.run()
      loadAll().size should equal (3)
    }
  }

  private def loadAll(): List[DbTransactionClass] = transaction {
    val dal = applicationContext.getBean(classOf[MySQLDAL])
    import dal.profile.simple._
    dal.transactionClasses.list()
  }

  private def deleteAll(): Unit = transaction {
    val dal = applicationContext.getBean(classOf[MySQLDAL])
    import dal.profile.simple._
    dal.transactionClasses.delete

    val deleteHelper = new AssetClassDeleteHelper(dal)
    deleteHelper.deleteAll()
  }
}
