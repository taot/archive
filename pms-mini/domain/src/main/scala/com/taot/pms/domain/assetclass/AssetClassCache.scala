package com.taot.pms.domain.assetclass

trait AssetClassCache {

  def getById(id: Long): AssetClass

  def getByLongCode(longCode: String): AssetClass

  def getByLongCode(longCode: Seq[String]): AssetClass

  def getParent(id: Long): Option[AssetClass]

  def getParent(assetClass: AssetClass): Option[AssetClass]

  def isDescendentOf(assetClass: AssetClass): Boolean

  def getAllLeafDescendents(assetClass: AssetClass): Set[AssetClass]

  def getAll(): List[AssetClass]
}
