package com.taot.pms.domain.model.asset

import com.taot.pms.ApplicationContextSuiteBase
import com.taot.pms.domain.model.account.{AccountRepository, Account}
import com.taot.pms.persist.dsl.transaction
import org.joda.time.LocalDate
import org.scalatest.{ShouldMatchers, WordSpec}
import com.taot.pms.domain.model.asset.equity.EquityPosition
import com.taot.pms.marketdata.model.Equity
import com.taot.pms.domain.model.asset.cash.{CommissionPosition, DepositPosition}
import com.taot.pms.domain.model.Currencies
import com.taot.pms.domain.assetclass.AssetClassConstants

class PositionRepositoryTest extends WordSpec with ApplicationContextSuiteBase with ShouldMatchers {

  "PositionRepository" should {

//    "save equity positions" in {
//      transaction {
//        val account = Account("Test Account", LocalDate.parse("2014-02-20"))
//        val security = Equity(1L, "GOOG")
//        account.create()
//        val equityP = EquityPosition(account.id, security, LocalDate.parse("2014-03-03"), 100, 1000)
//        val positionRepository = applicationContext.getBean(classOf[PositionRepository])
//        positionRepository.save(equityP)
//      }
//    }

//    "save cash positions" in {
//      transaction {
//        val accountRepository = applicationContext.getBean(classOf[AccountRepository])
//        val account = accountRepository.findById(1L)
//        val depositP = DepositPosition(account.id, Currencies.CNY, LocalDate.parse("2014-03-03"), 35.54)
//        val positionRepository = applicationContext.getBean(classOf[PositionRepository])
//        positionRepository.save(depositP)
//      }
//    }

    "load positions" in {
      transaction {
        val accountRepository = applicationContext.getBean(classOf[AccountRepository])
        val account = accountRepository.findById(1L)
        val positionRepository = applicationContext.getBean(classOf[PositionRepository])
        val positions = positionRepository.load(account, AssetClassConstants.ASSET, LocalDate.parse("2014-03-03"))
        println(positions)
      }
    }
  }
}
