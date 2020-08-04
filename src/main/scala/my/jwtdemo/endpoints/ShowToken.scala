package my.jwtdemo.endpoints

import akka.http.scaladsl.server.Directives
import com.typesafe.scalalogging.StrictLogging
import my.jwtdemo.profiles.Profile
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import my.jwtdemo.dtos.TestOut
import pdi.jwt.JwtClaim

class ShowToken extends Directives with StrictLogging{

 import FailFastCirceSupport._

 def route(jwtClaim: JwtClaim, profile: Profile) = {
    get {
      complete(TestOut(
        profile = profile, issuer = jwtClaim.issuer, subject = jwtClaim.subject,
        expiration = jwtClaim.expiration, notBefore = jwtClaim.notBefore,
        issuedAt = jwtClaim.issuedAt
      ))
    }
  }

}
