package ua.scala_blog.model

import io.circe.generic.JsonCodec

@JsonCodec
case class Address(country: String, city: String, street: String, house: String)
