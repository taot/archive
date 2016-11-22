package com.taot.pms.gui.exception

class InputException(name: String, cause: Throwable) extends RuntimeException(cause) {

  override def getMessage(): String = s"Bad input: ${name}"
}
