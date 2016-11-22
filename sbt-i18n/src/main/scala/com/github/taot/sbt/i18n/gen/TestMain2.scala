package com.github.taot.sbt.i18n.gen

import scala.collection.mutable
import java.math.RoundingMode

object TestMain2 {

  def main(args: Array[String]): Unit = {
    val buf = mutable.ListBuffer(1, 2)
    println(R.error.exception.number_arguments_wrong(1, buf))
    println(R.message.rounding_mode(RoundingMode.CEILING))
  }
}
