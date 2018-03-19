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

  //resourceDirectory in Compile := (scalaSource in Compile).value
  
  libraryDependencies += "com.github.wookietreiber" %% "scala-chart" % "latest.integration"
  libraryDependencies += "com.itextpdf" % "itextpdf" % "5.5.6"
  libraryDependencies += "org.jfree" % "jfreesvg" % "3.0"
  libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.144-R12"
  libraryDependencies ++= Seq("org.scalafx" %% "scalafxml-core-sfx8" % "0.3")


  
   
  //we need below for fxml-load.
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  shellPrompt := { state => System.getProperty("user.name") + ":" + Project.extract(state).currentRef.project + "> " }
  // Fork a new JVM for 'run' and 'test:run', to avoid JavaFX double initialization problems
  fork := true
