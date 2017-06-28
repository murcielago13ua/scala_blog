package ua.scala_blog.model

import io.circe.generic.JsonCodec

@JsonCodec
case class Tag(name: String,
               id: Option[Long] = None,
               isDeleted: Boolean = false) extends GenericEntity
