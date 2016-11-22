package com.taot.pms.common.util

import org.scalatest.WordSpec
import org.scalatest.matchers.ShouldMatchers

class DateTimeUtilTest extends WordSpec with ShouldMatchers {

  "DateTimeUtil" should {

    "parse yyyy-MM-dd" in {
      val date = DateTimeUtil.parseLocalDate("2014-03-01")
      date.getYear should be (2014)
      date.getMonthOfYear should be (3)
      date.getDayOfMonth() should be (1)
    }

    "parse yyyy/MM/dd" in {
      val date = DateTimeUtil.parseLocalDate("2014/03/01")
      date.getYear should be (2014)
      date.getMonthOfYear should be (3)
      date.getDayOfMonth() should be (1)
    }

    "parse yyyyMMdd" in {
      val date = DateTimeUtil.parseLocalDate("20140301")
      date.getYear should be (2014)
      date.getMonthOfYear should be (3)
      date.getDayOfMonth() should be (1)
    }

    "throw exception on bad formats" in {
      evaluating { DateTimeUtil.parseLocalDate("2014030a") } should produce [IllegalArgumentException]
      evaluating { DateTimeUtil.parseLocalDate("201403") } should produce [IllegalArgumentException]
      evaluating { DateTimeUtil.parseLocalDate("2014\\03\\01") } should produce [IllegalArgumentException]
    }
  }
}
