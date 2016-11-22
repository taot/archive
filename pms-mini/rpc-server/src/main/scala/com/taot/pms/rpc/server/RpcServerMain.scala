package com.taot.pms.rpc.server

import com.taot.pms.common.logging.Logging
import org.springframework.context.ApplicationContext
import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import com.taot.pms.common.RichDomainObjectFactory

object RpcServerMain extends Logging {

  val ctx: ApplicationContext = FunctionalConfigApplicationContext(classOf[RpcServerConfiguration])

  def main(args: Array[String]): Unit = {
    val factory = new ThriftServerFactory
    RichDomainObjectFactory.autowire(factory)
    val server = factory.create()
  }
}
