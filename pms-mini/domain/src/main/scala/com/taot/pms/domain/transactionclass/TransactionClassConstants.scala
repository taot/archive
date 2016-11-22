package com.taot.pms.domain.transactionclass

import com.taot.pms.common.RichDomainObjectFactory

object TransactionClassConstants {

  private lazy val cache = RichDomainObjectFactory.getInstance(classOf[TransactionClassCache])

  lazy val ASSET_SECURITY_EQUITY_BUY = cache.getByLongCode("ASSET.SECURITY.EQUITY|BUY")

  lazy val ASSET_SECURITY_EQUITY_SELL = cache.getByLongCode("ASSET.SECURITY.EQUITY|SELL")
}
