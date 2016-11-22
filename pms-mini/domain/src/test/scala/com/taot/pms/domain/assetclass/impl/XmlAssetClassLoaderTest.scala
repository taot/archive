package com.taot.pms.domain.assetclass.impl

import com.taot.pms.domain.assetclass.AssetClass
import org.scalatest.{ShouldMatchers, WordSpec}

class XmlAssetClassLoaderTest extends WordSpec with ShouldMatchers {

  "XmlAssetClassLoader" should {

    "load asset classes from xml" in {
      val loader = new XmlAssetClassLoader("asset-class-test.xml")
      val assetClassRoot = loader.load().get
      val total = countRecursive(assetClassRoot, Nil)
      total should be (6)
    }
  }

  private def countRecursive(assetClass: AssetClass, path: List[String]): Int = {
    println(path.reverse + " " + assetClass.code)
    val nextPath = assetClass.code :: path
    1 + assetClass.children.foldLeft(0) { (accu, l) => accu + countRecursive(l, nextPath) }
  }
}
