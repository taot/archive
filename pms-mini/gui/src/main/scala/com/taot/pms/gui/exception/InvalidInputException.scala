package com.taot.pms.gui.exception

class InvalidInputException(name: String, badValue: String, cause: Throwable) extends InputException(name, cause: Throwable) {

  def this(name: String, badValue: String) = this(name, badValue, null)

  override def getMessage(): String = s"Invalid input value for ${name}: ${badValue}"
}
