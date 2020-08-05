package my.jwtdemo

import java.time.Clock

import akka.http.javadsl.server.directives.BasicDirectives
import akka.http.scaladsl.server.{AuthorizationFailedRejection, Directive, Directive1, RequestContext, ValidationRejection}
import com.typesafe.scalalogging.StrictLogging
import io.circe.syntax._
import my.jwtdemo.profiles.{Profile, User}
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import scala.util.{Failure, Success, Try}

object JWT {
  val expirationTime = 60*60*2

  trait JWTDirective extends StrictLogging {

    val jwtHeader = "jwt"

    def extractJwt(jwt: JWT): Directive[(JwtClaim,Profile)] = Directive[(JwtClaim,Profile)] { inner => ctx =>
      ctx.request.headers.find(h => h.lowercaseName() == jwtHeader) match {
        case Some(token) => jwt.decode(token.value()) match {
          case Success((claim,content)) =>
            inner( (claim,content) )(ctx)
          case Failure(exception) =>
            logger.error(exception.toString)
            ctx.reject(AuthorizationFailedRejection)
        }
        case None =>
          ctx.reject(ValidationRejection(s"missing '$jwtHeader' header"))
      }
    }

  }
}

class JWT(val secretKey: String) {

  import io.circe.generic.auto._

  implicit val clock: Clock = Clock.systemUTC

  def encode(content: Profile, expiration: Boolean = true) = {
    val algo = JwtAlgorithm.HS256
    val claimJson = JwtClaim()
      .issuedNow
      .startsNow
      .withContent(content.asJson.noSpaces)
    if (expiration)
      JwtCirce.encode(claimJson.expiresIn(JWT.expirationTime), secretKey, algo)
    else
      JwtCirce.encode(claimJson, secretKey, algo)
  }

  def decode(jwtToken: String): Try[(JwtClaim,Profile)] = Try {
    import io.circe.parser.{decode => ciceDecode}
    JwtCirce.decode(jwtToken, secretKey, Seq(JwtAlgorithm.HS256)) match {
      case Success(claim) =>
        ciceDecode[Profile](claim.content) match {
          case Right(content) =>
            (claim, content)
          case Left(error) =>
            throw error
        }
      case Failure(exception) =>
        throw exception
    }
  }

}
