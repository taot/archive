//package com.typesafe.config.impl
package com.github.taot.sbt.config

import java.io.File

import scala.collection.JavaConversions._

import com.typesafe.config.ConfigFactory

//import com.typesafe.config.impl.SimpleConfigList

object TestMain {

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.parseFile(new File("/home/taot/tmp/reference.conf"))
    val root = config.root()

    for (k <- root.keys) {
      val v = root.get(k)
      println(k + " " + v.valueType())
      import com.typesafe.config.ConfigValueType._
      v.valueType() match {
        case NUMBER =>
          println(v)
          println(v.unwrapped().getClass)
        case _ =>
          println(v)
      }
    }

//    val configImpl = config.asInstanceOf[SimpleConfig]
//    configImpl.

//    println(config)
//    val entrySet = config.entrySet()
//    for (e <- entrySet) {
//      val (k, v) = (e.getKey, e.getValue)
//      println("key: " + k);
//      println("value: " + v)
//
//      v match {
//        case list: SimpleConfigList =>
//          for (i <- 0 until list.size()) {
//            println(list.get(i))
//          }
//        case _ =>
//      }
//    }
  }
}
