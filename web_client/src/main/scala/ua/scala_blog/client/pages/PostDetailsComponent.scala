package ua.scala_blog.client.pages


import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.raw.ReactElement
import ua.scala_blog.client.api.PostsApi
import ua.scala_blog.api_endpoints.Posts._
import ua.scala_blog.api_endpoints.StatusCode
import ua.scala_blog.model.Post

import scala.concurrent.ExecutionContext.Implicits.global

object PostDetailsComponent {
  case class Props(ctl: RouterCtl[Pages], postId: Long)

  sealed trait State
  case object Loading extends State
  case class Error(st: StatusCode) extends State
  case class Details(post: Post) extends State

  class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement = {
      val content = s match {
        case Loading => div(h2("Loading..."))
        case Error(st) => div(h2(s"$st"))
        case Details(post) => div(
          h2(post.title),
          h3(s"${post.dateCreated}"),
          h3(post.text)
        )
      }
      div(
        LoginComponent(),
        button("<=", onClick --> p.ctl.set(Pages.Posts)),
        content
      )
    }

    def load(postId: Long): Callback = {
      Callback.future(PostsApi.view(postId).map {
        case Left(st) => $.setState(Error(st))
        case Right(post) => $.setState(Details(post))
      })
    }
  }

  private val component = ScalaComponent.builder[Props]("posts_component")
    .initialState(Loading: State)
    .renderBackend[Backend]
    .componentDidMount($ => $.backend.load($.props.postId))
    .build


  def apply(ctl: RouterCtl[Pages], postId: Long) = component(Props(ctl, postId))
}
