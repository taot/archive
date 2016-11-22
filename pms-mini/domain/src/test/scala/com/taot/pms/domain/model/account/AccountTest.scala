package com.taot.pms.domain.model.account

import com.taot.pms.ApplicationContextSuiteBase
import com.taot.pms.common.logging.Logging
import com.taot.pms.persist.dsl.transaction
import org.scalatest.{ShouldMatchers, WordSpec}
import org.joda.time.LocalDate

class AccountTest extends WordSpec with ShouldMatchers with ApplicationContextSuiteBase with Logging {

  val OPEN_DATE = new LocalDate(2014, 1, 2)

  protected var accountRepository: AccountRepository = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    accountRepository = applicationContext.getBean(classOf[AccountRepository])

    // clean up existing accounts
    try {
      transaction {
        val accounts = accountRepository.findAll()
        accounts.map(_.delete)
      }
    } finally {
      super.afterAll()
    }
  }

  "Account" should {

    "create account" in {
      val accountId = transaction {
        val account = Account(name = "TestAccount", openDate = new LocalDate(2014, 1, 2))
        account.create()
        account.id
      }
      transaction {
        val account = accountRepository.findById(accountId)
        account.name should equal ("TestAccount")
        account.openDate should equal (new LocalDate(2014, 1, 2))
      }
    }

    "update account" in {
      val id = createAccount("TestAccount-update")
      transaction {
        val account = accountRepository.findById(id)
        account.name = "TestAccount-updated-yeah"
        account.update()
      }
      transaction {
        val account = accountRepository.findById(id)
        account.name should equal ("TestAccount-updated-yeah")
      }
    }

    "delete account" in {
      val id = createAccount("TestAccount-delete")
      transaction {
        val account = accountRepository.findById(id)
        account.delete()
      }
      transaction {
        accountRepository.findById(id) should be (null)
      }
    }
  }

  private def createAccount(name: String): Long = transaction {
    val account = Account(name = name, openDate = OPEN_DATE)
    account.create()
    account.id
  }
}
