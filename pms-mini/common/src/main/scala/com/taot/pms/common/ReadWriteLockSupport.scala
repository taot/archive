package com.taot.pms.common

import java.util.concurrent.locks.ReentrantReadWriteLock

trait ReadWriteLockSupport {

  private val lock = new ReentrantReadWriteLock()

  protected def lockRead[R](op: => R): R = {
    lock.readLock().lock()
    try {
      op
    } finally {
      lock.readLock().unlock()
    }
  }

  protected def lockWrite[R](op: => R): R = {
    lock.writeLock().lock()
    try {
      op
    } finally {
      lock.writeLock().unlock()
    }
  }
}
