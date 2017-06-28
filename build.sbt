import sbt.Keys._

val projectId = "scala_blog"

lazy val web_client = Project.project.in(file("web_client"))
lazy val back_end = ProjectRef(file("back_end"), "back_end")

lazy val root = Project(id = projectId, base = file("."))
  .enablePlugins(WebScalaJS)
  .settings(
    scalaVersion := "2.11.8",
    scalaJSProjects := Seq(web_client)
  )
  .aggregate(back_end)