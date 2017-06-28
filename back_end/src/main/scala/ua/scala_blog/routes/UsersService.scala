package ua.scala_blog.routes

import akka.http.scaladsl.server._
import com.softwaremill.session.SessionManager
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import com.softwaremill.tagging._
import ua.scala_blog.Global
import ua.scala_blog.api_endpoints.Users.ChangeRoleRequest
import ua.scala_blog.api_endpoints._
import ua.scala_blog.model._
import ua.scala_blog.repositories.UserRepository

import scala.concurrent.ExecutionContext


class UsersService(usersRepo: UserRepository)
                  (implicit ec: ExecutionContext @@ Global,
                   sessionManager: SessionManager[Long]
                  ) extends Service[Users] with Users {

  override def route: Route = optionalSession(oneOff, usingCookies) { userIdOpt =>
    findById.implementedByAsync(usersRepo.findById(userIdOpt.getOrElse(NoAuth), _))
  } ~
    requiredSession(oneOff, usingCookies) { userId =>
      edit.implementedByAsync(usersRepo.upsert(userId, _)) ~
        deleteById.implementedByAsync(usersRepo.delete(userId, _)) ~
        changeRole.implementedByAsync {
          (crr: ChangeRoleRequest) => usersRepo.changeRole(crr.copy(adminId = userId))
        }
    }
}
