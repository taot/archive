package com.taot.pms.persist

import com.taot.pms.common.config.Configurator
import com.taot.pms.common.logging.Logging
import scala.slick.jdbc.JdbcBackend._

class PersistInitializer(configurator: Configurator) extends Logging {

  val MYSQL_DRIVER = "com.mysql.jdbc.Driver"

  private val PREFIX = "server.accountmaster."

  private val CHARACTER_ENCODING = "characterEncoding"

  private val config = configurator.getConfig()

  private val url = getDbUrl()

  private val user = config.getString(PREFIX + "user")

  private val password = config.getString(PREFIX + "password")


  private def getDbUrl(): String = {
    val s = config.getString(PREFIX + "url")
    if (s.contains(CHARACTER_ENCODING)) {
      s
    } else {
      var sb = new StringBuilder(s)
      if (sb.contains("?")) {
        sb.append("&")
      } else {
        sb.append("?")
      }
      sb.append(CHARACTER_ENCODING + "=UTF-8")
      sb.toString()
    }
  }

  // Initialize database
  dsl.db = Database.forURL(url = url, driver = MYSQL_DRIVER, user = user, password = password)
  logger.info("Database initialized: url = {}, user = {}, password = {}", url, user, password)
}
