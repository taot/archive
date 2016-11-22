package com.github.taot.sbt.config

import java.io.{PrintStream, PrintWriter}

import scala.collection.JavaConversions._

import com.typesafe.config.{ConfigList, ConfigObject, ConfigValue, Config}

class CodeGenerator(config: Config, pkg: String, writer: PrintStream) {

  val INDENT = "  "

  def generate(): Unit = {
    writer.println(s"package ${pkg}")
    writer.println("import com.github.taot.config.Setting")
    writer.println("object S {")

    writeObject(1, Nil, config.root())
    writer.println("}")
  }

  private def writeObject(level: Int, keys: List[String], obj: ConfigObject): Unit = {
    for (k <- obj.keySet().toList.sortBy(s => s)) {
      indent(level)
      writer.print(s"val ${k} = ")
      obj.get(k) match {
        case o: ConfigObject =>
          writer.println("new {")
          writeObject(level + 1, k :: keys, o)
          indent(level)
          writer.print("}")
        case _ @ v => writeValue(level + 1, k :: keys, v)
      }
      writer.println
    }
  }

  private def writeValue(level: Int, keys: List[String], value: ConfigValue): Unit = {
    val path = keys.reverse.mkString(".")

    import com.typesafe.config.ConfigValueType._
    value.valueType() match {
      case LIST =>
        val t = getListType(value, path)
        writer.print(setting(t, path))

      case NUMBER =>
        value.unwrapped() match {
          case i: java.lang.Integer =>
            writer.print(setting("Int", path))
          case l: java.lang.Long =>
            writer.print(setting("Long", path))
          case d: java.lang.Double =>
            writer.print(setting("Double", path))
          case _ @ x =>
            throw new RuntimeException("Unsupported number type: " + x)
        }

      case BOOLEAN =>
        writer.print(setting("Boolean", path))

      case NULL =>
        writer.print(setting("Null", path))

      case STRING =>
        val s = value.unwrapped().asInstanceOf[String]
        val p = "<([^<>]*)>".r
        try {
          val p(t) = s
          writer.print(setting(t, path))
        } catch {
          case e: Throwable =>
            writer.print(setting("String", path))
        }

      case OBJECT =>
        throw new RuntimeException("Coding error. Object type should be handled by writeObject() already.")
    }
  }

  private def setting(tpe: String, path: String): String = {
    "new Setting[" + tpe + "](\"" + path + "\")"
  }

  private def getListType(value: ConfigValue, path: String): String = {
    val list = value.asInstanceOf[ConfigList]
    if (list.isEmpty) {
      throw new RuntimeException("Must write at least one element in the list: " + path)
    }
    val v = list.get(0)

    import com.typesafe.config.ConfigValueType._
    val elemType = v.valueType() match {
      case NUMBER =>
        v.unwrapped() match {
          case i: java.lang.Integer => "Int"
          case l: java.lang.Long => "Long"
          case d: java.lang.Double => "Double"
          case _ @ x => throw new RuntimeException("Unsupported number type: " + x)
        }
      case BOOLEAN => "Boolean"
      case STRING => "String"
      case _ @ t => throw new RuntimeException(s"Unsupported list element type ${t}: ${path}")
    }
    s"List[${elemType}]"
  }

  private def indent(level: Int): Unit = {
    for (i <- 0 until level) {
      writer.print(INDENT)
    }
  }
}
