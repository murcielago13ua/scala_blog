package ua.scala_blog.client

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import com.thoughtworks.binding.{Binding, FutureBinding, dom}
import com.thoughtworks.binding.Binding._
import org.scalajs.dom.raw._
import org.scalajs.dom.html._
import org.scalajs.dom.document
import ua.scala_blog.model._
import ua.scala_blog.api_endpoints.Posts._
import ua.scala_blog.api_endpoints.StatusCode
import ua.scala_blog.client.api.{LoginApi, PostsApi}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scalajs.js.Thenable.Implicits._


@JSExportTopLevel("ReactApp")
object ReactApp extends JSApp {
  @dom
  def postWithAuthour(post: Post, authorLogin: String): Binding[Div] = {
    <div>
      <table>
        <tr>
          <th>
            {post.title}
            (author:
            {authorLogin}
            )</th>
          <th>posted:
            {post.dateCreated.toString}
          </th>
        </tr>
        <tr>
          <td>
            {post.text}
          </td>
        </tr>
      </table>
    </div>
  }

  @dom
  def postsPage: Binding[Div] = {
    val postsFuture: FutureBinding[StatusCode Either PostsWithCount] = FutureBinding(PostsApi.allPosts(FindRequest(Seq.empty, 1, 5, None)))
    postsFuture.bind match {
      case None =>
        <div>
          <h2>Loading...</h2>
        </div>

      case Some(Success(Left(sc))) =>
        <div>
          <h2>
            {sc.toString}
          </h2>
        </div>

      case Some(Success(Right(PostsWithCount(postsWithAuthor, count)))) =>
        <div>
          <div>
            {for (PostWithAuthorLogin(post, authorLogin) <- Constants(postsWithAuthor: _*)) yield postWithAuthour(post, authorLogin).bind}
          </div>
          <p>Posts
            {postsWithAuthor.size.toString}
            of
            {count.toString}
          </p>
        </div>

      case Some(Failure(e)) =>
        <div>
          <h2>Failure:
            {e.getMessage}
          </h2>
        </div>
    }
  }

  case class Auth(login: Var[String], password: Var[String])

  def getValue(e: Event): String = e.target.asInstanceOf[HTMLInputElement].value

  @dom
  def loginPage(mainPage: Binding[Div]): Binding[Div] = {
    val auth = Auth(Var(""), Var(""))
    <div>
      <h3>Login: </h3>
      <input type="text" value={auth.login.bind} onChange={auth.login := getValue(_: Event)}/>
      <h3>Password: </h3>
      <input type="password" value={auth.password.bind} onChange={auth.password := getValue(_: Event)}/>
      <button type="button" onclick={e: Event =>

      }></button>
    </div>
  }

  @JSExport
  def main(): Unit = {
    dom.render(document.body, postsPage)
  }
}