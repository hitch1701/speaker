package speaker

import java.util.Locale

import marytts.LocalMaryInterface

object Say extends App {
  println("Loading")
  val marytts = new LocalMaryInterface
  marytts.setLocale(Locale.GERMAN)

  val audioF = marytts.generateAudio(",, Guten morgen... Es ist 7 Uhr 30 .. Aufstehen . Ein schöner neuer Tag erwartet Dich")
  println("Say something")

  val audio = new AudioPlayer(audioF, 0, _ => {
    println("sound ended")
  })
  audio.gain(50)
  audio.play()

  Thread.sleep(7000)
}
