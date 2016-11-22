package com.taot.pms.web

import akka.actor.Actor
import spray.routing.{RejectionHandler, MissingCookieRejection}
import spray.http.StatusCodes._

class PmsHttpServiceActor extends Actor with PmsHttpService {

  implicit val myRejectionHandler = RejectionHandler {
    case MissingCookieRejection(cookieName) :: _ =>
      complete(BadRequest, "No cookies, no service!!!")
  }

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}