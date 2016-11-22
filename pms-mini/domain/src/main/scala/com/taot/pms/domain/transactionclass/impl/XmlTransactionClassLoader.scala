package com.taot.pms.domain.transactionclass.impl

import com.taot.pms.domain.transactionclass.TransactionClass
import java.io.InputStream
import org.dom4j.io.SAXReader
import com.taot.pms.domain.assetclass.{AssetClass, AssetClassCache}
import org.dom4j.Element
import scala.collection.JavaConversions._

class XmlTransactionClassLoader(inputStream: InputStream, assetClassCache: AssetClassCache) extends TransactionClassLoader {

  def this(resourceName: String, assetClassCache: AssetClassCache) = {
    this(Thread.currentThread().getContextClassLoader.getResourceAsStream(resourceName), assetClassCache)
  }

  override def load(): List[TransactionClass] = {
    try {
      val reader = new SAXReader()
      val doc = reader.read(inputStream)
      val root = doc.getRootElement
      doLoad(root)
    } finally {
      if (inputStream != null) {
        inputStream.close()
      }
    }
  }

  private def doLoad(elem: Element): List[TransactionClass] = {
    val assetElems = elem.elements("asset").map(_.asInstanceOf[Element])
    val buffer = assetElems.flatMap { elem =>
      val assetClassCode = elem.attribute("class").getValue
      val assetClass = assetClassCache.getByLongCode(assetClassCode)
      val tranClasses = loadForAssetClass(elem, assetClass)
      tranClasses
    }
    buffer.toList
  }

  private def loadForAssetClass(node: Element, assetClass: AssetClass): List[TransactionClass] = {
    val children = node.elements("transaction-class").map(_.asInstanceOf[Element])
    val buffer = for  {
      c <- children
      code = c.attribute("code").getValue
    } yield TransactionClass(code, assetClass)
    buffer.toList
  }
}
