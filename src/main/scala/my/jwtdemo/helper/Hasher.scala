package my.jwtdemo.helper

import java.security.MessageDigest

object Hasher {

  def apply(str:String):String = {
    MessageDigest.getInstance("SHA-512")
      .digest(str.getBytes("UTF-8"))
      .map("%02x".format(_)).mkString
  }

}
