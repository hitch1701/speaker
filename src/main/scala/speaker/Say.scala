package speaker

import java.io.{ByteArrayOutputStream, FileOutputStream, InputStream, OutputStream}
import java.util.Locale
import javax.sound.sampled.{AudioFileFormat, AudioInputStream, AudioSystem}

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{HttpApp, Route}
import marytts.LocalMaryInterface
import speaker.WebServer.Speaker.Say
import sun.audio.AudioPlayer

/**
  * Example: curl -X POST -H "Content-Type: text/plain" --data "this is raw data" http://localhost:9100/say
  */
object CopyStreams {
  type Bytes = Int
  def apply(input: InputStream, output: ByteArrayOutputStream, chunkSize: Bytes = 1024) = {
    val buffer = Array.ofDim[Byte](chunkSize)
    var count = -1
    while ({count = input.read(buffer); count > 0})
      output.write(buffer, 0, count)
    output
  }
}

object WebServer extends HttpApp {
  val marytts = new LocalMaryInterface
  marytts.setLocale(Locale.GERMAN)

  class Speaker extends Actor with ActorLogging {
    override def receive: Receive = {
      case Say(text) =>
        val audioStream = marytts.generateAudio(text)
        sender ! audioStream
    }
  }

  object Speaker {
    case class Say(text:String)
  }

  lazy val speaker = systemReference.get.actorOf(Props[Speaker])
  import scala.concurrent.duration._
  import akka.util.Timeout
  implicit val duration: Timeout = 60 seconds

  override def routes: Route =
    path("say") {
      post {
        entity(as[String]) { body =>
          onSuccess((speaker ? Say(body)).mapTo[AudioInputStream]) { audioStream =>
            complete {
              val wave = new ByteArrayOutputStream()
              AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, wave)
              HttpEntity(wave.toByteArray)
            }
          }
        }
      }
    }
}


object Process extends App {
  println("Speaker version 0.0.0")
  WebServer.startServer("localhost", 9100)
}
