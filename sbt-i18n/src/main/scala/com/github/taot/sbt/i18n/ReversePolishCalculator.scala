package com.github.taot.sbt.i18n

import scala.util.parsing.combinator._

trait Maths {
  def add(x: Float, y: Float) = x + y
  def sub(x: Float, y: Float) = x - y
  def mul(x: Float, y: Float) = x * y
  def div(x: Float, y: Float) = if (y > 0) (x / y) else 0.0f
}

class ReversePolishCalculator extends JavaTokenParsers with Maths {

  def num: Parser[Float] = floatingPointNumber ^^ (_.toFloat)

  def term: Parser[List[Float]] = rep(num)

  def operator: Parser[(Float, Float) => Float] = ("*" | "/" | "+" | "-") ^^ {
    case "+" => (x, y) => x + y
    case "-" => (x, y) => x - y
    case "*" => (x, y) => x * y
    case "/" => (x, y) => if (y != 0) (x / y) else 0
  }

  def expr: Parser[Float] = rep(term ~ operator) ^^ {
    // match a list of term~operator
    case terms =>
      // Each operand will be placed on the stack, and pairs will be popped off for each operation,
      // replacing the pair with the result of the operation. Calculation ends when the final operator
      // is applied to all remaining operands
      var stack  = List.empty[Float]
      // Remember the last operation performed, default to addition
      var lastOp: (Float, Float) => Float = (x, y) => x + y
      terms.foreach(t =>
      // match on the operator to perform the appropriate calculation
        t match {
          // append the operands to the stack, and reduce the pair at the top using the current operator
          case nums ~ op => lastOp = op; stack = reduce(stack ++ nums, op)
        }
      )
      // Apply the last operation to all remaining operands
      stack.reduceRight((x, y) => lastOp(y, x))
  }

  // Reduces a stack of numbers by popping the last pair off the stack, applying op, and pushing the result
  def reduce(nums: List[Float], op: (Float, Float) => Float): List[Float] = {
    // Reversing the list lets us use pattern matching to destructure the list safely
    val result = nums.reverse match {
      // Has at least two numbers at the end
      case x :: y :: xs => xs ++ List(op(y, x))
      // List of only one number
      case List(x)      => List(x)
      // Empty list
      case _            => List.empty[Float]
    }
    result
  }
}
