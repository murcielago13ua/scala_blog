val projectId = "web_client"

lazy val modelJS = ProjectRef(file("./model"), "modelJS")

lazy val web_client = Project(id = projectId, base = file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .settings(
    version := "0.1",
    scalaVersion := "2.11.8",
    scalaJSUseMainModuleInitializer := true,
    skip in packageJSDependencies := false,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.2",
      //endpoints
      "org.julienrf" %%% "endpoints-xhr-client" % "0.2.0",
      "org.julienrf" %%% "endpoints-xhr-client-circe" % "0.2.0",

      //binding
      "com.thoughtworks.binding" %%% "dom" % "10.0.2",
      "com.thoughtworks.binding" %%% "futurebinding" % "10.0.2",

      //http
      "fr.hmil" %%% "roshttp" % "2.0.2"
    ),
    jsDependencies ++= Seq(
      RuntimeDOM
    )
  )
  .dependsOn(modelJS)
