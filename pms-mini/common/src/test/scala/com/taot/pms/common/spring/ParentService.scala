package com.taot.pms.common.spring

import org.springframework.beans.factory.DisposableBean
import com.taot.pms.common.logging.Logging

class ParentService extends DisposableBean with Logging {

  var destroyed = false

  def run(): Unit = {
    logger.info("Running ParentService")
  }

  override def destroy(): Unit = {
    logger.info("Destroying ParentService")
    destroyed = true
  }
}
