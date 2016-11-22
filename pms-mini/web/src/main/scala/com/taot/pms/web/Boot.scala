package com.taot.pms.web

import akka.actor.{Props, ActorSystem}
import spray.can.Http
import akka.io.IO


object Boot {

  private implicit val system = ActorSystem("pms-web-actor-system")

  private val service = system.actorOf(Props[PmsHttpServiceActor], "pms-web-service")

  def main(args: Array[String]): Unit = {
    IO(Http) ! Http.Bind(service, interface = "0.0.0.0", port = 8080)
  }
}
