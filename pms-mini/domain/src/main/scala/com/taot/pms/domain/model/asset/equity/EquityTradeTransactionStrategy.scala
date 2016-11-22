package com.taot.pms.domain.model.asset.equity

import com.taot.pms.domain.model.transaction.{SecurityTransaction, Transaction, TransactionStrategy}
import com.taot.pms.domain.model.account.Account
import org.springframework.beans.factory.annotation.Autowired
import com.taot.pms.domain.transactionclass.TransactionClassConstants._
import com.taot.pms.domain.BusinessException

class EquityTradeTransactionStrategy extends TransactionStrategy {

  @Autowired
  protected var equityPositionRepository: EquityPositionRepository = _

  override def process(account: Account, tran: Transaction): Unit = {
    val secTran = tran.asInstanceOf[SecurityTransaction]

    val asOfDate = secTran.asOfDate
    val security = secTran.security

    val equityP = {
      val p = equityPositionRepository.find(account, asOfDate, security)
      if (p == null) {
        EquityPosition(account.id, security, asOfDate, 0, 0)
      } else {
        p
      }
    }

    // Update position (including carrying value)
    secTran.transactionClass match {
      case ASSET_SECURITY_EQUITY_BUY => equityP.buy(secTran.amount.toLong, secTran.price)
      case ASSET_SECURITY_EQUITY_SELL => equityP.sell(secTran.amount.toLong, secTran.price)
      case _ => throw new BusinessException("Unsupported transaction: " + secTran.transactionClass)
    }

    equityPositionRepository.save(equityP)
  }
}
