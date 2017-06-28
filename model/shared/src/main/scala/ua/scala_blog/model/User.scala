package ua.scala_blog.model

import io.circe.generic.JsonCodec
import java.time._


object Roles extends Enumeration {
  type Role = Value
  val Admin, Author, Reader = Value
}


@JsonCodec
case class User(login: String, email: String,
                password: String, role: Roles.Role,
                registrationDate: LocalDate,
                fullNameOpt: Option[String],
                avatarPathOpt: Option[String],
                workplaceOpt: Option[String],
                addressOpt: Option[Address],
                about: Option[String],
                id: Option[Long] = None, isDeleted: Boolean = false
               ) extends GenericEntity {
  def withoutAuth: User = this.copy(password = "*")
}