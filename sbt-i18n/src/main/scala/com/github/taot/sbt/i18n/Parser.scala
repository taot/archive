package com.github.taot.sbt.i18n

import sbt.File
import com.github.taot.sbt.i18n.model._
import scala.io.Source
import com.github.taot.sbt.i18n.model.MessageFunc

class Parser(file: File) {

  /*
  def parse(): Seq[KeyTree] = {
    val lines = Source.fromFile(file).getLines().filter { line => !line.trim.isEmpty && !line.trim.startsWith("#") }
//    lines.flatMap { line =>
//      val (sKey, sValue) = line.split("=").map(_.trim)
//      val keyComponents = sKey.split("\\.")
//    }
    ???
  }

  private def parseLine(line: String): Option[(Seq[KeyNodeValue], MessageFunc)] = {
    val idx = line.indexOf("=")
    if (idx <= 0) {
      None
    } else {
      val leftSide = line.substring(0, idx).trim
      val rightSide = line.substring(idx + 1).trim
      val keys = leftSide.split("\\.").map { s =>
        parseKeyNodeValue(s)
      }
      val messageFunc = parseMessageFunc(rightSide)
      Some(keys, messageFunc)
    }
  }

  private def parseMessageFunc(s: String): MessageFunc = {
    ???
  }

  private def parseKeyNodeValue(s: String): KeyNodeValue = {
    if (s.startsWith("{") && s.endsWith("}")) {
      val inner = s.substring(1, s.length - 1).trim
      if (inner.isEmpty) {
        throw new RuntimeException(s"Invalid key component: ${s}. Key argument is empty")
      }
      val parts = inner.split(":").map(_.trim)
      if (parts.length != 2) {
        throw new RuntimeException(s"Invalid key component: ${s}. Should be in form of {value: Type}")
      }
      val value = parts(0)
      val tpe = parts(1)
      if (value.exists(_.isWhitespace)) {
        throw new RuntimeException(s"Invalid key component: ${s}. No white space allowed in value")
      }
      if (tpe.exists(_.isWhitespace)) {
        throw new RuntimeException(s"Invalid key component: ${s}. No white space allowed in type")
      }
      Right(KeyNodeArg(value, tpe))
    } else {
      if (s.exists(_.isWhitespace)) {
        throw new RuntimeException(s"Invalid key component: ${s}. No white space allowed")
      }
      Left(s)
    }
  }
  */
}
