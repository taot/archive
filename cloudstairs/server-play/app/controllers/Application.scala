package controllers

import data._
import data.DataWrites._

import play.api._
import play.api.mvc._
import play.api.libs.json._


object Application extends Controller {

  def index = Action {

    /*val req = new CSReq(method = "GET", host = "localhost", remoteAddr = "127.0.0.1",
      requestUri = "/", header = Map("p" -> List("1","2")))
    val json = Json.toJson(req)*/

    val session = new CSSession
    val json = Json.toJson(session)

    println(Json.prettyPrint(json))


    Ok(json)
  }

}
