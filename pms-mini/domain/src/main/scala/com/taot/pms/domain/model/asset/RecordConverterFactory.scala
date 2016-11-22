package com.taot.pms.domain.model.asset

import com.taot.pms.domain.BusinessException
import com.taot.pms.domain.assetclass.{AssetClass, AssetClassConstants}
import com.taot.pms.domain.model.asset.cash.DepositPosition
import com.taot.pms.domain.model.asset.equity.EquityPosition

object RecordConverterFactory {

  import AssetClassConstants._

  def get(assetClass: AssetClass): RecordConverter = {
    assetClass match {
      case ASSET_SECURITY_EQUITY => EquityPosition
      case ASSET_CASH_DEPOSIT => DepositPosition
      case x =>
        throw new BusinessException("Failed to find record converter for asset class: " + assetClass)
    }

  }
}
