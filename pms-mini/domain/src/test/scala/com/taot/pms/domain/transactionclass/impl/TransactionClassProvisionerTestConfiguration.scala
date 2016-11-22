package com.taot.pms.domain.transactionclass.impl

import com.taot.pms.common.RichDomainObjectFactory
import com.taot.pms.common.config.impl.ReloadableConfigurator
import com.taot.pms.persist.{MySQLDAL, PersistInitializer}
import org.springframework.scala.context.function.FunctionalConfiguration
import com.taot.pms.domain.assetclass.impl.AssetClassCacheAutoUpgradeImpl

class TransactionClassProvisionerTestConfiguration extends FunctionalConfiguration {

  val configurator = singleton() {
    new ReloadableConfigurator()
  }

  val persistInitializer = singleton() {
    new PersistInitializer(configurator)
  }

  val richDomainObjectFactory = singleton() {
    RichDomainObjectFactory // be aware that this is an object!
  }

  val dal = singleton() {
    new MySQLDAL
  }
}
