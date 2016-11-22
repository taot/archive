package com.taot.pms.domain.model.account

trait AccountRepository {

  def create(account: Account): Long

  def update(account: Account): Unit

  def delete(account: Account): Unit

  def findById(id: Long): Account

  def findAll(): Seq[Account]
}
