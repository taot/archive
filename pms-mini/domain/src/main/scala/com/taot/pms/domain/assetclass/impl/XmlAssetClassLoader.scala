package com.taot.pms.domain.assetclass.impl

import java.io.InputStream

import scala.collection.JavaConversions._

import com.taot.pms.domain.BusinessException
import com.taot.pms.domain.assetclass.AssetClass
import org.dom4j.{Attribute, Element}
import org.dom4j.io.SAXReader

class XmlAssetClassLoader(inputStream: InputStream) extends AssetClassLoader {

  def this(resourceName: String) = {
    this(Thread.currentThread().getContextClassLoader.getResourceAsStream(resourceName))
  }

  override def load(): Option[AssetClass] = {
    try {
      val reader = new SAXReader()
      val doc = reader.read(inputStream)
      val root = doc.getRootElement
      val assetClassRoot = loadRecursive(root)
      Some(assetClassRoot)
    } finally {
      if (inputStream != null) {
        inputStream.close()
      }
    }
  }

  private def loadRecursive(elem: Element): AssetClass = {
    val code = elem.attribute("code") match {
      case attr: Attribute => attr.getValue
      case _ => throw new BusinessException("Invalid asset classs xml. Missing attribute 'code' on assetClass element")
    }
    val desc = elem.attribute("desc") match {
      case attr: Attribute => attr.getValue
      case _ => ""
    }

    val children = for {
      e <- elem.elements("asset-class").map(_.asInstanceOf[Element])
      child = loadRecursive(e)
    } yield child

    AssetClass(code, desc, children.toList)
  }
}
