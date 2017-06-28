package ua.scala_blog.api_endpoints

import io.circe.generic.JsonCodec


@JsonCodec sealed trait StatusCode {val code: Int}
object StatusCode {
  case object Ok extends StatusCode {val code = 200}
  case object BadRequest extends StatusCode {val code = 400}
  case object AccessDenied extends StatusCode {val code = 403}
  case object NotFound extends StatusCode {val code = 404}
  case object RequestTimeout extends StatusCode {val code = 407}
  case object InternalServerError extends StatusCode {val code = 1414}
}
