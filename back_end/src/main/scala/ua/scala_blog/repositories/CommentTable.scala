package ua.scala_blog.repositories

import java.time.LocalDateTime
import ua.scala_blog.model.{Tag => _, _}
import PostgresProfile.api._


class CommentTable(tag: Tag) extends GenericTable[Comment](tag, "comments") {
  val postId = column[Long]("post_id")
  val userId = column[Long]("user_id")
  val text = column[String]("text")
  val dateCreated = column[LocalDateTime]("date_created")

  def * = (postId, userId, text, dateCreated, id.?, isDeleted) <> ((Comment.apply _).tupled, Comment.unapply)

  val postFk = foreignKey("post_fk", postId, PostTable.query)(_.id)
  val userFk = foreignKey("user_fk", userId, UserTable.query)(_.id)
}

object CommentTable {
  val query = TableQuery[CommentTable]
}
