name := "sbt-i18n"

organization := "com.github.taot"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.2"

sbtVersion := "13.0"

sbtPlugin := true

publishMavenStyle := true

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
)