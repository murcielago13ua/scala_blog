package ua.scala_blog.api_endpoints

import io.circe.generic.JsonCodec
import endpoints.algebra.CirceEntities
import ua.scala_blog.api_endpoints.Users.ChangeRoleRequest
import ua.scala_blog.model._


trait Users extends CirceEntities {
  val root = path / "users"

  type UserId = Long

  val findById = endpoint(request(Get, root / segment[UserId]), jsonResponse[StatusCode Either User])
  val edit = endpoint(request(Post, root, jsonRequest[User]), jsonResponse[StatusCode])
  val deleteById = endpoint(request(Delete, root / segment[UserId]), jsonResponse[StatusCode])
  val changeRole = endpoint(request(Post, root / "administration", jsonRequest[ChangeRoleRequest]), jsonResponse[StatusCode])
}

object Users {
  @JsonCodec case class ChangeRoleRequest(adminId: Long, userId: Long, role: Roles.Role)
}
