package com.taot.scala.logging

trait Logging {

  protected lazy val logger = new Logger(this.getClass)
}
