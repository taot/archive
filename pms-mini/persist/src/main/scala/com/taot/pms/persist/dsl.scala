package com.taot.pms.persist

import scala.slick.jdbc.JdbcBackend

object dsl {

  protected[persist] var db: JdbcBackend.DatabaseDef = null

  def transaction[R](f: => R): R = {
    db.withDynTransaction(f)
  }
}
