package ua.scala_blog.repositories

import com.github.tminglei.slickpg._
import io.circe.{Decoder, Encoder, Json}
import slick.basic.Capability
import slick.jdbc.JdbcCapabilities
import ua.scala_blog.model
import ua.scala_blog.model.{Address, Roles}
import io.circe.syntax._
import scala.reflect.ClassTag
import io.circe.parser._


trait PostgresProfile extends ExPostgresProfile
                              with PgArraySupport
                              with PgDate2Support
                              with PgEnumSupport
                              with PgCirceJsonSupport {

  def pgjson = "jsonb" // jsonb support is in postgres 9.4.0 onward; for 9.3.x use "json"

  // Add back `capabilities.insertOrUpdate` to enable native `upsert` support; for postgres 9.5+
  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + JdbcCapabilities.insertOrUpdate

  override val api = MyAPI

  object MyAPI extends API
                       with ArrayImplicits
                       with DateTimeImplicits
                       with CirceImplicits
                       with CirceJsonPlainImplicits {

    implicit val roleTypeMapper = createEnumJdbcType("role", Roles)

    def a2Json[A](implicit encoder: Encoder[A], decoder: Decoder[A], ctg: ClassTag[A]) =
      MappedColumnType.base[A, Json](_.asJson, _.as[A].right.get)

    implicit val address2Json = a2Json[Address]
  }

}

object PostgresProfile extends PostgresProfile
