ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.1"

lazy val root = (project in file("."))
  .settings(
    name := "json-schema-inferrer",
    libraryDependencies ++= Seq(
      "org.json4s" %% "json4s-jackson" % "4.1.0-M5",
      "org.json4s" %% "json4s-native" % "4.1.0-M5",
      // Testing dependencies
      "org.scalatest" %% "scalatest" % "3.2.17" % Test,
    ),
    // ScalaTest configuration
    testFrameworks += new TestFramework("org.scalatestplus.junit.JUnitRunner"),
    testOptions += Tests.Argument(
      TestFrameworks.ScalaTest,
      "-oD"
    ), // Output detailed test results
    Test / parallelExecution := false // Disable parallel execution for testing
  )
