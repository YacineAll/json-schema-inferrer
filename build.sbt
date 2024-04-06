ThisBuild / version := "0.0.1"
ThisBuild / scalaVersion := "3.4.1"
ThisBuild / organization := "io.github.yacineall"
ThisBuild / organizationName := "yacineAll"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/YacineAll/json-schema-inferrer"),
    "scm:git@github.com:YacineAll/json-schema-inferrer.git"))

ThisBuild / developers := List(
  Developer(
    id = "yacineAll",
    name = "Yacine ALLOUACHE",
    email = "yacine.allouache.dev@gmail.com",
    url = url("https://github.com/YacineAll")))

ThisBuild / description := "Scala library for automating JSON Schema generation from data and merging schemas for comprehensive data modeling."
ThisBuild / licenses := List(
  "Apache-2.0" -> new URL("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage := Some(url("https://github.com/YacineAll/json-schema-inferrer"))

//publishTo := sonatypePublishToBundle.value
sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
publishMavenStyle := true
sonatypeLogLevel := "DEBUG"
// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / versionScheme := Some("early-semver")


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
    testOptions += Tests.Argument(
      TestFrameworks.ScalaTest,
      "-oD"
    ), // Output detailed test results
    Test / parallelExecution := false, // Disable parallel execution for testing
    publishTo := {
      // For accounts created after Feb 2021:
      val nexus = "https://s01.oss.sonatype.org/"
      if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
      else Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    publishMavenStyle := true)
