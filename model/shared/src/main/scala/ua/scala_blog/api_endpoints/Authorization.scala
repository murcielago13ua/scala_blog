package ua.scala_blog.api_endpoints

import endpoints.algebra.CirceEntities
import io.circe.generic.JsonCodec
import Authorization._
import ua.scala_blog.model._


trait Authorization extends CirceEntities {
  val root = path / "auth"

//  val login = endpoint(request(Post, path / "login", jsonRequest[Auth]), jsonResponse[StatusCode Either Long])
//  val logout = endpoint(request(Post, path / "logout", emptyRequest), jsonResponse[StatusCode])
  val signUp = endpoint(request(Put, path / "sign-up", jsonRequest[User]), jsonResponse[StatusCode])
}

object Authorization {
  @JsonCodec case class Auth(login: String, password: String)
}
