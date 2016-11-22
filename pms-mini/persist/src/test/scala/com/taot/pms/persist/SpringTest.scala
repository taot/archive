package com.taot.pms.persist

import org.scalatest.{MustMatchers, WordSpec}
import com.taot.pms.common.logging.Logging
import scala.beans.BeanProperty
import org.springframework.scala.context.function.{FunctionalConfigApplicationContext, FunctionalConfiguration}
import org.springframework.beans.factory.DisposableBean

class SpringTest extends WordSpec with MustMatchers with Logging {
/*
  "Spring" should {
    "inject object" in {
      val applicationContext = FunctionalConfigApplicationContext(classOf[PersonConfiguration])
      val john = applicationContext.getBean(classOf[Person])
      println(john.firstName)
      applicationContext.close()
      println("context is active? " + applicationContext.isActive)
    }
  }
*/
}

class Person(@BeanProperty var firstName: String, @BeanProperty var lastName: String) extends DisposableBean with Logging {
  def init(): Unit = {
    logger.info("Initializing Person bean")
  }

  override def destroy(): Unit = {
    logger.info("Destroying Person bean")
  }
}

class PersonConfiguration extends FunctionalConfiguration {

  singleton() {
    val p = new Person("John", "Doe")
    p.init()
    p
  }
}