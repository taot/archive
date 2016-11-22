package com.github.taot.config

import scala.collection.JavaConversions._

import com.typesafe.config.Config

trait Extractor[T] {
  def extract(config: Config, path: String): T
}

class NullExtractor extends Extractor[Null] {
  override def extract(config: Config, path: String): Null = null
}

class BooleanExtractor extends Extractor[Boolean] {
  override def extract(config: Config, path: String): Boolean = config.getBoolean(path)
}

class StringExtractor extends Extractor[String] {
  override def extract(config: Config, path: String): String = config.getString(path)
}

class IntExtractor extends Extractor[Int] {
  override def extract(config: Config, path: String): Int = config.getInt(path)
}

class LongExtractor extends Extractor[Long] {
  override def extract(config: Config, path: String): Long = config.getLong(path)
}

class DoubleExtractor extends Extractor[Double] {
  override def extract(config: Config, path: String): Double = config.getDouble(path)
}

class StringListExtractor extends Extractor[List[String]] {
  override def extract(config: Config, path: String): List[String] = config.getStringList(path).toList
}

class IntListExtractor extends Extractor[List[Int]] {
  override def extract(config: Config, path: String): List[Int] = config.getIntList(path).toList.map(_.intValue())
}

class LongListExtractor extends Extractor[List[Long]] {
  override def extract(config: Config, path: String): List[Long] = config.getLongList(path).toList.map(_.longValue())
}

class DoubleListExtractor extends Extractor[List[Double]] {
  override def extract(config: Config, path: String): List[Double] = config.getDoubleList(path).toList.map(_.doubleValue())
}

class BooleanListExtractor extends Extractor[List[Boolean]] {
  override def extract(config: Config, path: String): List[Boolean] = config.getBooleanList(path).toList.map(_.booleanValue())
}