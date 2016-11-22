libraryDependencies ++= {
  val sprayVersion = "1.2.0"
  val akkaVersion = "2.2.1"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaVersion,
    "io.spray" % "spray-can" % sprayVersion,
    "io.spray" %% "spray-json" % "1.2.5",
    "io.spray" % "spray-routing" % sprayVersion,
    "io.spray" % "spray-testkit" % sprayVersion
  )
}

resolvers += "spray-repo" at "http://repo.spray.io"