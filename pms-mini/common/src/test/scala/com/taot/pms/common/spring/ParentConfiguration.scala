package com.taot.pms.common.spring

import org.springframework.scala.context.function.FunctionalConfiguration

class ParentConfiguration extends FunctionalConfiguration {

  val parentService = singleton() {
    new ParentService
  }

  val childService = singleton() {
    new ChildService("Parent", parentService)
  }
}
