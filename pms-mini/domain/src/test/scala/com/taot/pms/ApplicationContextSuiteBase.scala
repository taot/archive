package com.taot.pms

import org.scalatest.{BeforeAndAfterAll, Suite}
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import com.taot.pms.persist.MySQLDAL
import com.taot.pms.domain.assetclass.impl.AssetClassDeleteHelper
import com.taot.pms.persist.dsl.transaction

trait ApplicationContextSuiteBase extends Suite with BeforeAndAfterAll {

  protected var applicationContext: ConfigurableApplicationContext = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    applicationContext = FunctionalConfigApplicationContext(classOf[ServerTestConfiguration])

//    deleteAssetClassHierarchy()
  }

  override protected def afterAll(): Unit = {
    applicationContext.close()
    super.afterAll()
  }

  private def deleteAssetClassHierarchy(): Unit = transaction {
    val dal = applicationContext.getBean(classOf[MySQLDAL])
    val deleteHelper = new AssetClassDeleteHelper(dal)
    deleteHelper.deleteAll()
  }
}
