package com.taot.pms.domain.model.account

import org.springframework.beans.factory.annotation.Autowired
import com.taot.pms.domain.model.asset.cash.CashPositionRepository
import org.joda.time.LocalDate
import com.taot.pms.common.logging.Logging
import com.taot.pms.common.RichDomainObjectFactory
import com.taot.pms.domain.model.transaction.Transaction
import com.taot.pms.domain.model.asset.{Position, PositionRepository}
import com.taot.pms.domain.assetclass.AssetClassConstants

class Account(_id: Long, _name: String, _openDate: LocalDate) extends Logging {

  @Autowired
  protected var accountRepository: AccountRepository = _

  @Autowired
  protected var cashPositionRepository: CashPositionRepository = _

  @Autowired
  protected var positionRepository: PositionRepository = _

  private var id_ : Long = _id

  private var name_ : String = _name

  private var openDate_ : LocalDate = _openDate

  def id = id_

  def name = name_

  def name_=(s: String) = name_ = s

  def openDate = openDate_

  def create(): Unit = {
    this.id_ = accountRepository.create(this)
    logger.info("Account created: {}", this)
  }

  def update(): Unit = {
    accountRepository.update(this)
    logger.info("Account {} updated: {}", this.id, this)
  }

  def delete(): Unit = {
    accountRepository.delete(this)
    logger.info("Account {} deleted", this.id)
  }

  def initialize(initialCash: BigDecimal, initialMargin: BigDecimal): Unit = {
    // Deposit
    val depositP = cashPositionRepository.findDepositPosition(this, openDate_)
    depositP.change(initialCash)
    cashPositionRepository.update(depositP)

    // Margin and others coming later
  }

  def processTransaction(tran: Transaction): Unit = {
    ???
  }

  def renewPositionsDaily(asOfDate: LocalDate): Unit = {
    val yesterday = asOfDate.minusDays(1)
    val positions = positionRepository.load[Position](this, AssetClassConstants.ASSET, yesterday)
    for (p <- positions) {
      val np = p.renewDaily(asOfDate)
      positionRepository.save(np)
    }
  }

//  def processCashSettlement(asOfDate: LocalDate): Unit = {
//    val payable = cashPositionRepository.findPayablePosition(this, asOfDate)
//    val receivable = cashPositionRepository.findReceivablePosition(this, asOfDate)
//    val deposit = cashPositionRepository.findDepositPosition(this, asOfDate)
//
//    deposit.change(receivable.quantity + payable.quantity)
//    payable.reset()
//    receivable.reset()
//
//    cashPositionRepository.update(deposit)
//    cashPositionRepository.update(payable)
//    cashPositionRepository.update(receivable)
//
//    logger.info("Cash settlement processed on account {}", this.id)
//  }

  override def toString(): String = {
    s"Account [id = ${id_}, name = ${name_}]"
  }
}

object Account {

  def apply(name: String, openDate: LocalDate): Account = {
    val a = new Account(-1, name, openDate)
    RichDomainObjectFactory.autowire(a)
  }
}
