import com.twitter.scrooge._

libraryDependencies ++= Seq(
  "com.google.protobuf" % "protobuf-java" % "2.5.0",
  "com.googlecode.protobuf-rpc-pro" % "protobuf-rpc-pro-duplex" % "3.2.2",
  "com.twitter" %% "finagle-thrift" % "6.12.1",
  "com.twitter" %% "scrooge-runtime" % "3.12.3",
  "com.twitter" %% "scrooge-serializer" % "3.12.3",
  "joda-time" % "joda-time" % "2.2",
  "net.sandrogrzicic" %% "scalabuff-runtime" % "1.3.7",
  "org.joda" % "joda-convert" % "1.5"
)

seq(ScroogeSBT.newSettings: _*)

resolvers += "twitter-repo" at "http://maven.twttr.com"
