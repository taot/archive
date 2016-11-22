package com.taot.pms.json

import org.joda.time.LocalDate
import org.scalatest.{ShouldMatchers, WordSpec}
import spray.http.{ContentType, HttpEntity}
import spray.http.HttpCharsets._
import spray.http.MediaTypes._
import spray.httpx.marshalling._
import spray.httpx.unmarshalling._
import spray.json._

class SprayJsonTest extends WordSpec with ShouldMatchers {

  case class Person(firstName: String, lastName: String, bod: Option[LocalDate])

  object MyJsonProtocol extends DefaultJsonProtocol {

    implicit object LocalDateFromat extends JsonFormat[LocalDate] {

      def write(obj: LocalDate): JsValue = JsString(obj.toString())

      def read(json: JsValue): LocalDate = json match {
        case JsString(s) => LocalDate.parse(s)
        case _ => throw new RuntimeException("LocalDate expected")
      }
    }

    implicit val PersonFormat = jsonFormat3(Person)
  }

  import MyJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  val p = Person("Terry", "Tao", Some(LocalDate.parse("1983-02-03")))

  val body = HttpEntity(
    contentType = ContentType(`application/json`, `UTF-8`),
    string =
      """{
        |  "firstName": "Terry",
        |  "lastName": "Tao",
        |  "bod": "1983-02-03"
        |}""".stripMargin
  )

  "spray-json" should {

    "print json" in {
      marshal(p) should equal (Right(body))
    }

    "parse json" in {
      body.as[Person] should equal (Right(p))
    }
  }
}
