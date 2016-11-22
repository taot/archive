package com.taot.pms.common.config

trait ConfigChangeListener {

  def onConfigChange(e: ConfigChangeEvent)
}
