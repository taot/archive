package com.taot.pms.common.logging

trait Logging {

  protected val logger = new Logger(this.getClass)
}
