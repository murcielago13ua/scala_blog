package ua.scala_blog.routes

import akka.http.scaladsl.server._
import com.softwaremill.session.SessionManager
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import com.softwaremill.tagging._
import ua.scala_blog.Global
import ua.scala_blog.api_endpoints.Posts.PostWithTags
import ua.scala_blog.api_endpoints._
import ua.scala_blog.model._
import ua.scala_blog.repositories.PostsRepository
import monocle.Lens
import monocle.macros.GenLens
import scala.concurrent.{ExecutionContext, Future}
import cats.implicits._


class PostsService(postsRepo: PostsRepository)
                  (implicit ec: ExecutionContext @@ Global,
                   sessionManager: SessionManager[Long]
                  ) extends Service[Posts] with Posts {

  private val postAuthorIdLens = GenLens[PostWithTags](_.post.id)

  private def upsertPost(userId: Long, postWithTags: PostWithTags): Future[StatusCode] = {
    val futureRes = postsRepo.upsert(userId, postAuthorIdLens.set(userId.some)(postWithTags))

    //TODO: notify subscribers

    futureRes
  }

  private def deletePostImpl(userId: Long, postId: Long): Future[StatusCode] = {
    val futureRes = postsRepo.delete(userId, postId)

    //TODO: notify author if admin has deleted his post

    futureRes
  }

  private def upsertComment(userId: Long, comment: Comment): Future[StatusCode] = {
    val futureRes: Future[StatusCode] = ???

    //TODO: notify author about upserting comment to post

    futureRes
  }

  private def deleteCommentImpl(userId: Long, commentId: Long): Future[StatusCode] = {
    val futureRes: Future[StatusCode] = ???

    //TODO: notify comment author if admin has deleted his comment

    futureRes
  }

  override def route: Route = requiredSession(oneOff, usingCookies) { userId =>
    createPost.implementedByAsync(upsertPost(userId, _)) ~
      editPost.implementedByAsync(upsertPost(userId, _)) ~
      deletePost.implementedByAsync(deletePostImpl(userId, _)) ~
      getCommentsWithTags.implementedByAsync(postsRepo.postCommentsAndTags(userId, _)) ~
      addComment.implementedByAsync(upsertComment(userId, _)) ~
      editComment.implementedByAsync(upsertComment(userId, _)) ~
      deleteComment.implementedByAsync(deleteCommentImpl(userId, _))
  } ~ optionalSession(oneOff, usingCookies) { userIdOpt =>
    allPosts.implementedByAsync(postsRepo.findAll(userIdOpt.getOrElse(NoAuth), _))
  }
}
