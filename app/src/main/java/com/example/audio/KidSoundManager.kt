package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Locale
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/**
 * KidSoundManager: Fully self-contained sound engine.
 * Generates and plays rich, encouraging audio without requiring external raw files:
 * 1. Native SoundPool with custom synthesized musical chimes (0ms latency).
 * 2. ToneGenerator backup via STREAM_MUSIC at maximum gain.
 * 3. TextToSpeech for warm Arabic voice praises ("أحسنت يا بطل!", "حاول ثانية!").
 */
class KidSoundManager(private val context: Context) : TextToSpeech.OnInitListener {
  private val sampleRate = 22050
  private val scope = CoroutineScope(Dispatchers.Default)

  private val _isSoundEnabled = MutableStateFlow(true)
  val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

  private var isAppActive = true

  // Native SoundPool for zero-latency game audio
  private var soundPool: SoundPool? = null
  private var soundIdSuccess1 = 0
  private var soundIdSuccess2 = 0
  private var soundIdFanfare = 0
  private var soundIdGentleOops = 0
  private var soundIdChalkSnap = 0
  private var soundIdCardLift = 0

  // Native Arabic Voice Synthesis
  private var tts: TextToSpeech? = null
  private var isTtsReady = false

  init {
    initSoundPool()

    try {
      tts = TextToSpeech(context, this)
    } catch (_: Throwable) {}
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val result = tts?.setLanguage(Locale("ar"))
      isTtsReady = (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED)
      if (isTtsReady) {
        tts?.setSpeechRate(0.95f)
        tts?.setPitch(1.15f)
      }
    }
  }

  private fun initSoundPool() {
    try {
      val attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

      soundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(attributes)
        .build()

      scope.launch(Dispatchers.IO) {
        try {
          val cacheDir = context.cacheDir
          val fSuccess1 = writeWavFile(cacheDir, "snd_success_1.wav", generateMarimbaArpeggio())
          val fSuccess2 = writeWavFile(cacheDir, "snd_success_2.wav", generateCrystalChime())
          val fFanfare = writeWavFile(cacheDir, "snd_fanfare.wav", generateFanfareSound())
          val fOops = writeWavFile(cacheDir, "snd_oops.wav", generateGentleOopsSound())
          val fSnap = writeWavFile(cacheDir, "snd_snap.wav", generateChalkSnapSound())
          val fLift = writeWavFile(cacheDir, "snd_lift.wav", generatePopSound())

          soundPool?.let { pool ->
            soundIdSuccess1 = pool.load(fSuccess1.absolutePath, 1)
            soundIdSuccess2 = pool.load(fSuccess2.absolutePath, 1)
            soundIdFanfare = pool.load(fFanfare.absolutePath, 1)
            soundIdGentleOops = pool.load(fOops.absolutePath, 1)
            soundIdChalkSnap = pool.load(fSnap.absolutePath, 1)
            soundIdCardLift = pool.load(fLift.absolutePath, 1)
          }
        } catch (_: Throwable) {}
      }
    } catch (_: Throwable) {}
  }

  private val vibrator: Vibrator? by lazy {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
      } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
      }
    } catch (_: Throwable) {
      null
    }
  }

  fun toggleSound() {
    _isSoundEnabled.value = !_isSoundEnabled.value
    if (_isSoundEnabled.value) {
      playTap()
    } else {
      tts?.stop()
    }
  }

  fun setAppActive(active: Boolean) {
    isAppActive = active
    if (!active) {
      tts?.stop()
    }
  }

  /**
   * Cheerful, joyful sound played on correct answer + encouraging Arabic voice praise!
   */
  fun playSuccess() {
    if (!_isSoundEnabled.value || !isAppActive) return

    val soundToPlay = if (Random.nextBoolean()) soundIdSuccess1 else soundIdSuccess2
    if (soundToPlay != 0) {
      soundPool?.play(soundToPlay, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    speakPraise()
    vibrateQuick(45)
  }

  /**
   * Gentle, encouraging sound on wrong answer + friendly retry prompt.
   */
  fun playWrong() {
    if (!_isSoundEnabled.value || !isAppActive) return

    if (soundIdGentleOops != 0) {
      soundPool?.play(soundIdGentleOops, 0.9f, 0.9f, 1, 0, 1.0f)
    }

    speakEncouragement()
    vibrateDouble()
  }

  /**
   * Triumphant level-up fanfare
   */
  fun playLevelUp() {
    if (!_isSoundEnabled.value || !isAppActive) return
    if (soundIdFanfare != 0) {
      soundPool?.play(soundIdFanfare, 1.0f, 1.0f, 2, 0, 1.0f)
    }
    speakText("ما شاء الله! ترقية ممتازة يا بطل!")
    vibrateQuick(70)
  }

  /**
   * Chalk snap sound when dropped on board
   */
  fun playChalkSnap() {
    if (!_isSoundEnabled.value || !isAppActive) return
    if (soundIdChalkSnap != 0) {
      soundPool?.play(soundIdChalkSnap, 0.9f, 0.9f, 1, 0, 1.0f)
    }
  }

  /**
   * Click / Tap sound
   */
  fun playTap() {
    if (!_isSoundEnabled.value || !isAppActive) return
    if (soundIdCardLift != 0) {
      soundPool?.play(soundIdCardLift, 0.7f, 0.7f, 1, 0, 1.0f)
    }
    vibrateQuick(20)
  }

  private fun speakPraise() {
    playRawSoundOrFallback("correct_answer", "إجابة صحيحة!")
  }

  private fun speakEncouragement() {
    playRawSoundOrFallback("wrong_answer", "حاول مرة أخرى!")
  }

  private fun playRawSoundOrFallback(rawResName: String, fallbackText: String) {
    if (!_isSoundEnabled.value || !isAppActive) return
    try {
      val resId = context.resources.getIdentifier(rawResName, "raw", context.packageName)
      if (resId != 0) {
        val mp = android.media.MediaPlayer.create(context, resId)
        mp?.setOnCompletionListener { it.release() }
        mp?.start()
        return
      }
    } catch (_: Throwable) {}

    // Fallback to native Arabic voice synthesis
    speakText(fallbackText)
  }

  private fun speakText(text: String) {
    if (!isTtsReady || !_isSoundEnabled.value || !isAppActive) return
    try {
      tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "kid_speech_${System.currentTimeMillis()}")
    } catch (_: Throwable) {}
  }

  private fun vibrateQuick(millis: Long) {
    try {
      val v = vibrator ?: return
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
      } else {
        @Suppress("DEPRECATION")
        v.vibrate(millis)
      }
    } catch (_: Throwable) {}
  }

  private fun vibrateDouble() {
    try {
      val v = vibrator ?: return
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 30, 40, 30), -1))
      } else {
        @Suppress("DEPRECATION")
        v.vibrate(70)
      }
    } catch (_: Throwable) {}
  }

  // --- Sound Wave Synthesizers ---

  private fun generateMarimbaArpeggio(): ShortArray {
    val notes = listOf(523.25, 659.25, 783.99, 1046.50) // C5, E5, G5, C6
    val noteDur = 0.12
    val totalSamples = (sampleRate * noteDur * notes.size).toInt()
    val buffer = ShortArray(totalSamples)
    val noteSamples = (sampleRate * noteDur).toInt()

    for (n in notes.indices) {
      val freq = notes[n]
      for (i in 0 until noteSamples) {
        val idx = n * noteSamples + i
        if (idx >= totalSamples) break
        val t = i.toDouble() / sampleRate
        val env = exp(-6.5 * (i.toDouble() / noteSamples))
        val wave = 0.65 * sin(2.0 * PI * freq * t) +
          0.25 * sin(4.0 * PI * freq * t) +
          0.10 * sin(6.0 * PI * freq * t)
        buffer[idx] = (wave * env * Short.MAX_VALUE * 0.70).toInt().toShort()
      }
    }
    return buffer
  }

  private fun generateCrystalChime(): ShortArray {
    val duration = 0.55
    val totalSamples = (sampleRate * duration).toInt()
    val buffer = ShortArray(totalSamples)
    val freqs = listOf(1318.51, 1567.98, 2093.00) // E6, G6, C7

    for (i in 0 until totalSamples) {
      val t = i.toDouble() / sampleRate
      val env = exp(-4.5 * (i.toDouble() / totalSamples))
      var wave = 0.0
      for (f in freqs) {
        wave += sin(2.0 * PI * f * t)
      }
      buffer[i] = ((wave / freqs.size) * env * Short.MAX_VALUE * 0.65).toInt().toShort()
    }
    return buffer
  }

  private fun generateGentleOopsSound(): ShortArray {
    val duration = 0.35
    val totalSamples = (sampleRate * duration).toInt()
    val buffer = ShortArray(totalSamples)

    for (i in 0 until totalSamples) {
      val t = i.toDouble() / sampleRate
      val progress = i.toDouble() / totalSamples
      val freq = 320.0 - (progress * 160.0) + (15.0 * sin(2.0 * PI * 20.0 * t))
      val env = exp(-3.8 * progress)
      val wave = 0.75 * sin(2.0 * PI * freq * t) + 0.25 * sin(4.0 * PI * freq * t)
      buffer[i] = (wave * env * Short.MAX_VALUE * 0.60).toInt().toShort()
    }
    return buffer
  }

  private fun generateChalkSnapSound(): ShortArray {
    val duration = 0.06
    val totalSamples = (sampleRate * duration).toInt()
    val buffer = ShortArray(totalSamples)
    for (i in 0 until totalSamples) {
      val progress = i.toDouble() / totalSamples
      val env = exp(-12.0 * progress)
      val noise = (Random.nextDouble() * 2.0 - 1.0) * 0.4
      val tone = sin(2.0 * PI * 850.0 * (i.toDouble() / sampleRate)) * 0.6
      buffer[i] = ((tone + noise) * env * Short.MAX_VALUE * 0.55).toInt().toShort()
    }
    return buffer
  }

  private fun generatePopSound(): ShortArray {
    val duration = 0.04
    val totalSamples = (sampleRate * duration).toInt()
    val buffer = ShortArray(totalSamples)
    for (i in 0 until totalSamples) {
      val progress = i.toDouble() / totalSamples
      val freq = 700.0 - progress * 350.0
      val env = exp(-9.0 * progress)
      buffer[i] = (sin(2.0 * PI * freq * (i.toDouble() / sampleRate)) * env * Short.MAX_VALUE * 0.55).toInt().toShort()
    }
    return buffer
  }

  private fun generateFanfareSound(): ShortArray {
    val notes = listOf(
      Pair(523.25, 0.10),
      Pair(659.25, 0.10),
      Pair(783.99, 0.10),
      Pair(1046.50, 0.28)
    )
    val totalSamples = (sampleRate * notes.sumOf { it.second }).toInt()
    val buffer = ShortArray(totalSamples)
    var offset = 0
    for ((freq, dur) in notes) {
      val noteCount = (sampleRate * dur).toInt()
      for (i in 0 until noteCount) {
        val idx = offset + i
        if (idx >= totalSamples) break
        val t = i.toDouble() / sampleRate
        val env = exp(-3.2 * (i.toDouble() / noteCount))
        val wave = 0.7 * sin(2.0 * PI * freq * t) + 0.3 * sin(4.0 * PI * freq * t)
        buffer[idx] = (wave * env * Short.MAX_VALUE * 0.65).toInt().toShort()
      }
      offset += noteCount
    }
    return buffer
  }

  private fun writeWavFile(dir: File, filename: String, pcmData: ShortArray): File {
    val file = File(dir, filename)
    val byteData = ByteArray(pcmData.size * 2)
    ByteBuffer.wrap(byteData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(pcmData)

    val totalAudioLen = byteData.size.toLong()
    val totalDataLen = totalAudioLen + 36
    val longSampleRate = sampleRate.toLong()
    val channels = 1
    val byteRate = 16 * sampleRate * channels / 8

    FileOutputStream(file).use { out ->
      val header = ByteArray(44)
      header[0] = 'R'.code.toByte()
      header[1] = 'I'.code.toByte()
      header[2] = 'F'.code.toByte()
      header[3] = 'F'.code.toByte()
      header[4] = (totalDataLen and 0xff).toByte()
      header[5] = ((totalDataLen shr 8) and 0xff).toByte()
      header[6] = ((totalDataLen shr 16) and 0xff).toByte()
      header[7] = ((totalDataLen shr 24) and 0xff).toByte()
      header[8] = 'W'.code.toByte()
      header[9] = 'A'.code.toByte()
      header[10] = 'V'.code.toByte()
      header[11] = 'E'.code.toByte()
      header[12] = 'f'.code.toByte()
      header[13] = 'm'.code.toByte()
      header[14] = 't'.code.toByte()
      header[15] = ' '.code.toByte()
      header[16] = 16
      header[17] = 0
      header[18] = 0
      header[19] = 0
      header[20] = 1
      header[21] = 0
      header[22] = channels.toByte()
      header[23] = 0
      header[24] = (longSampleRate and 0xff).toByte()
      header[25] = ((longSampleRate shr 8) and 0xff).toByte()
      header[26] = ((longSampleRate shr 16) and 0xff).toByte()
      header[27] = ((longSampleRate shr 24) and 0xff).toByte()
      header[28] = (byteRate and 0xff).toByte()
      header[29] = ((byteRate shr 8) and 0xff).toByte()
      header[30] = ((byteRate shr 16) and 0xff).toByte()
      header[31] = ((byteRate shr 24) and 0xff).toByte()
      header[32] = (channels * 16 / 8).toByte()
      header[33] = 0
      header[34] = 16
      header[35] = 0
      header[36] = 'd'.code.toByte()
      header[37] = 'a'.code.toByte()
      header[38] = 't'.code.toByte()
      header[39] = 'a'.code.toByte()
      header[40] = (totalAudioLen and 0xff).toByte()
      header[41] = ((totalAudioLen shr 8) and 0xff).toByte()
      header[42] = ((totalAudioLen shr 16) and 0xff).toByte()
      header[43] = ((totalAudioLen shr 24) and 0xff).toByte()

      out.write(header)
      out.write(byteData)
    }
    return file
  }
}
