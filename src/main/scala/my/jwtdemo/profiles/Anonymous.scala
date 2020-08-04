package my.jwtdemo.profiles

case class Anonymous(clazz:String, from: String, anonymous: String) extends Profile

object Anonymous {
  def apply(from:String, name:String):Anonymous = Anonymous(Anonymous.getClass.getName, from, name)
}
