package com.taot.pms.domain.transactionclass

trait TransactionClassCache {

  def getById(id: Long): TransactionClass

  def getByLongCode(code: String): TransactionClass

  def getAll(): List[TransactionClass]
}
