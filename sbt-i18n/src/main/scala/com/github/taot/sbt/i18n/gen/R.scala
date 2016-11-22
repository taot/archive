package com.github.taot.sbt.i18n.gen

import scala.collection.mutable.ListBuffer
import java.math.RoundingMode

object R {

  val error = new {
    val exception = new {
      def number_arguments_wrong(n: Int, list: ListBuffer[Int]) = {
        s"No enough elements (${n}) in argument ${list}"
      }
    }
  }

  val message = new {
    def rounding_mode(roundingMode: RoundingMode) = "Rounding Mode Ceiling"
  }
}
