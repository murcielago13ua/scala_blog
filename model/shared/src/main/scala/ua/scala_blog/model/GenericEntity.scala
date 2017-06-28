package ua.scala_blog.model

trait GenericEntity {
  val id: Option[Long]
  val isDeleted: Boolean
}
