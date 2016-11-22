package com.github.taot.config

class Setting[T : Extractor](path: String) {

  private val config = TypesafeConfig.config

  def apply(): T = {
    val extractor = implicitly[Extractor[T]]
    extractor.extract(config, path)
  }
}