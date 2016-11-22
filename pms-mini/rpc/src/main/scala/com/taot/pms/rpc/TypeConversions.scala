package com.taot.pms.rpc

import org.joda.time.LocalDate
import com.taot.pms.rpc.thrift.TLocalDate

object TypeConversions {

  implicit def localDateToThrift(date: LocalDate): TLocalDate = {
    TLocalDate(date.getYear, date.getMonthOfYear, date.getDayOfMonth)
  }

  implicit def localDateFromThrift(date: TLocalDate): LocalDate = {
    new LocalDate(date.year, date.month, date.day)
  }
}
