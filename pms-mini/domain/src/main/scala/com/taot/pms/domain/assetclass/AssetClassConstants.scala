package com.taot.pms.domain.assetclass

import com.taot.pms.common.RichDomainObjectFactory

object AssetClassConstants {

  private lazy val cache = RichDomainObjectFactory.getInstance(classOf[AssetClassCache])

  /*
   * Asset
   */
  lazy val ASSET = cache.getByLongCode("ASSET")

  /*
   * Cash
   */
  lazy val ASSET_CASH = cache.getByLongCode("ASSET.CASH")

  lazy val ASSET_CASH_DEPOSIT = cache.getByLongCode("ASSET.CASH.DEPOSIT")

  lazy val ASSET_CASH_COMMISSION = cache.getByLongCode("ASSET.CASH.COMMISSION")

  lazy val ASSET_CASH_FEE = cache.getByLongCode("ASSET.CASH.FEE")

  lazy val ASSET_CASH_PAYABLE = cache.getByLongCode("ASSET.CASH.PAYABLE")

  lazy val ASSET_CASH_RECEIVABLE = cache.getByLongCode("ASSET.CASH.RECEIVABLE")

  /*
   * Security
   */
  lazy val ASSET_SECURITY = cache.getByLongCode("ASSET.SECURITY")

  lazy val ASSET_SECURITY_EQUITY = cache.getByLongCode("ASSET.SECURITY.EQUITY")
}
