package ua.scala_blog.client

import com.thoughtworks.binding.Binding
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.{Event, HTMLInputElement}
import ua.scala_blog.api_endpoints.StatusCode


package object pages {
  implicit def makeIntellijHappy[T <: org.scalajs.dom.raw.Node](x: scala.xml.Node): Binding[T] =
    throw new AssertionError("This should never execute.")

  def getValue(e: Event): String = e.target.asInstanceOf[HTMLInputElement].value

  def loadingPage: Binding[Div] =
    <div>
      <h3>Loading...</h3>
    </div>

  def errorPage(statusCode: StatusCode): Binding[Div] =
    <div>
      <h3>
        Error,
        {statusCode.toString}
      </h3>
    </div>
}
