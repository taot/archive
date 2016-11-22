package com.taot.pms.rpc.server

import java.net.InetSocketAddress

import com.taot.pms.domain.model.account.{Account => MAccount, AccountRepository}
import com.taot.pms.persist.dsl.transaction
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import com.twitter.util.Future
import org.apache.thrift.protocol.TBinaryProtocol
import org.springframework.beans.factory.annotation.Autowired
import com.taot.pms.rpc.thrift.PMSService.FutureIface
import com.taot.pms.rpc.thrift.{TAccount, PMSService, TAccountCreate}
import com.taot.pms.rpc.TypeConversions._
import com.taot.pms.common.BigDecimalConstants
import com.taot.pms.common.logging.Logging

class ThriftServerFactory {

  @Autowired
  protected var accountRepository: AccountRepository = _

  def create(): Server = {

    val processor = new FutureIface with Logging {

      override def createAccount(request: TAccountCreate): Future[Long] = Future {
        logger.info("Request create account: {}", request)
        transaction {
          val account = request.account
          val a = MAccount(account.name, account.openDate)
          a.create()
          a.initialize(request.initialCash, BigDecimalConstants.ZERO)
          a.id
        }
      }

      override def loadAllAccounts(): Future[Seq[TAccount]] = Future {
        logger.info("Request load all accounts")
        transaction {
          val maccounts = accountRepository.findAll()
          maccounts.map { a => TAccount(a.id, a.name, a.openDate) }
        }
      }
    }

    val service = new PMSService.FinagledService(processor, new TBinaryProtocol.Factory())

    val server = ServerBuilder().name("PMSService").bindTo(new InetSocketAddress(8080)).codec(ThriftServerFramedCodec()).build(service)

    server
  }
}
