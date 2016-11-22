package test

import util.Logging
import util.ListUtil

import org.specs2.mutable._


class ListUtilSpec extends Specification {

  "ListUtil" should {

    "split normal string list" in {
      val list = List(
        "line1",
        "line2",
        " ",
        "line3",
        "line4",
        "line5",
        "",
        "line6",
        "line7"
      )
      val lineGroups = ListUtil.split(list)(_.trim.isEmpty)
      println("lineGroups: " + lineGroups)
      lineGroups must have size(3)
    }

    "split more complex string list" in {
      val list = List(
        "   ",
        "line1",
        "line2",
        " ",
        "",
        "line3",
        " ",
        "line4",
        "line5",
        "",
        "line6",
        "line7",
        "  ",
        "  "
      )
      val lineGroups = ListUtil.split(list)(_.trim.isEmpty)
      println("lineGroups: " + lineGroups)
      lineGroups must have size(4)
    }
  }

  "split empty list" in {
    val list = List[String]()
    val lineGroups = ListUtil.split(list)(_.trim.isEmpty)
    println("lineGroups: " + lineGroups)
    lineGroups must have size(0)
  }

  "report error on null" in {
    val list: List[String] = null
    try {
      ListUtil.split(list)(_.trim.isEmpty)
      assert(false)
    } catch {
      case ex: NullPointerException => assert(true)
      case _ => assert(false)
    }
  }
}
