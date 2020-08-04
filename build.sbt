organization := "my.jwtdemo"

scalaVersion := "2.13.2"

version := "0.1"

name := "JWTDemo"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "my.jwtdemo",
      test in assembly := {},
    )),
    name := "JWTDemo",
    assemblyJarName in assembly := name.value + ".jar",
    libraryDependencies ++= 
      CirceDependenciesGeneric.libs ++
      AkkaDependencies.libs ++
      Seq(
     	"ch.qos.logback" % "logback-classic" % "1.2.3",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      	"org.scalatest" %% "scalatest" % "3.0.8" % Test
    )
  )

