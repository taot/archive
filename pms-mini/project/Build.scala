import sbt._
import Keys._

object PmsMiniBuild extends Build {

  lazy val pmsMini = Project(id = "pms-mini", base = file(".")).aggregate(
    common,
    domain,
    gui,
    marketdata,
    persist,
    rpc,
    rpc_server,
    tools,
    web
  )

  lazy val common = Project(id = "common", base = file("common"))

  lazy val domain = Project(id = "domain", base = file("domain")) dependsOn(common, marketdata, persist)

  lazy val gui = Project(id = "gui", base = file("gui")) dependsOn(common, rpc)

  lazy val marketdata = Project(id = "marketdata", base = file("marketdata")) dependsOn(common)

  lazy val persist = Project(id = "persist", base = file("persist")) dependsOn(common)

  lazy val rpc = Project(id = "rpc", base = file("rpc"))

  lazy val rpc_server = Project(id = "rpc-server", base = file("rpc-server")) dependsOn(common, rpc, domain)

  lazy val tools = Project(id = "tools", base = file("tools")) dependsOn(domain)

  lazy val web = Project(id = "web", base = file("web")) dependsOn(domain)
}
