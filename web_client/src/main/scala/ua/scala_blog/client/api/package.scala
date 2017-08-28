package ua.scala_blog.client


import endpoints.xhr._
import ua.scala_blog.api_endpoints.Authorization.Auth
import ua.scala_blog.api_endpoints._
import ua.scala_blog.model.{Comment, Post, Roles, User}
import java.time._

import ua.scala_blog.api_endpoints.Posts._

import scala.concurrent.Future
import scala.util.Random


package object api {
  //  object PostsApi extends Posts with CirceEntities with thenable.Endpoints
  //  object LoginApi extends Authorization with CirceEntities with thenable.Endpoints
  object LoginApi {
    private var users: Seq[User] = Seq(User("admin", "", "admin", Roles.Admin, LocalDate.now, None, None, None, None, None))

    def login(auth: Auth): Future[StatusCode] = users.find(u => u.login == auth.login && u.password == auth.password) match {
      case Some(_) => Future.successful(StatusCode.Ok)
      case _ => Future.successful(StatusCode.AccessDenied)
    }

    def logout(): Future[StatusCode] = Future.successful(StatusCode.Ok)

    def signUp(user: User): Future[StatusCode] = {
      users :+= user
      Future.successful(StatusCode.Ok)
    }
  }

  object PostsApi {
    private var posts: Seq[Post] = Seq(
      Post(-1, LocalDateTime.now, "Post #1", "tesxtshgd............", Some(1L)),
      Post(-1, LocalDateTime.now, "Post #2", "tesxtshgd............", Some(2L)),
      Post(-1, LocalDateTime.now, "Post #3", "tesxtshgd............", Some(3L)),
      Post(-1, LocalDateTime.now, "Post #4", "tesxtshgd............", Some(4L)),
      Post(-1, LocalDateTime.now, "Post #5", "tesxtshgd............", Some(5L)),
      Post(-1, LocalDateTime.now, "Post #6", "tesxtshgd............", Some(6L)),
      Post(-1, LocalDateTime.now, "Post #7", "tesxtshgd............", Some(7L)),
      Post(-1, LocalDateTime.now, "Post #8", "tesxtshgd............", Some(8L)),
      Post(-1, LocalDateTime.now, "Post #9", "tesxtshgd............", Some(9L))
    )

    def allPosts(request: FindRequest): Future[StatusCode Either PostsWithCount] = {
      val FindRequest(_, page, rows, _) = request
      val res = posts
        .drop((page - 1) * rows)
        .take(rows)
        .map(PostWithAuthorLogin(_, "***"))

      Future.successful(Right(PostsWithCount(res, posts.size)))
    }

    def view(postId: Long): Future[StatusCode Either Post] = {
      Future.successful(posts.find(_.id.contains(postId)).map(Right(_)).getOrElse(Left(StatusCode.NotFound)))
    }

    def createPost(p: PostWithTags): Future[StatusCode] = ???

    //    val editPost = endpoint(request(Post, root, jsonRequest[PostWithTags]), jsonResponse[StatusCode])
    //    val deletePost = endpoint(request(Delete, root / segment[PostId]), jsonResponse[StatusCode])
    //
    //    val getCommentsWithTags = endpoint(request(Get, comments / segment[PostId]), jsonResponse[StatusCode Either PostCommentsAndTags])
    //    val addComment = endpoint(request(Put, comments, jsonRequest[Comment]), jsonResponse[StatusCode])
    //    val editComment = endpoint(request(Post, comments, jsonRequest[Comment]), jsonResponse[StatusCode])
    //    val deleteComment = endpoint(request(Delete, comments / segment[CommentId]), jsonResponse[StatusCode])
  }
}
