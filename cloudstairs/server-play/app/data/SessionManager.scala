package data

import util.Logging

import java.util.concurrent.{ Executors, ScheduledExecutorService, TimeUnit }
import scala.collection.concurrent.TrieMap

object SessionManager extends Logging {

  private val sessionMap = TrieMap.empty[String, CSSession]

  createCleaner()

  def add(session: CSSession): Unit = sessionMap += (session.uuid -> session)

  def get(uuid: String): Option[CSSession] = {
    sessionMap.get(uuid) match {
      case Some(s) =>
        if (s.isExpired) {
          sessionMap -= uuid
          None
        } else {
          Some(s)
        }
      case None =>
        None
    }
  }

  private def createCleaner(): Unit = {
    val cleaner = new Runnable() {
      def run(): Unit = removeExpired
    }
    val scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleWithFixedDelay(cleaner, 0, 5 * 60, TimeUnit.SECONDS)  // TODO config
  }

  private def removeExpired(): Unit = {
    logger.info("Cleaning expired sessions...")
    var count = 0
    val expired = sessionMap filter { case (_, s) => s.isExpired } map (_._1)
    sessionMap --= expired
  }
}
