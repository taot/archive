package com.taot.pms.domain.model.transaction

import com.taot.pms.domain.transactionclass.{ TransactionClass, TransactionClassConstants }
import com.taot.pms.domain.model.asset.equity.EquityTradeTransactionStrategy
import com.taot.pms.domain.BusinessException
import com.taot.pms.common.RichDomainObjectFactory

object TransactionStrategyFactory {

  import TransactionClassConstants._

  def get(transactionClass: TransactionClass): TransactionStrategy = {

    val strategy = transactionClass match {
      case ASSET_SECURITY_EQUITY_BUY | ASSET_SECURITY_EQUITY_SELL =>
        new EquityTradeTransactionStrategy
      case _ =>
        throw new BusinessException("Unable to find transaction strategy for " + transactionClass)
    }

    RichDomainObjectFactory.autowire(strategy)
  }
}
