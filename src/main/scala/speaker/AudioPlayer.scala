package speaker

import javax.sound.sampled._

class AudioPlayer(audioStream: AudioInputStream, initialGain: Int, whenDone: AudioPlayer => Unit) extends LineListener {
  var playCompleted = false
  val format = audioStream.getFormat
  val defaultFormat = new AudioFormat(44100, 16, 2, true, true)
  val info = new DataLine.Info(classOf[Clip], defaultFormat)
  val audioClip = AudioSystem.getLine(info).asInstanceOf[Clip]

  import javax.sound.sampled.AudioSystem

  val currentAudioStream = AudioSystem.getAudioInputStream(defaultFormat, audioStream)
  audioClip.open(currentAudioStream)
  audioClip.addLineListener(this)
  val masterGain = audioClip.getControl(FloatControl.Type.MASTER_GAIN).asInstanceOf[FloatControl]
  gain(initialGain)

  def close = {
    gain(0)
    audioClip.stop()
    audioClip.close()
  }

  def play(): AudioPlayer = {
    println("playing...")
    try {
      audioClip.setFramePosition(0)
      audioClip.start()
      this
    } catch {
      case ex: Exception =>
        ex.printStackTrace()
        this
    }
  }

  override def update(event: LineEvent): Unit = if (event.getType eq LineEvent.Type.STOP) whenDone(this)

  def gain(newGain:Float) = {
    masterGain.setValue((masterGain.getMinimum + Math.sqrt(newGain / 100.0) * (masterGain.getMaximum - masterGain.getMinimum)).toFloat)
  }

  def gain = (Math.pow(((masterGain.getValue-masterGain.getMinimum)/(masterGain.getMaximum - masterGain.getMinimum)), 2.0)*100)

  def isMaxGain = gain >= 100.0f

  def increaseGain(amount:Float) = gain(Math.min(gain+amount,100.0f).toFloat)
}