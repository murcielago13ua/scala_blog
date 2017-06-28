package ua.scala_blog.repositories

import ua.scala_blog.model
import model.{Tag => _, _}
import PostgresProfile.api._


abstract class GenericTable[E <: GenericEntity](tag: Tag, name: String) extends Table[E](tag, name) {
  val id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  val isDeleted = column[Boolean]("is_deleted")
}

abstract class GenericRepository[E <: GenericEntity, ETable <: GenericTable[E]](val db: Database,
                                                                                val tableQ: TableQuery[ETable]
                                                                               ) {
  protected final def adminAccessQuery(userId: Long) = UserTable.query
    .filter { r => r.id === userId && r.role === (Roles.Admin: Roles.Role) }
    .exists
}
