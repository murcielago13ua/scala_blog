package ua.scala_blog.routes

import akka.http.scaladsl.server._
import com.softwaremill.session.SessionManager
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import ua.scala_blog.api_endpoints._
import ua.scala_blog.model._
import ua.scala_blog.repositories.UserRepository

import scala.concurrent.{ExecutionContext, Future}
import com.softwaremill.tagging._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import ua.scala_blog.Global
import ua.scala_blog.api_endpoints.Authorization.Auth
import io.circe.generic.auto._

import scala.util.{Failure, Success}


class LoginService(usersRepo: UserRepository)
                  (implicit ec: ExecutionContext @@ Global,
                   sessionManager: SessionManager[Long]
                  ) extends Service[Authorization] with Authorization with FailFastCirceSupport {

  private def signUpUser(user: User): Future[StatusCode] = usersRepo.upsert(NoAuth, user.copy(id = None))

  override def route: Route =
    signUp.implementedByAsync(signUpUser) ~
      path("auth" / "login") {
        entity(as[Auth]) { auth =>
          onComplete(usersRepo login auth) {
            case Failure(e) => complete(StatusCode.InternalServerError)
            case Success(Left(sc)) => complete(sc)
            case Success(Right(userId)) =>
              setSession(oneOff, usingCookies, userId) {
                complete(StatusCode.Ok)
              }
          }
        }
      } ~
      path("auth" / "logout") {
        invalidateSession(oneOff, usingCookies) {
          complete(StatusCode.Ok)
        }
      }
}
