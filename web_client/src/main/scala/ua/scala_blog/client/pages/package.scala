package ua.scala_blog.client


import com.thoughtworks.binding.Binding
import org.scalajs.dom.EventTarget
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.{Event, HTMLInputElement}
import ua.scala_blog.api_endpoints.StatusCode
import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react._


package object pages {
  sealed trait Pages
  object Pages {
    case object Login extends Pages
    case object Posts extends Pages
    case class PostDetails(postId: Long) extends Pages
  }
}
