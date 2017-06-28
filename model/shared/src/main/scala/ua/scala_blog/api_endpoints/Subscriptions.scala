package ua.scala_blog.api_endpoints

import endpoints.algebra.CirceEntities
import ua.scala_blog.model._


trait Subscriptions extends CirceEntities {
  val root = path / "subscriptions"

  type UserId = Long
  type SubscriptionId = Long

  val all = endpoint(request(Get, root / segment[UserId]), jsonResponse[Seq[Subscription]])
  val create = endpoint(request(Put, root, jsonRequest[Subscription]), jsonResponse[StatusCode])
  val delete = endpoint(request(Delete, root / segment[SubscriptionId]), jsonResponse[StatusCode])
}
