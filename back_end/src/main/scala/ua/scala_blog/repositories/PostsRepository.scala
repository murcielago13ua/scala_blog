package ua.scala_blog.repositories

import java.time.LocalDateTime
import ua.scala_blog.model.{Tag => TTag, _}
import PostgresProfile.api.{Tag => DbTag, _}
import ua.scala_blog.api_endpoints._
import scala.concurrent.{ExecutionContext, Future}
import com.softwaremill.tagging._


case class TagToPost(tagId: Long, postId: Long)

class TagToPostTable(tag: DbTag) extends Table[TagToPost](tag, "tag_to_post") {
  val tagId = column[Long]("tag_id")
  val postId = column[Long]("post_id")

  def * = (tagId, postId) <> ((TagToPost.apply _).tupled, TagToPost.unapply)

  val pk = primaryKey("tag_to_post_pk", (tagId, postId))
  val tagFk = foreignKey("tag_fk", tagId, TagsTable.query)(_.id)
  val postFk = foreignKey("post_fk", postId, PostTable.query)(_.id)
}

object TagToPostTable {
  val query = TableQuery[TagToPostTable]
}

class PostTable(tag: DbTag) extends GenericTable[Post](tag, "posts") {
  val userId = column[Long]("user_id")
  val dateCreated = column[LocalDateTime]("date_created")
  val title = column[String]("title")
  val text = column[String]("text")

  def * = (userId, dateCreated, title, text, id.?, isDeleted) <> ((Post.apply _).tupled, Post.unapply)

  val userFk = foreignKey("userFK", userId, UserTable.query)(_.id)
}

object PostTable {
  val query = TableQuery[PostTable]
}

class PostsRepository(db: Database)(implicit ec: ExecutionContext @@ Db) extends GenericRepository[Post, PostTable](db, PostTable.query) {

  def findAll(userId: Long, request: Posts.FindRequest): Future[StatusCode Either Posts.PostsWithCount] = {
    val Posts.FindRequest(tags, page, rows, searchQueryOpt) = request

    def filtered(query: Query[PostTable, PostTable#TableElementType, Seq]) = {
      val allPostsQuery = query
        .filter { r =>
          searchQueryOpt match {
            case Some(searchQuery) => r.title like s"%$searchQuery%"
            case None => true.bind
          }
        }
        .join(TagToPostTable.query).on(_.id === _.postId)
        .join(TagsTable.query.filter(_.name inSet tags)).on(_._2.tagId === _.id)
        .map(_._1._1).distinct

      val postsQuery = allPostsQuery
        .drop((page - 1) * rows)
        .take(rows)
        .join(UserTable.query).on(_.userId === _.id)
        .map { case (post, user) => post -> user.login }

      postsQuery.result zip allPostsQuery.size.result
    }

    val query = adminAccessQuery(userId).result flatMap {
      case false => filtered(PostTable.query.filterNot(_.isDeleted))
      case true => filtered(PostTable.query)
    }

    db run query map {
      case (postsWithAuthors, count) => Right {
        Posts.PostsWithCount(postsWithAuthors map (Posts.PostWithAuthorLogin.apply _).tupled,
          count
        )
      }
    }
  }

  def postCommentsAndTags(userId: Long, postId: Long): Future[StatusCode Either Posts.PostCommentsAndTags] = {

    def commentsWithTagsQuery(adminAccess: Boolean) = {
      val tags = TagsTable.query
        .join(TagToPostTable.query.filter(_.postId === postId)).on(_.id === _.tagId)
        .map(_._1)

      val comments = CommentTable.query.filter { r =>
        if (adminAccess) r.postId === postId
        else !r.isDeleted && r.postId === postId
      }

      comments.result zip tags.result
    }

    val query = adminAccessQuery(userId).result flatMap commentsWithTagsQuery

    db run query map (Posts.PostCommentsAndTags.apply _).tupled.andThen(Right(_))
  }

  def upsert(userId: Long, postWithTags: Posts.PostWithTags): Future[StatusCode] = {
    val Posts.PostWithTags(post, tags, tagsToCreate) = postWithTags

    val query = post.id match {
      case Some(postId) =>
        val accessQuery = PostTable.query.filter(r => r.userId === userId && r.id === postId).exists
        accessQuery.result flatMap {
          case false => DBIO.successful(None)
          case true => for {
            _ <- TagToPostTable.query.filter(_.postId === postId).delete
            ids <- TagsTable.query returning TagsTable.query.map(_.id) ++= tagsToCreate.map(TTag(_))
            _ <- TagToPostTable.query ++= (tags.map(_.id.get) ++ ids).map(TagToPost(_, postId))
            res <- PostTable.query.filter(_.id === postId).update(post)
          } yield res
        }

      case None => for {
        ids <- TagsTable.query returning TagsTable.query.map(_.id) ++= tagsToCreate.map(TTag(_))
        postId <- PostTable.query returning PostTable.query.map(_.id) += post
        _ <- TagToPostTable.query ++= (tags.map(_.id.get) ++ ids).map(TagToPost(_, postId))
      } yield 1
    }

    db run query.transactionally map {
      case 1 => StatusCode.Ok
      case _ => StatusCode.AccessDenied
    }
  }

  def delete(userId: Long, postId: Long): Future[StatusCode] = {
    val accessQuery = adminAccessQuery(userId) || PostTable.query.filter(r => r.userId === userId && r.id === postId).exists

    val query = accessQuery.result flatMap {
      case false => DBIO.successful(None)
      case true => for {
        _ <- CommentTable.query.filter(_.postId === postId).map(_.isDeleted).update(true)
        _ <- PostTable.query.filter(_.id === postId).map(_.isDeleted).update(true)
      } yield 1
    }

    db run query.transactionally map {
      case 1 => StatusCode.Ok
      case _ => StatusCode.AccessDenied
    }
  }
}
