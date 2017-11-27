package speaker


import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.{HttpApp, Route}
import speaker.WebServer.Speaker.Say

/**
  * Example: curl -X POST -H "Content-Type: text/plain" --data "this is raw data" http://localhost:9100/say
  */

object WebServer extends HttpApp {

  class Speaker extends Actor with ActorLogging {
    override def receive: Receive = {
      case Say(text) => println("saying "+text)
    }
  }

  object Speaker {
    case class Say(text:String)
  }

  lazy val speaker = systemReference.get.actorOf(Props[Speaker])

  override def routes: Route =
    path("say") {
      post {
        entity(as[String]) { body =>
          complete{
            speaker ! Say(body)
            HttpEntity.Empty
          }
        }
      }
    }
}


object Process extends App {
  println("Speaker version 0.0.0")
  WebServer.startServer("localhost", 9100)
}
