ThisBuild / scalaVersion := "3.4.1"
ThisBuild / organization := ""

inThisBuild(
  List(
    organization := "io.github.yacineall",
    homepage := Some(url("https://github.com/YacineAll/json-schema-inferrer")),
    ThisBuild / description := "Scala library for automating JSON Schema generation from data and merging schemas for comprehensive data modeling.",
    licenses := List("Apache-2.0" -> new URL("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        id = "yacineAll",
        name = "Yacine ALLOUACHE",
        email = "yacine.allouache.dev@gmail.com",
        url = url("https://github.com/YacineAll")))))
sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"

lazy val root = (project in file("."))
  .settings(
    name := "json-schema-inferrer",
    libraryDependencies ++= Seq(
      "org.json4s" %% "json4s-jackson" % "4.1.0-M5",
      "org.json4s" %% "json4s-native" % "4.1.0-M5",
      // Testing dependencies
      "org.scalatest" %% "scalatest" % "3.2.17" % Test),
    // ScalaTest configuration
    testFrameworks += new TestFramework("org.scalatestplus.junit.JUnitRunner"),
    testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oD"),
    Test / parallelExecution := true)
