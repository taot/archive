package util

import java.io.IOException
import java.security.PublicKey
import scala.io.Source


object RsaUtil {

  val RSA = "RSA"

  val TRANSFORMATION = "RSA/ECB/PKCS1Padding"

  def loadPublicKeys(resource: String): Array[PublicKey] = {
    withSource(resource) { s =>

    }
    Array[PublicKey]()
  }

  private def withSource[R](resource: String)(op: Source => R): R = {
    val inputStream = getClass().getResourceAsStream(resource)
    if (inputStream == null) {
      throw new IOException("Failed to find resource " + resource + " in classpth.")
    }
    var source: Source = null
    try {
      source = Source.fromInputStream(inputStream)
      op(source)
    } finally {
      if (source != null) {
        source.close
      }
    }
  }
}
