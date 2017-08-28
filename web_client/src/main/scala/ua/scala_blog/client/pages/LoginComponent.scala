package ua.scala_blog.client.pages


import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react._
import ua.scala_blog.api_endpoints.Authorization.Auth
import ua.scala_blog.client.api.LoginApi


object LoginComponent {
  case class State(login: String, password: String)

  class Backend($: BackendScope[Unit, State]) {
    def render(s: State) : VdomElement = {
      div(
        label("login"),
        input(
          value := s.login,
          onChange ==> { (e: ReactEventFromInput) =>
            val updatedLogin = e.target.value
            $.modState(_.copy(login = updatedLogin))
          }
        ),
        br(),
        label("password"),
        input.password(
          value := s.password,
          onChange ==> { (e: ReactEventFromInput) =>
            val updatedPassword = e.target.value
            $.modState(_.copy(password = updatedPassword))
          }
        ),
        br(),
        button("login", onClick --> login(s))
      )
    }

    private def login(s: State): Callback = {
      Callback.alert(s"Login: ${s.login}, password: ${s.password}")
    }
  }

  private val component = ScalaComponent.builder[Unit]("login_component")
    .initialState(State("", ""))
    .renderBackend[Backend]
    .build

  def apply() = component()
}
