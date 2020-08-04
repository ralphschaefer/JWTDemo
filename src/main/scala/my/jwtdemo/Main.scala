package my.jwtdemo

import com.typesafe.scalalogging.StrictLogging

object Main extends App with StrictLogging {
  logger.info("JWT Demo")

  new HttpServer
}

