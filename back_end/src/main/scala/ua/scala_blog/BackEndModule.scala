package ua.scala_blog

import java.util.concurrent.Executors

import com.softwaremill.macwire._
import com.softwaremill.tagging._
import routes._
import repositories._
import PostgresProfile.api._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.softwaremill.session.{SessionConfig, SessionManager}
import com.typesafe.config.{Config, ConfigFactory}
import ua.scala_blog.api_endpoints._
import scala.concurrent.ExecutionContext


trait BackEndModule {
//  lazy val loginService: Service[Authorization] = wire[LoginService]
//  lazy val postsService: Service[Posts] = wire[PostsService]
//  lazy val usersSerivce: Service[Users] = wire[UsersService]
  lazy val indexPage: Routes = IndexPage

  lazy implicit val sessionManager: SessionManager[Long] = new SessionManager[Long](
    SessionConfig.default(
      "sejhbknkml6532.009ouihkjn/.,.mk,njfadglm.bm njhiafof;kmioiejflwnkihjksdnvijlhfjadklgp;klknjsdnlojsgdfnlgpokdflgjifdkn"
    )
  )

  lazy implicit val globalEc: ExecutionContext @@ Global = ExecutionContext.global.taggedWith[Global]
  lazy private implicit val dbEc: ExecutionContext @@ Db = ExecutionContext.fromExecutor(Executors.newCachedThreadPool()).taggedWith[Db]

  lazy val config: Config = ConfigFactory.load()
//  lazy val db: Database = Database.forConfig("postgresql", config)
//  lazy val userRepository: UserRepository = wire[UserRepository]
//  lazy val postsRepository: PostsRepository = wire[PostsRepository]

  lazy implicit val system: ActorSystem = ActorSystem("blog_system")
  lazy implicit val materializer: ActorMaterializer = ActorMaterializer()
}
