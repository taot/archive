package com.taot.pms.domain.transactionclass.impl

import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

import com.taot.pms.common.logging.Logging
import com.taot.pms.domain.BusinessException
import com.taot.pms.domain.transactionclass.TransactionClass
import com.taot.pms.persist.{DAL, TransactionClass => DbTransactionClass}
import com.taot.pms.persist.dsl.transaction

class DbTransactionClassWriter(dal: DAL) extends TransactionClassWriter with Logging {

  override def write(list: List[TransactionClass]): Unit = transaction {
    import dal.profile.simple._
    val dbList = (for (c <- dal.transactionClasses) yield c).list()
    for (tc <- list) {
      dbList.find(_.code == tc.code) match {
        case Some(c) =>
          if (c.id != tc.id) {
            throw new BusinessException(s"Ids do not match for transaction class ${tc.code}: DB id = ${c.id}, XML id = ${tc.id}")
          }
        case None =>
          dal.transactionClasses += DbTransactionClass(tc.id, tc.code, "", tc.assetClass.id)
          logger.info("Add transaction class {}", tc)
      }
    }
  }
}
