package ua.scala_blog.repositories

import ua.scala_blog.model
import PostgresProfile.api._


class TagsTable(tag: Tag) extends GenericTable[model.Tag](tag, "tags") {
  val name = column[String]("name", O.Unique)

  def * = (name, id.?, isDeleted) <> ((model.Tag.apply _).tupled, model.Tag.unapply)
}

object TagsTable {
  val query = TableQuery[TagsTable]
}
