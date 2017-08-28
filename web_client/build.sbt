val projectId = "web_client"

lazy val modelJS = ProjectRef(file("../model"), "modelJS")

lazy val web_client = Project(id = projectId, base = file("."))
  .dependsOn(modelJS)
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .settings(
    version := "0.1",
    scalaVersion := "2.11.8",
    scalaJSUseMainModuleInitializer := true,
    skip in packageJSDependencies := false,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.2",
      "com.github.japgolly.scalajs-react" %%% "core" % "1.1.0",
      "com.github.japgolly.scalajs-react" %%% "extra" % "1.1.0",

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
      RuntimeDOM,
      "org.webjars.bower" % "react" % "15.6.1"
        / "react-with-addons.js"
        minified "react-with-addons.min.js"
        commonJSName "React",

      "org.webjars.bower" % "react" % "15.6.1"
        / "react-dom.js"
        minified "react-dom.min.js"
        dependsOn "react-with-addons.js"
        commonJSName "ReactDOM",

      "org.webjars.bower" % "react" % "15.6.1"
        / "react-dom-server.js"
        minified "react-dom-server.min.js"
        dependsOn "react-dom.js"
        commonJSName "ReactDOMServer")
  )
