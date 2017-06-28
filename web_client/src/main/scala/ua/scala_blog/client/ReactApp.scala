package ua.scala_blog.client

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom._


@JSExport("ReactApp")
object ReactApp extends JSApp {
  val HelloMessage = ScalaComponent.builder[String]("HelloMessage")
    .render { $ =>
      <.div("Hello ", $.props)
    }
    .build

  @JSExport
  def main(): Unit = {
    HelloMessage("world!").renderIntoDOM(document.body)
  }
}
