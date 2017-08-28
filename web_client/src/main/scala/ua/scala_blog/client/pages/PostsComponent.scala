package ua.scala_blog.client.pages


import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.raw.ReactElement
import ua.scala_blog.client.api.PostsApi
import ua.scala_blog.api_endpoints.Posts._
import ua.scala_blog.api_endpoints.StatusCode

import scala.concurrent.ExecutionContext.Implicits.global


object PostsComponent {
  sealed trait State
  case object Loading extends State
  case class Error(st: StatusCode) extends State
  case class Posts(posts: Seq[PostWithAuthorLogin], count: Int, page: Int) extends State

  case class Props(ctl: RouterCtl[Pages])

  class Backend($: BackendScope[Props, State]) {
    def render(p: Props, s: State): VdomElement = {
      val content = s match {
        case Loading => div(h2("Loading..."))
        case Error(st) => div(h2(s"$st"))
        case Posts(posts, count, page) =>
          div(
            posts.map { case PostWithAuthorLogin(post, authorLogin) =>
              div(
                h4(post.title),
                h5(s"${post.dateCreated} by $authorLogin"),
                button("details", onClick --> viewDetails(p, post.id.get))
              )
            }.toVdomArray,
            div(
              h3(s"Page $page"),
              if (page == 1) button("->", onClick --> changePage(2))
              else Seq(
                button("<-", onClick --> changePage(page - 1)),
                button("->", onClick --> changePage(page + 1))
              ).toVdomArray
            )
          )

      }
      div(
        LoginComponent(),
        content
      )
    }

    def load: Callback = changePage(1)

    private def changePage(p: Int): Callback = {
      val postsFuture = PostsApi.allPosts(FindRequest(Nil, p, 5, None))
      Callback.future(postsFuture.map {
        case Left(st) => $.setState(Error(st))
        case Right(PostsWithCount(posts, count)) => $.setState(Posts(posts, count, p))
      })
    }

    private def viewDetails(p: Props, postId: Long): Callback = {
      p.ctl.set(Pages.PostDetails(postId))
    }
  }

  private val component = ScalaComponent.builder[Props]("posts_component")
    .initialState(Loading: State)
    .renderBackend[Backend]
    .componentDidMount(_.backend.load)
    .build


  def apply(ctl: RouterCtl[Pages]) = component(Props(ctl))
}
