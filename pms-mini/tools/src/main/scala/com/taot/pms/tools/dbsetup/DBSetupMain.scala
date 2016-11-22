package com.taot.pms.tools.dbsetup

import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import com.taot.pms.domain.assetclass.impl.{AssetClassCacheImpl, AssetClassProvisioner}
import com.taot.pms.persist.MySQLDAL
import com.taot.pms.domain.transactionclass.impl.TransactionClassProvisioner

object DBSetupMain {

  def main(args: Array[String]): Unit = {
    val context = FunctionalConfigApplicationContext(classOf[DBSetupConfiguration])
    val dal = context.getBean(classOf[MySQLDAL])

    val assetClassResource = "asset-class.xml"
    val transactionClassResource = "transaction-class.xml"

    val assetClassProvisioner = new AssetClassProvisioner(dal, assetClassResource)
    assetClassProvisioner.run()

    val assetClassCache = new AssetClassCacheImpl(dal, assetClassResource)
    val transactionClassProvisioner = new TransactionClassProvisioner(dal, assetClassCache, transactionClassResource)
    transactionClassProvisioner.run()
  }
}
