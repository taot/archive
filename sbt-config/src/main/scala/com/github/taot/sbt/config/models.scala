package com.github.taot.sbt.config

abstract class CValueType
case class CIntType(v: Int)
case class CStringType(v: String)


case class CSetting(keys: Seq[String], value: CValueType)