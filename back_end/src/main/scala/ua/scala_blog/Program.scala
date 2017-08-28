package ua.scala_blog

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives


object Program extends BackEndModule with Directives {
  def main(args: Array[String]): Unit = {
    val interface = config.getString("http.interface")
    val port = config.getInt("http.port")

    val routes = Seq(
      pathPrefix("public")(getFromResourceDirectory("public")),
//      loginService.route,
//      postsService.route,
//      usersSerivce.route,
      indexPage.route
    ).reduce(_ ~ _)

    val binding = Http().bindAndHandle(routes, interface, port)

    println(s"Bound to port $port on interface $interface")
    binding.onFailure {
      case ex: Exception =>
        println(s"[ERROR] Failed to bind to $interface:$port!")
        ex.printStackTrace()
    }

    sys.addShutdownHook {
      for {
        b <- binding
        _ <- b.unbind()
      } system.terminate()
    }
  }
}