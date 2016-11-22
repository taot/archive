package com.taot.pms.domain.model.account

import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

import com.taot.pms.persist.{Account => DbAccount, DAL}
import com.taot.pms.common.RichDomainObjectFactory

class AccountRepositoryImpl(dal: DAL) extends AccountRepository {

  import dal.profile.simple._

  private val insertInvoker = (dal.accounts returning dal.accounts.map(_.id)).insertInvoker

  override def create(account: Account): Long = {
    RichDomainObjectFactory.autowire(account)
    val id = insertInvoker += convertToDbObject(account)

    id
  }

  override def update(account: Account): Unit = {
    val a = convertToDbObject(account)
    findByIdCompiled(a.id).updateInvoker.update(a)
  }

  override def delete(account: Account): Unit = {
    val a = convertToDbObject(account)
    findByIdCompiled(a.id).deleteInvoker.delete
  }

  override def findById(id: Long): Account = {
    val list = findByIdCompiled(id).run
    list.headOption match {
      case Some(account) => convertToModel(account)
      case None => null
    }
  }

  override def findAll(): Seq[Account] = {
    val list = findAllCompiled.run
    list.map(convertToModel)
  }

  private val findByIdCompiled = Compiled(
    { id: Column[Long] =>
      dal.accounts.filter(_.id === id)
    }
  )

  private val findAllCompiled = Compiled(
    {
      for (a <- dal.accounts) yield a
    }
  )

  private def convertToModel(account: DbAccount): Account = {
    val a = new Account(_id = account.id, _name = account.name, _openDate = account.openDate)
    RichDomainObjectFactory.autowire(a)
  }

  private def convertToDbObject(account: Account): DbAccount = {
    new DbAccount(name = account.name, openDate = account.openDate, id = account.id)
  }
}
