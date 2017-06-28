package ua.scala_blog.model

import io.circe.generic.JsonCodec

@JsonCodec
case class Subscription(userId: Long,
                        tagId: Long,
                        id: Option[Long] = None,
                        isDeleted: Boolean = false
                       ) extends GenericEntity
