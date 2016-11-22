package data

import util.AesUtil

import org.apache.commons.codec.binary.Base64
import java.util.UUID
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import play.api.libs.json._

case class CSReq(
  val method: String,
  val host: String,
  val remoteAddr: String,
  val requestUri: String,
  val header: Map[String, List[String]] = Map(),
  val postForm: Map[String, List[String]] = Map()
)

case class CSResp(
  val status: String,
  val statusCode: Int,
  val header: Map[String, List[String]] = Map(),
  val body: String
)

case class CSSession() {

  val createTime: Long = System.currentTimeMillis

  val timeout: Long = 1000L * 60  // TODO Timeout read from config

  val uuid: String = UUID.randomUUID.toString

  val aesKey: SecretKey = new SecretKeySpec(AesUtil.randomAesKey, "AES")

  def isExpired(): Boolean = System.currentTimeMillis >= createTime + timeout
}

object DataWrites {

  implicit object CSReqWrites extends Writes[CSReq] {

    def writes(r: CSReq): JsObject = {
      Json.obj(
        "Method" -> r.method,
        "Host" -> r.host,
        "RemoteAddr" -> r.remoteAddr,
        "RequestURI" -> r.requestUri,
        "Header" -> Json.toJson(r.header),
        "PostForm" -> Json.toJson(r.postForm)
      )
    }
  }

  implicit object CSRespWrites extends Writes[CSResp] {

    def writes(r: CSResp): JsObject = {
      Json.obj(
        "Status" -> r.status,
        "StatusCode" -> r.statusCode,
        "Header" -> r.header,
        "Body" -> r.body
      )
    }
  }

  implicit object CSSessionWrites extends Writes[CSSession] {

    def writes(s: CSSession): JsObject = {
      Json.obj(
        "Uuid" -> s.uuid,
        "AesKey" -> s.aesKey,
        "Timeout" -> s.timeout
      )
    }
  }

  implicit object SecretKeyWrites extends Writes[SecretKey] {

    def writes(k: SecretKey): JsString = JsString(Base64.encodeBase64String(k.getEncoded))
  }
}
