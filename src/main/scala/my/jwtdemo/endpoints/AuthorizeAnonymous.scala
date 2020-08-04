package my.jwtdemo.endpoints

import akka.http.scaladsl.server.Directives
import com.typesafe.scalalogging.StrictLogging
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import my.jwtdemo.JWT
import my.jwtdemo.dtos.AnonymousName

class AuthorizeAnonymous(val jwt: JWT) extends Directives with StrictLogging {

  import FailFastCirceSupport._
  import my.jwtdemo.dtos.AuthOut
  import my.jwtdemo.profiles.Anonymous

  def route = {
    post { // not 100% rest conform ..
      extractRequest { req => {
        extractHost { host =>
          entity(as[AnonymousName]) { anonymous =>
            logger.info(s"POST - ${req.uri.path.toString}")
            complete(AuthOut(jwt.encode(Anonymous(host, anonymous.name),anonymous.expiration)))
          }
        }
      } }
    }
  }

}
