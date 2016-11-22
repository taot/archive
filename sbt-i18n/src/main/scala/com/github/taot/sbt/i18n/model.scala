package com.github.taot.sbt.i18n

package object model {

  type ArgType = String

  sealed abstract class Arg {
    val tpe: ArgType
  }
  case class MessageFuncArg(nameOpt: Option[String], tpe: ArgType) extends Arg
  case class KeyNodeArg(value: String, tpe: ArgType) extends Arg

  type MessageComponent = Either[String, MessageFuncArg]

  case class MessageFunc(messageComponents: Seq[MessageComponent])

  type KeyNodeValue = Either[String, KeyNodeArg]

  sealed abstract class KeyTree {
    val value: KeyNodeValue
  }
  case class KeyNode(value: KeyNodeValue, children: Seq[KeyTree]) extends KeyTree
  case class KeyLeaf(value: KeyNodeValue, messageFunc: MessageFunc) extends KeyTree

  case class Import(value: String)
}
