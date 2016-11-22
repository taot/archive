libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "joda-time" % "joda-time" % "2.2",
  "org.joda" % "joda-convert" % "1.5",
  "org.scalatest" % "scalatest_2.10" % "2.0",
  "org.slf4j" % "slf4j-api" % "1.7.2",
  "org.springframework.scala" % "spring-scala" % "1.0.0.M2",
  "com.typesafe" % "config" % "1.2.0"
)

resolvers += "repo.springsource.org-milestone" at "https://repo.spring.io/libs-milestone"