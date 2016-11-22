package com.taot.pms.common.util

import org.joda.time.LocalDate

object DateTimeUtil {

  /*
   * Parse joda-time LocalDate from a string.
   * Accepts the following formats:
   *     "2014-03-01"   (joda-time native support)
   *     "20140301"     (mostly used in financial software)
   *     "2014/03/01"   (Microsoft excel format, BTW, excel is stupid at formatting cells)
   */
  def parseLocalDate(sDate: String): LocalDate = {
    if (sDate == null) {
      throw new NullPointerException("argument cannot be null")
    }
    var date: LocalDate = null

    // "yyyy-MM-dd"
    if (sDate.contains("-")) {
      try {
        date = LocalDate.parse(sDate)
      } catch {
        case e: Throwable =>
      }
    }
    if (date != null) {
      return date
    }

    // "yyyy/MM/dd"
    if (sDate.contains("/")) {
      try {
        date = LocalDate.parse(sDate.replace('/', '-'))
      } catch {
        case e: Throwable =>
      }
    }
    if (date != null) {
      return date
    }

    // "yyyyMMdd"
    if (sDate.length == 8 && sDate.forall(_.isDigit)) {
      try {
        val year = sDate.substring(0, 4).toInt
        val month = sDate.substring(4, 6).toInt
        val day = sDate.substring(6, 8).toInt
        date = new LocalDate(year, month, day)
      } catch {
        case e: Throwable =>
      }
    }
    if (date != null) {
      return date
    }

    throw new IllegalArgumentException("Invalid date format: " + sDate)
  }
}
