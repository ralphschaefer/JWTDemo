package my.jwtdemo.dtos

import my.jwtdemo.profiles.Profile

case class TestOut(profile: Profile, issuer: Option[String],
                   subject: Option[String], expiration: Option[Long],
                   notBefore: Option[Long], issuedAt: Option[Long])
