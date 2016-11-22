package com.taot.pms.domain

class BusinessException(msg: String, cause: Throwable) extends RuntimeException(msg, cause) {

  def this(msg: String) = this(msg, null)
}
