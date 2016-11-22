package com.taot.pms.gui.exception

class MissingInputException(name: String, cause: Throwable) extends InputException(name, cause) {

  def this(name: String) = this(name, null)

  override def getMessage(): String = s"Missing input: ${name}"
}