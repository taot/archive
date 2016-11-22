package com.taot.pms.common

import org.springframework.beans.factory.{BeanFactory, BeanFactoryAware}
import org.springframework.beans.factory.config.AutowireCapableBeanFactory

object RichDomainObjectFactory extends BeanFactoryAware {

  private var factory: AutowireCapableBeanFactory = null

  def autowire[T](instance: T): T = {
    factory.autowireBeanProperties(instance, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false)
    instance
  }

  def setBeanFactory(beanFactory: BeanFactory): Unit = {
    this.factory = beanFactory.asInstanceOf[AutowireCapableBeanFactory]
  }

  def getInstance[T](clazz: Class[T]): T = this.factory.getBean(clazz)
}
