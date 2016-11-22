package util

import org.slf4j.{ Logger, LoggerFactory }


trait Logging {

  protected val logger = LoggerFactory.getLogger(this.getClass)
}
