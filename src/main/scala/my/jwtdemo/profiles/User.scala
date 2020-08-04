package my.jwtdemo.profiles

case class User(clazz:String, name: String) extends Profile

object User {
  def apply(name:String):User = User(User.getClass.getName, name)
}
