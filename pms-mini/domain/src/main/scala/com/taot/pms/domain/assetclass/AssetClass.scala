package com.taot.pms.domain.assetclass

case class AssetClass(
  code: String,
  desc: String,
  children: List[AssetClass],
  id: Long = -1L
) {

  def isLeaf: Boolean = children.isEmpty

  override def toString(): String = s"AssetClass [id = ${id}, code = ${code}, desc = ${desc}]"
}