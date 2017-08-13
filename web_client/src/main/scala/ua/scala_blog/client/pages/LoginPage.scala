package ua.scala_blog.client.pages

import com.thoughtworks.binding.Binding
import com.thoughtworks.binding.Binding.Var
import org.scalajs.dom.{Event, XMLHttpRequest}
import org.scalajs.dom.html.Div
import org.scalajs.dom.document
import com.thoughtworks.binding.dom
import ua.scala_blog.api_endpoints.Authorization.Auth
import fr.hmil.roshttp._

object LoginPage {
  def render(): Binding[Div] = {
    val login = Var("")
    val password = Var("")
    <div>
      <h3>Login:</h3>
      <input type="text" value={login.bind} onChange={login := getValue(_: Event)}/>
      <h3>Password:</h3>
      <input type="password" value={password.bind} onChange={password := getValue(_: Event)}/>
      <button type="button" onclick={e: Event =>}></button>
    </div>
  }
}
