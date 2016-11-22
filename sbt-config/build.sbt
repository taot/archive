name := "sbt-config"

organization := "com.github.taot"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.2"

sbtVersion := "13.0"

sbtPlugin := true

publishMavenStyle := true

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.3.0",
  "com.typesafe" % "config" % "1.2.1",
  "org.scalatest" %% "scalatest" % "2.0" % "test"
)

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"