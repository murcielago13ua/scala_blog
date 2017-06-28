package ua.scala_blog.repositories

import java.time.LocalDate
import ua.scala_blog.model
import model.{Tag => _, _}
import PostgresProfile.api.{Tag => DbTag, _}
import ua.scala_blog.api_endpoints.{Authorization, StatusCode, Users}
import scala.concurrent.{ExecutionContext, Future}
import com.softwaremill.tagging._


class UserTable(tag: DbTag) extends GenericTable[User](tag, "users") {
  val login = column[String]("login")
  val email = column[String]("email")
  val password = column[String]("password")
  val role = column[Roles.Role]("role")
  val registrationDate = column[LocalDate]("registartion_date")
  val fullName = column[Option[String]]("full_name")
  val avatarPath = column[Option[String]]("avatar_path")
  val workplace = column[Option[String]]("workplace")
  val address = column[Option[Address]]("address")
  val about = column[Option[String]]("about")

  def * = (login, email, password, role, registrationDate, fullName, avatarPath,
    workplace, address, about, id.?, isDeleted) <> ((User.apply _).tupled, User.unapply)
}

object UserTable {
  val query = TableQuery[UserTable]
}

class UserRepository(db: Database)(implicit ec: ExecutionContext @@ Db) extends GenericRepository[User, UserTable](db, UserTable.query) {
  def login(auth: Authorization.Auth): Future[StatusCode Either Long] = db run {
    val Authorization.Auth(login, password) = auth
    UserTable.query.filter(r => r.login === login && r.password === password).map(_.id).result.headOption
  } map {
    case Some(userId) => Right(userId)
    case None => Left(StatusCode.AccessDenied)
  }

  def findById(authId: Long, userId: Long): Future[StatusCode Either User] = db run {
    adminAccessQuery(authId).result flatMap {
      case false => UserTable.query.filter(r => r.id === userId && !r.isDeleted).result.headOption
      case true => UserTable.query.filter(_.id === userId).result.headOption
    }
  } map {
    case Some(user) => Right(user.withoutAuth)
    case None => Left(StatusCode.NotFound)
  }

  def upsert(authId: Long, user: User): Future[StatusCode] = {
    val query = user.id match {
      case None => UserTable.query += user
      case Some(userId) =>
        UserTable.query.filter(r => r.id === userId && r.id === authId).update(user)
    }

    db run query map {
      case 1 => StatusCode.Ok
      case _ => StatusCode.AccessDenied
    }
  }

  def delete(authId: Long, userId: Long): Future[StatusCode] = db run {
    val userQuery = UserTable.query.filter(r => r.id === userId && r.id === authId)
    val accessQuery = adminAccessQuery(authId) || userQuery.exists

    userQuery.filter(_ => accessQuery).map(_.isDeleted).update(true)
  } map {
    case 1 => StatusCode.Ok
    case _ => StatusCode.AccessDenied
  }

  def changeRole(request: Users.ChangeRoleRequest): Future[StatusCode] = db run {
    val Users.ChangeRoleRequest(adminId, userId, newRole) = request
    UserTable.query.filter(_.id === userId && adminAccessQuery(adminId)).map(_.role).update(newRole)
  } map {
    case 1 => StatusCode.Ok
    case _ => StatusCode.AccessDenied
  }
}
