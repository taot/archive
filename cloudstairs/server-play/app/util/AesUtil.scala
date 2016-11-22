package util

import java.security.SecureRandom
import javax.crypto.{ Cipher, SecretKey }
import javax.crypto.spec.IvParameterSpec
import org.apache.commons.codec.binary.Hex
import org.slf4j.{ Logger, LoggerFactory }


object AesUtil extends Logging {

  val BLOCK_SIZE: Int = 16

  private val rand = new SecureRandom

  def randomAesKey(): Array[Byte] = {
    val bytes = new Array[Byte](BLOCK_SIZE)
    synchronized {
      rand.nextBytes(bytes)
    }
    logger.debug("Random aes key (hex): " + Hex.encodeHexString(bytes))
    bytes
  }

  def decrypt(key: SecretKey, encrypted: Array[Byte]): Array[Byte] = {
    val cipher = Cipher.getInstance("AES/CBC/NoPadding")
    logger.info("AES Key: " + Hex.encodeHexString(key.getEncoded))
    val iv = new IvParameterSpec(encrypted, 0, BLOCK_SIZE)
    logger.debug("encoded.length: " + encrypted.length)
    cipher.init(Cipher.DECRYPT_MODE, key, iv)
    val data = cipher.doFinal(encrypted, BLOCK_SIZE, encrypted.length - BLOCK_SIZE)
    data
  }
}
