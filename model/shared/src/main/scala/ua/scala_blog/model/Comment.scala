package ua.scala_blog.model

import io.circe.generic.JsonCodec
import java.time._

@JsonCodec
case class Comment(postId: Long, userId: Long,
                   text: String,
                   dateCreated: LocalDateTime,
                   id: Option[Long] = None,
                   isDeleted: Boolean = false) extends GenericEntity
