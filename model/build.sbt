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
    resolvers += Resolver.sonatypeRepo("releases"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %% "scala-java-time" % "2.0.0-M9",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test",

      "org.julienrf" %% "endpoints-algebra" % "0.2.0",
      "org.julienrf" %% "endpoints-algebra-circe" % "0.2.0",

      "io.circe" %% "circe-core" % "0.7.1",
      "io.circe" %% "circe-generic" % "0.7.1"
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % "2.0.0-M9",
      "org.scalatest" %%% "scalatest" % "3.0.0" % "test",

      "org.julienrf" %%% "endpoints-algebra" % "0.2.0",
      "org.julienrf" %%% "endpoints-algebra-circe" % "0.2.0",

      "io.circe" %%% "circe-core" % "0.7.1",
      "io.circe" %%% "circe-generic" % "0.7.1"
    )
  )

lazy val modelJVM = model.jvm
lazy val modelJS = model.js