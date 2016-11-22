import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "cloudstair-server-play"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "commons-codec" % "commons-codec" % "1.8"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    unmanagedResourceDirectories in Test += file("test/conf")
  )

}
