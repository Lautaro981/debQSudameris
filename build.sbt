import play.Project._

name := "debQSudameris"

version := "1.0.0"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "ch.qos.logback.contrib" % "logback-json-classic" % "0.1.5",
  "ch.qos.logback.contrib" % "logback-jackson" % "0.1.5",
  "org.codehaus.janino" % "janino" % "2.7.8",
  "org.codehaus.janino" % "commons-compiler" % "2.7.8",
  "mysql" % "mysql-connector-java" % "5.1.38",
  "com.microsoft.sqlserver" % "mssql-jdbc" % "6.2.0.jre8",
  "com.debmedia" % "utils_2.10" % "1.7.0"
)

play.Project.playJavaSettings

resolvers += "Archiva" at "https://archiva.debmedia.com/repository/internal"

publishMavenStyle := true

publishTo := Some("Archiva Managed internal Repository" at "https://archiva.debmedia.com/repository/internal")

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

organization := "com.debmedia"

