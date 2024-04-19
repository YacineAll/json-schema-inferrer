ThisBuild / scalaVersion := "3.4.1"

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
resolvers += "Sonatype OSS Releases" at "https://s01.oss.sonatype.org/content/repositories/releases"

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

ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches :=
  Seq(RefPredicate.StartsWith(Ref.Tag("v")), RefPredicate.Equals(Ref.Branch("main")))

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(commands = List("ci-release"), name = Some("Publish project")))

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    commands = List("ci-release"),
    name = Some("Publish project"),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}")))

ThisBuild / githubWorkflowBuildPostamble ++= Seq(
  WorkflowStep.Sbt(
    name = Some("Formatting"),
    commands = List("scalafmtSbtCheck", "scalafmtCheck", "test:scalafmtCheck")),
  WorkflowStep.Sbt(commands = List("coverage", "coverageReport"), name = Some("Coverage Report")),
  WorkflowStep.Use(
    ref = UseRef.Public("codecov", "codecov-action", "v4.0.1"),
    name = Some("Upload coverage reports to Codecov"),
    params =
      Map("token" -> "${{ secrets.CODECOV_TOKEN }}", "slug" -> "YacineAll/json-schema-inferrer")))
ThisBuild / githubWorkflowAddedJobs += WorkflowJob(
  id = "post-publish",
  name = "post publish",
  cond = Some(
    "${{ success() && github.event_name == 'push' && startsWith(github.ref, 'refs/tags/') }}"),
  needs = List("publish"),
  steps = List(
    WorkflowStep.Run(
      name = Some("Create release"),
      env =
        Map("GITHUB_TOKEN" -> "${{ secrets.GITHUB_TOKEN }}", "tag" -> "${{ github.ref_name }}"),
      commands = List(
        "gh",
        "release",
        "create",
        "\"$tag\"",
        "--repo=\"$GITHUB_REPOSITORY\"",
        "--title=\"${GITHUB_REPOSITORY#*/} ${tag#v}\"",
        "--generate-notes"))))
