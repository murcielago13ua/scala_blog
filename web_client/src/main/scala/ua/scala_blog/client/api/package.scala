package ua.scala_blog.client

import endpoints.xhr._
import ua.scala_blog.api_endpoints._


package object api {
  object PostsApi extends Posts with CirceEntities with thenable.Endpoints
  object LoginApi extends Authorization with CirceEntities with thenable.Endpoints
}
