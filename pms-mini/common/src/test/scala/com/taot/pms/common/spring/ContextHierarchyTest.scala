package com.taot.pms.common.spring

import org.scalatest.{ShouldMatchers, WordSpec}
import org.springframework.scala.context.function.FunctionalConfigApplicationContext

class ContextHierarchyTest extends WordSpec with ShouldMatchers {

  "spring-scala" should {

    "not close parent context when closing child context" in {
      val parentConfiguration = new ParentConfiguration
      val parentCtx = FunctionalConfigApplicationContext()
      parentCtx.registerConfigurations(parentConfiguration)

      val childConfiguration = new ChildConfiguration(parentConfiguration)
      val childCtx = FunctionalConfigApplicationContext()
      childCtx.registerConfigurations(childConfiguration)
      childCtx.setParent(parentCtx)

      val parentService = childCtx.getBean(classOf[ParentService])
      parentService.run()
      val childService = childCtx.getBean(classOf[ChildService])
      childService.run()
      childCtx.close()

      parentService.destroyed should be (false)
      childService.destroyed should be (true)
    }
  }
}
