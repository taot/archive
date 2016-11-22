package com.taot.pms.common.spring

import org.springframework.beans.factory.DisposableBean
import com.taot.pms.common.logging.Logging

class ChildService(name: String, parentService: ParentService) extends DisposableBean with Logging {

  var destroyed = false

  def run(): Unit = {
    logger.info("Running ChildService {} and invoking parent service", name)
    parentService.run()
  }

  override def destroy(): Unit = {
    logger.info("Destroying ChildService {}", name)
    destroyed = true
  }
}
