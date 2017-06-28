package ua.scala_blog.api_endpoints

import endpoints.algebra.CirceEntities
import ua.scala_blog.model._
import io.circe.generic.JsonCodec
import Posts._


trait Posts extends CirceEntities {
  val root = path / "posts"
  val comments = root / "comments"

  type PostId = Long
  type CommentId = Long

  val allPosts = endpoint(request(Post, root / "all", jsonRequest[FindRequest]), jsonResponse[StatusCode Either PostsWithCount])
  val createPost = endpoint(request(Put, root, jsonRequest[PostWithTags]), jsonResponse[StatusCode])
  val editPost = endpoint(request(Post, root, jsonRequest[PostWithTags]), jsonResponse[StatusCode])
  val deletePost = endpoint(request(Delete, root / segment[PostId]), jsonResponse[StatusCode])

  val getCommentsWithTags = endpoint(request(Get, comments / segment[PostId]), jsonResponse[StatusCode Either PostCommentsAndTags])
  val addComment = endpoint(request(Put, comments, jsonRequest[Comment]), jsonResponse[StatusCode])
  val editComment = endpoint(request(Post, comments, jsonRequest[Comment]), jsonResponse[StatusCode])
  val deleteComment = endpoint(request(Delete, comments / segment[CommentId]), jsonResponse[StatusCode])
}

object Posts {
  @JsonCodec
  case class PostWithAuthorLogin(post: Post, authorLogin: String)

  @JsonCodec
  case class PostWithTags(post: Post, tags: Seq[Tag], tagsToCreate: Seq[String])

  @JsonCodec
  case class PostCommentsAndTags(comments: Seq[Comment], tags: Seq[Tag])

  @JsonCodec
  case class PostsWithCount(posts: Seq[PostWithAuthorLogin], count: Int)

  @JsonCodec
  case class FindRequest(tags: Seq[String], page: Int, rows: Int, searchQueryOpt: Option[String])
}
