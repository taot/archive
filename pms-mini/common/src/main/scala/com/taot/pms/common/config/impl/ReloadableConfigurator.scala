package com.taot.pms.common.config.impl

import com.typesafe.config.{ConfigFactory, Config}
import com.taot.pms.common.ReadWriteLockSupport
import com.taot.pms.common.logging.Logging
import org.springframework.beans.factory.DisposableBean
import scala.collection.mutable
import com.taot.pms.common.config.{ConfigChangeEvent, ConfigChange, ConfigChangeListener, Configurator}


class ReloadableConfigurator(configName: String = "", reload: Boolean = true) extends Configurator with ReadWriteLockSupport with DisposableBean with Logging {

  private var underlying: Config = loadUnderlyingConfig()

  private var listeners = List.empty[(ConfigChangeListener, Seq[String])]

  private val DEFAULT_RELOAD_INTERVAL = 10

  private val RELOAD_INTERVAL = {
    val intervalInSeconds = System.getProperty("config.reload.interval", DEFAULT_RELOAD_INTERVAL.toString)
    try {
      java.lang.Integer.parseInt(intervalInSeconds) * 1000
    } catch {
      case e: Throwable =>
        logger.error("Error parsing property config.reload.interval: {}", e.getMessage, e)
      DEFAULT_RELOAD_INTERVAL
    }
  }

  private val reloadThread = if (reload) {
    val t = new ReloadThread()
    t.start()
    t
  } else {
    null
  }

  logger.info("Reloadable config {} initialized", configName)

  private def loadUnderlyingConfig(): Config = if (configName == null || configName.trim.isEmpty) {
    ConfigFactory.load()
  } else {
    ConfigFactory.load(configName)
  }

  override def getConfig(): Config = lockRead { underlying }

  override def addListener(listener: ConfigChangeListener, paths: Seq[String]): Unit = lockWrite {
    val list = listeners.filter(_._1 ne listener)
    listeners = (listener, paths) :: list
  }

  override def removeListener(listener: ConfigChangeListener): Unit = lockWrite {
    listeners = listeners.filter(_._1 ne listener)
  }

  override def destroy(): Unit = {
    if (reloadThread != null) {
      reloadThread.setStop()
    }
  }

  private class ReloadThread extends Thread("config-reloading-thread") {

    private var stopFlag = false

    setDaemon(true)

    override def run(): Unit = {
      logger.info("Thread {} started", getName)
      Thread.sleep(RELOAD_INTERVAL)
      while (! stopFlag) {
        run0()
        Thread.sleep(RELOAD_INTERVAL)
      }
      logger.info("Thread {} stopped", getName)
    }

    def setStop(): Unit = stopFlag = true

    private def run0(): Unit = {
      logger.trace("Reloading config {}", configName)
      ConfigFactory.invalidateCaches()
      val oldConfig = lockWrite {
        val c = underlying
        underlying = loadUnderlyingConfig()
        c
      }
      lockRead {
        for ((listener, paths) <- listeners) {
          val changed = mutable.ListBuffer.empty[ConfigChange]
          for (p <- paths) {
            val oldValue = oldConfig.getValue(p)
            val newValue = underlying.getValue(p)
            if (newValue != oldValue) {
              changed += ConfigChange(p, oldValue, newValue)
            }
          }
          if (changed.nonEmpty) {
            logger.info("Firing config change to {}: {}", listener, changed)
            val e = ConfigChangeEvent(underlying, changed.toList)
            listener.onConfigChange(e)
          }
        }
      }
    }
  }

}
