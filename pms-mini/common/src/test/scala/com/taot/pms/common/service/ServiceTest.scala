package com.taot.pms.common.service

import org.scalatest.{ShouldMatchers, MustMatchers, WordSpec}
import com.taot.pms.common.logging.Logging
import org.springframework.scala.context.function.FunctionalConfigApplicationContext


class ServiceTest extends WordSpec with ShouldMatchers with Logging {

  "ServiceRef" should {
    "proxy invocation to actual service" in {
      val ctx = FunctionalConfigApplicationContext(classOf[TestServiceConfiguration])
      val mds = ctx.getBean(classOf[MarketDataService])
      mds.getData("GOOG")

      val t1 = new Thread {
        override def run(): Unit = {
          val n = 1000
          logger.info("Thread 1 sleeping " + n + " ms before refresh marketdata")
          Thread.sleep(n)
          mds.refresh()
        }
      }

      var duration: Long = 0L

      val t2 = new Thread {
        override def run(): Unit = {
          val n = 1500
          logger.info("Thread 2 sleeping " + n + " ms before getting data")
          Thread.sleep(n)
          val start = System.currentTimeMillis()
          mds.getData("GOOG")
          logger.info("Thread 2 get data finish")
          duration = System.currentTimeMillis() - start
        }
      }

      t1.start()
      t2.start()
      t1.join()
      t2.join()

      duration should be > (1000L)

      ctx.close()
    }
  }
}
