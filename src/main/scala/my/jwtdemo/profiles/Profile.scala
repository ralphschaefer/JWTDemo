package my.jwtdemo.profiles


import cats.syntax.functor._
import io.circe.{ Decoder, Encoder }, io.circe.generic.auto._
import io.circe.syntax._


object Profile{

  implicit val encodeEvent: Encoder[Profile] = Encoder.instance {
    case user @ User(_,_) => user.asJson
    case an @ Anonymous(_,_,_) => an.asJson
  }

  implicit val decodeEvent: Decoder[Profile] =
    List[Decoder[Profile]](
      Decoder[User].widen,
      Decoder[Anonymous].widen
    ).reduceLeft(_ or _)

}

trait Profile {
  val clazz:String
}