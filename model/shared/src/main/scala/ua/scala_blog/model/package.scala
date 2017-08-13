package ua.scala_blog

import io.circe.{Decoder, Encoder}


package object model extends json.TimeInstances with json.EitherJsonSupport {
  implicit val roleDecoder: Decoder[Roles.Role] = Decoder.enumDecoder(Roles)
  implicit val roleEncoder: Encoder[Roles.Role] = Encoder.enumEncoder(Roles)
}
