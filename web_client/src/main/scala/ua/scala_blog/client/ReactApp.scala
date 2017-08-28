package ua.scala_blog.client


import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra._
import japgolly.scalajs.react.extra.router._
import ua.scala_blog.client.pages.{LoginComponent, Pages, PostDetailsComponent, PostsComponent}
import org.scalajs.dom._
import japgolly.scalajs.react.vdom.Implicits._


@JSExportTopLevel("ReactApp")
object ReactApp extends JSApp {
  val routerConfig = RouterConfigDsl[Pages].buildConfig { dsl =>
    import dsl._
    (
      staticRoute(root, Pages.Posts) ~> renderR(PostsComponent(_)) |
      dynamicRouteCT("post" / long.caseClass[Pages.PostDetails]) ~> dynRenderR { (postDet: Pages.PostDetails, ctl) =>
        PostDetailsComponent(ctl, postDet.postId)
      }
    ).notFound(redirectToPage(Pages.Posts)(Redirect.Replace))
  }

  val router = Router(BaseUrl.fromWindowOrigin_/, routerConfig.logToConsole)

  @JSExport
  def main(): Unit = {
    router().renderIntoDOM(document.body)
  }
}