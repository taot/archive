package com.github.taot.sbt.i18n

object TestMain extends ReversePolishCalculator {

  def main(args: Array[String]): Unit = {
    val result = calculate("1 2")
    println(s"Result: ${result}")
  }

  def calculate(expression: String) = parseAll(expr, expression)
}
