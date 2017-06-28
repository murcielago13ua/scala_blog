package ua.scala_blog.model

import io.circe.{Decoder, DecodingFailure, Encoder, HCursor, Json}
import io.circe.syntax._


trait EitherJsonSupport {
  implicit def encodeFoo[A, B](implicit encoderA: Encoder[A], encoderB: Encoder[B]): Encoder[Either[A, B]] = new Encoder[Either[A, B]] {
    final def apply(e: Either[A, B]): Json = e match {
      case Left(a) => a.asJson
      case Right(b) => b.asJson
    }
  }

  implicit def decodeFoo[A, B](implicit decoderA: Decoder[A], decoderB: Decoder[B]): Decoder[Either[A, B]] = new Decoder[Either[A, B]] {
    override def apply(c: HCursor): Decoder.Result[Either[A, B]] = Decoder[B].apply(c) match {
      case _: Left[DecodingFailure, B] => Decoder[A].apply(c) match {
        case ll: Left[DecodingFailure, A] => Left(ll.a)
        case r: Right[DecodingFailure, A] => Right(Left[A, B](r.b))
      }
      case r: Right[DecodingFailure, B] => Right(Right[A, B](r.b))
    }
  }
}
