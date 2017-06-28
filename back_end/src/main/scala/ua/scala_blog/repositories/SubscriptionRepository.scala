package ua.scala_blog.repositories

import ua.scala_blog.model
import model.{Tag => _, _}
import PostgresProfile.api._

class SubscriptionTable(tag: Tag) extends GenericTable[Subscription](tag, "subscriptions") {
  override val id = column[Long]("id")
  val userId = column[Long]("user_id", O.PrimaryKey)
  val tagId = column[Long]("tag_id", O.PrimaryKey)

  def * = (userId, tagId, id.?, isDeleted) <> ((Subscription.apply _).tupled, Subscription.unapply)

  val userFk = foreignKey("user_fk", userId, UserTable.query)(_.id)
  val tagFk = foreignKey("tag_fk", tagId, TagsTable.query)(_.id)
}

object SubscriptionTable {
  val query = TableQuery[SubscriptionTable]
}

class SubscriptionRepository(db: Database)
  extends GenericRepository[Subscription, SubscriptionTable](db, SubscriptionTable.query){


}
