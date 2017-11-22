package speaker

import java.util.Locale

import marytts.LocalMaryInterface
import marytts.modules.synthesis.Voice
import marytts.util.data.audio.AudioPlayer

object Say extends App {

  import javax.sound.sampled.AudioFormat
  import javax.sound.sampled.AudioSystem
  import javax.sound.sampled.DataLine
  import javax.sound.sampled.Line
  import javax.sound.sampled.Mixer

  def displayMixerInfo(): Unit = {
    val mixersInfo = AudioSystem.getMixerInfo
    for (mixerInfo <- mixersInfo) {
      System.out.println("Mixer: " + mixerInfo.getName)
      val mixer = AudioSystem.getMixer(mixerInfo)
      val sourceLineInfo = mixer.getSourceLineInfo
      for (info <- sourceLineInfo) {
        showLineInfo(info)
      }
      val targetLineInfo = mixer.getTargetLineInfo
      for (info <- targetLineInfo) {
        showLineInfo(info)
      }
    }
  }


  private def showLineInfo(lineInfo: Line.Info): Unit = {
    System.out.println("  " + lineInfo.toString)
    if (lineInfo.isInstanceOf[DataLine.Info]) {
      val dataLineInfo = lineInfo.asInstanceOf[DataLine.Info]
      val formats = dataLineInfo.getFormats
      for (format <- formats) {
        System.out.println("    " + format.toString)
      }
    }
  }

  println("Run")

  displayMixerInfo

  println("Loading")
  val marytts = new LocalMaryInterface
  marytts.setLocale(Locale.GERMAN)

//  marytts.setVoice("dfki-pavoque-styles")

  val audioF = marytts.generateAudio(",, Guten morgen... Es ist 7 Uhr 30 .. Aufstehen . Ein schöner neuer Tag erwartet Dich")
  println("Say something")
  //val v = Voice.getVoice("dfki-pavoque-styles")
  //v.dbAudioFormat()
  /*
  val player = new AudioPlayer(audioF)
  player.run()
  */

  val audio = new MyAudioPlayer(audioF, 0, _ => {
    println("sound ended")
  })
  audio.gain(50)
  audio.play()

  Thread.sleep(7000)
}
