package my.jwtdemo.contextMock

import my.jwtdemo.helper.Hasher
import my.jwtdemo.profiles.User

import scala.util.Try

object UserPass {

  val users= Map(
    "admin" -> "admin",
    "user" -> "pass",
    "hans" -> "wurst"
  )

  def read(username: String): Try[(User,String)] = Try {
    val pwhash = users.getOrElse(username, throw UserPassException(s"No such user '$username'"))
    (User(username), Hasher(pwhash))
  }

  case class UserPassException(msg:String) extends Exception(msg)

}
