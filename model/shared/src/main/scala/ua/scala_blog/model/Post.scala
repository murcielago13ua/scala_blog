package ua.scala_blog.model

import io.circe.generic.JsonCodec
import java.time._


@JsonCodec
case class Post(userId: Long,
                dateCreated: LocalDateTime,
                title: String,
                text: String,
                id: Option[Long] = None, isDeleted: Boolean = false) extends GenericEntity
