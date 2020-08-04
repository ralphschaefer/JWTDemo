import sbt._

object CirceDependenciesGeneric {
  lazy val circeVersion = "0.13.0"

  lazy val libs = Seq (
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
  )
}


object CirceDependenciesJackson {
  lazy val circeVersion = "0.13.0"

  lazy val libs = Seq (
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-jackson210" % circeVersion,
  )
}
