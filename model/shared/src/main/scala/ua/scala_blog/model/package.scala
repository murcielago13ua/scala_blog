package ua.scala_blog

import io.circe.{Decoder, Encoder}
import io.circe.java8.time.TimeInstances


package object model extends TimeInstances with EitherJsonSupport {
  implicit val roleDecoder: Decoder[Roles.Role] = Decoder.enumDecoder(Roles)
  implicit val roleEncoder: Encoder[Roles.Role] = Encoder.enumEncoder(Roles)
}
