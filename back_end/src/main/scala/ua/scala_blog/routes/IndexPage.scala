package ua.scala_blog.routes

import java.nio.file.{Files, Paths}

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server._
import akka.stream.scaladsl.FileIO


object IndexPage extends Routes with Directives {
  private val path = Paths.get("./src/main/resources/public/index.html")

  def route: Route = pathSingleSlash {
    complete(
      HttpResponse(
        entity = HttpEntity(
          contentType = ContentTypes.`text/html(UTF-8)`,
          data = FileIO.fromPath(path)
        )
      )
    )
  }
}
