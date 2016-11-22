package com.taot.pms

import com.taot.pms.common.RichDomainObjectFactory
import com.taot.pms.common.config.impl.ReloadableConfigurator
import com.taot.pms.domain.assetclass.impl.AssetClassCacheAutoUpgradeImpl
import com.taot.pms.domain.model.account.AccountRepositoryImpl
import com.taot.pms.domain.model.asset.PositionRepositoryImpl
import com.taot.pms.persist.{MySQLDAL, PersistInitializer}
import org.springframework.scala.context.function.FunctionalConfiguration
import com.taot.pms.marketdata.impl.MarketDataServiceMockImpl

class ServerTestConfiguration extends FunctionalConfiguration {

  val configurator = singleton() {
    new ReloadableConfigurator()
  }

  val persistInitializer = singleton() {
    new PersistInitializer(configurator)
  }

  val richDomainObjectFactory = singleton() {
    RichDomainObjectFactory    // be aware that this is an object!
  }

  val marketDataService = singleton() {
    new MarketDataServiceMockImpl
  }

  val dal = singleton() {
    new MySQLDAL
  }

  val assetClassCache = singleton() {
    new AssetClassCacheAutoUpgradeImpl(dal, "asset-class-test.xml")
  }

  val accountRepository = singleton() {
    new AccountRepositoryImpl(dal)
  }

  val positionRepository = singleton() {
    new PositionRepositoryImpl(dal, assetClassCache)
  }

}
