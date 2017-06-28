package ua.scala_blog.routes

import akka.http.scaladsl.server._
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import endpoints.algebra
import endpoints.akkahttp.server
import endpoints.algebra.CirceEntities.CirceCodec


trait Service[E <: algebra.Endpoints] extends server.Endpoints
                                              with algebra.CirceEntities
                                              with Directives
                                              with Routes {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._


  def jsonRequest[A: CirceCodec] = {
    implicit val decoder = CirceCodec[A].decoder
    Directives.entity[A](implicitly[FromRequestUnmarshaller[A]])
  }

  def jsonResponse[A: CirceCodec] = a => {
    implicit val encoder = implicitly[CirceCodec[A]].encoder
    Directives.complete(a)
  }

  protected val NoAuth: Long = -1
}
