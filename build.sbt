import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.11.7",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "scala-test",
    libraryDependencies += scalaTest % Test
  )

  libraryDependencies += "com.github.wookietreiber" %% "scala-chart" % "latest.integration"
  libraryDependencies += "com.itextpdf" % "itextpdf" % "5.5.6"
  libraryDependencies += "org.jfree" % "jfreesvg" % "3.0"
