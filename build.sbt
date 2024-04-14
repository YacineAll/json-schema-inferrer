ThisBuild / version := "0.0.2-rc1"
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
    Test / parallelExecution := false // Disable parallel execution for testing
  )

ThisBuild / githubWorkflowJavaVersions := Seq(
  JavaSpec.temurin("11"),
  JavaSpec.temurin("17"),
  JavaSpec.temurin("20"))

ThisBuild / githubWorkflowPublishTargetBranches := Seq(
  RefPredicate.StartsWith(Ref.Tag("v")),
  RefPredicate.Equals(Ref.Branch("main")))

ThisBuild / githubWorkflowBuildPostamble ++= Seq(
  WorkflowStep.Sbt(
    List("${{ matrix.ci }}", "scalafmtSbtCheck", "scalafmtCheck", "test:scalafmtCheck"),
    name = Some("Formatting")),
  WorkflowStep.Sbt(List("${{ matrix.ci }}", "coverage", "test"), name = Some("Build tests")),
  WorkflowStep.Sbt(List("${{ matrix.ci }}", "coverageReport"), name = Some("Coverage Report")),
  WorkflowStep.Use(
    ref = UseRef.Public("codecov", "codecov-action", "v4.0.1"),
    name = Some("Upload coverage reports to Codecov"),
    params =
      Map("token" -> "${{ secrets.CODECOV_TOKEN }}", "slug" -> "YacineAll/json-schema-inferrer")))

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    commands = List("ci-release"),
    name = Some("Publish release"),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}")))

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
