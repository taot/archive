package com.taot.pms.common.spring

import org.springframework.scala.context.function.FunctionalConfiguration

class ChildConfiguration(parent: ParentConfiguration) extends FunctionalConfiguration {

  val childService = singleton() {
    new ChildService("Child in child configuration", parent.parentService)
  }
}
