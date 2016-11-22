package com.taot.pms.domain.model.asset.equity

import com.taot.pms.domain.BusinessException
import com.taot.pms.marketdata.model.Equity
import org.scalatest._
import org.joda.time.LocalDate

class EquityPositionTest extends WordSpec with ShouldMatchers {

  val equity = Equity(1, "000064")

  val asOfDate = LocalDate.parse("2013-12-30")

  "EquityPosition" should {

    "buy" in {
      val position = new EquityPosition(1, 1L, equity, asOfDate, 13, 100)
      position.buy(3, 30)
      position.quantity should equal (16)
      position.carryingValue should equal (130)
    }

    "sell" in {
      val position = new EquityPosition(1, 1L, equity, asOfDate, 12, 120)
      position.sell(3, 50)
      position.quantity should equal (9)
      position.carryingValue should equal (90)
    }

    "not sell if no enough holding" in {
      val position = new EquityPosition(1, 1L, equity, asOfDate, 12, 120)
      evaluating {
        position.sell(13, 100)
      } should produce [BusinessException]
    }
  }
}
