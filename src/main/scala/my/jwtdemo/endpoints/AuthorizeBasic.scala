package my.jwtdemo.endpoints

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.directives.Credentials
import com.typesafe.scalalogging.StrictLogging
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import my.jwtdemo.JWT
import my.jwtdemo.contextMock.UserPass
import my.jwtdemo.helper.Hasher
import my.jwtdemo.profiles.User

import scala.util.{Failure, Success, Try}


class AuthorizeBasic(val jwt: JWT) extends Directives with StrictLogging {

  import FailFastCirceSupport._
  import my.jwtdemo.dtos.AuthOut


  def authenticator(credentials: Credentials): Try[User] = Try {
    credentials match {
      case p@Credentials.Provided(username) =>
        UserPass.read(username) match {
          case Success((user, pwhash)) if p.verify(pwhash, Hasher.apply) => user
          case Failure(e) => throw e
        }
      case _ => throw AuthBasicException("missing Credentials")
    }
  }

  def authenticatorOption(credentials: Credentials): Option[User] = {
    authenticator(credentials) match {
      case Success(user) => Some(user)
      case Failure(e) =>
        logger.info(s"login failed: '${e.getMessage}'")
        None
    }
  }

  case class AuthBasicException(msg:String) extends Exception(msg)

  def route = {
    get {
      authenticateBasic(realm = "basic auth", authenticatorOption) { user =>
        complete(AuthOut(jwt.encode(user)))
      }
    }
  }

}
