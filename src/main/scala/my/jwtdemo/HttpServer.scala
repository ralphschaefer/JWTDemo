package my.jwtdemo

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Directives._
import akka.stream.SystemMaterializer
import com.typesafe.scalalogging.StrictLogging
import my.jwtdemo.endpoints.{AuthorizeAnonymous, AuthorizeBasic, ShowToken}

import scala.io.StdIn

class HttpServer extends Directives with JWT.JWTDirective with StrictLogging {

  import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._

  implicit val system = ActorSystem("System")
  implicit val materializer = SystemMaterializer(system)
  implicit val executionContext = system.dispatcher

  val jwt = new JWT("mySecretKey")

  val authorizeBasic = new AuthorizeBasic(jwt)
  val showToken = new ShowToken
  val authorizeAnonymous = new AuthorizeAnonymous(jwt)

  val route =
    pathEndOrSingleSlash {
      complete(
        <body>
          <h2>JWT Demo</h2>
          <a href="/static/index.html">simple test</a>
        </body>
      )
    } ~
    pathPrefix("api" / "v1") {
      path("test") {
        extractJwt(jwt) { (jwtClaim, profile) =>
          extractRequest { req =>
            logger.info(s"GET - ${req.uri.path.toString}")
            showToken.route(jwtClaim ,profile)
          }
        }
      }
    } ~
    pathPrefix("static") {
      extractRequest { req =>
        logger.info(s"static resource ${req.uri.path.toString()}")
        getFromResourceDirectory("static")
      }
    } ~
    path ("basicauth") {
      authorizeBasic.route
    } ~
    path ("anyone"){
      authorizeAnonymous.route
    }


  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
