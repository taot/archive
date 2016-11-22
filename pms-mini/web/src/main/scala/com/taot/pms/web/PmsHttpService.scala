package com.taot.pms.web

import spray.http.MediaTypes._
import spray.routing.HttpService
import akka.actor.Status.Failure
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import scala.concurrent.Future
import spray.httpx.marshalling.MetaMarshallers

//import akka.actor.Status.{Failure, Success}
import spray.http.StatusCodes._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.dispatch.Futures


trait PmsHttpService extends HttpService {

  case class Person(name: String, favoriteNumber: Int)

  import MetaMarshallers._

  object PersonJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
    implicit val PortofolioFormats = jsonFormat2(Person)
  }

  import PersonJsonSupport._

  def divide(): Future[String] = Future {
    println("Thread is " + Thread.currentThread().getId)
    println("Sleeping " + (System.currentTimeMillis() / 1000))
    Thread.sleep(10 * 1000)
    "Hello"
  }

  def findPerson(f: Person => Unit): Unit = {
    println("Sleeping " + Thread.currentThread().getId + ", " + (System.currentTimeMillis() / 1000))
    Thread.sleep(10 * 1000)
    val p = Person("Terry", 7)
    f(p)
  }

  def createFuture(): Future[String] = Future {
    println("Sleeping " + Thread.currentThread().getId + ", " + (System.currentTimeMillis() / 1000))
    Thread.sleep(10 * 1000)
    "Hello"
  }

  val myRoute = path("") {
    get {
//      complete(createFuture())
      dynamic {
//        onComplete(createFuture()) { s => complete(s) }
        complete(createFuture())
      }
    }
//    get {
//      produce(instanceOf[Person]) {
//        completionFunction => ctx => findPerson(completionFunction)
//      }
//    }
//    get {
//      onSuccess(divide()) { s =>
//        complete(s)
//      }
//      divide().onComplete {
//        case Success(s) => complete(s)
//        case Failure(th) => complete(th.getMessage)
//      }
//    }
  }
//    get {
//      detach() {
//        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
//detach() {
//          onSuccess(divide()) { s =>
//            complete(s)
//          }
//}
////          complete {
////            println("sleeping " + (System.currentTimeMillis() / 1000))
////            Thread.sleep(10 * 1000)
////            <html>
////              <body>
////                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
////              </body>
////            </html>
////          }
//        }
//      }
//    }
//  }
}
