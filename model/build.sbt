lazy val model_root = project.in(file("."))
  .aggregate(modelJS, modelJVM)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val model = crossProject.in(file("."))
  .settings(
    name := "model",
    version := "0.1",
    scalaVersion := "2.11.8",
    organization := "dataroot",
    resolvers += Resolver.sonatypeRepo("releases"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.0" % "test",

      "org.julienrf" %% "endpoints-algebra" % "0.2.0",
      "org.julienrf" %% "endpoints-algebra-circe" % "0.2.0",

      "io.circe" %% "circe-core" % "0.8.0",
      "io.circe" %% "circe-generic" % "0.8.0",
      "io.circe" %% "circe-java8" % "0.8.0"
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-java-time" % "0.2.1",
      "org.scalatest" %%% "scalatest" % "3.0.0" % "test",

      "org.julienrf" %%% "endpoints-algebra" % "0.2.0",
      "org.julienrf" %%% "endpoints-algebra-circe" % "0.2.0",

      "io.circe" %%% "circe-core" % "0.8.0",
      "io.circe" %%% "circe-generic" % "0.8.0",
      "io.circe" %%% "circe-java8" % "0.8.0"
    )
  )

lazy val modelJVM = model.jvm
lazy val modelJS = model.js