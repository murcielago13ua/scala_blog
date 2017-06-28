val projectId = "web_client"

lazy val modelJS = ProjectRef(file("model"), "modelJS")

lazy val web_client = Project(id = projectId, base = file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .settings(
    version := "0.1",
    scalaVersion := "2.11.8",
    scalaJSUseMainModuleInitializer := true,
    skip in packageJSDependencies := false,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.2",

      "com.github.japgolly.scalajs-react" %%% "core" % "1.0.1",
      "com.github.japgolly.scalajs-react" %%% "extra" % "1.0.0",

      "com.github.japgolly.scalacss" %%% "core" % "0.5.3",
      "com.github.japgolly.scalacss" %%% "ext-react" % "0.5.3",

      "io.circe" %%% "circe-core" % "0.7.1",
      "io.circe" %%% "circe-generic" % "0.7.1",
      "io.circe" %%% "circe-parser" % "0.7.1"
    ),
    jsDependencies ++= Seq(
      "org.webjars.bower" % "react" % "15.3.1" / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
      "org.webjars.bower" % "react" % "15.3.1" / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM",
      RuntimeDOM
    )
  )
  .dependsOn(modelJS)
