package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
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
import java.util.Locale
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/**
 * KidSoundManager: Fully self-contained native PCM audio engine.
 * Plays direct PCM audio via AudioTrack with 0ms latency without invoking MediaCodec.
 */
class KidSoundManager(private val context: Context) : TextToSpeech.OnInitListener {
  private val sampleRate = 22050
  private val scope = CoroutineScope(Dispatchers.Default)

  private val _isSoundEnabled = MutableStateFlow(true)
  val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

  private var isAppActive = true

  // Direct Hardware-independent AudioTracks for instant sound
  private var trackSuccess1: AudioTrack? = null
  private var trackSuccess2: AudioTrack? = null
  private var trackFanfare: AudioTrack? = null
  private var trackGentleOops: AudioTrack? = null
  private var trackChalkSnap: AudioTrack? = null
  private var trackCardLift: AudioTrack? = null

  // Native Arabic Voice Synthesis
  private var tts: TextToSpeech? = null
  private var isTtsReady = false

  init {
    scope.launch(Dispatchers.Default) {
      try {
        trackSuccess1 = createStaticAudioTrack(generateMarimbaArpeggio())
        trackSuccess2 = createStaticAudioTrack(generateCrystalChime())
        trackFanfare = createStaticAudioTrack(generateFanfareSound())
        trackGentleOops = createStaticAudioTrack(generateGentleOopsSound())
        trackChalkSnap = createStaticAudioTrack(generateChalkSnapSound())
        trackCardLift = createStaticAudioTrack(generatePopSound())
      } catch (_: Throwable) {}
    }

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

  private fun createStaticAudioTrack(pcmData: ShortArray): AudioTrack? {
    return try {
      val minBufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
      )
      val bufferSize = maxOf(pcmData.size * 2, minBufferSize)

      val attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

      val format = AudioFormat.Builder()
        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
        .setSampleRate(sampleRate)
        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
        .build()

      val track = AudioTrack.Builder()
        .setAudioAttributes(attributes)
        .setAudioFormat(format)
        .setBufferSizeInBytes(bufferSize)
        .setTransferMode(AudioTrack.MODE_STATIC)
        .build()

      track.write(pcmData, 0, pcmData.size)
      track
    } catch (_: Throwable) {
      null
    }
  }

  private fun playTrack(track: AudioTrack?) {
    if (track == null || !_isSoundEnabled.value || !isAppActive) return
    scope.launch(Dispatchers.Default) {
      try {
        track.stop()
        track.reloadStaticData()
        track.play()
      } catch (_: Throwable) {}
    }
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

    val trackToPlay = if (Random.nextBoolean()) trackSuccess1 else trackSuccess2
    playTrack(trackToPlay)

    speakPraise()
    vibrateQuick(45)
  }

  /**
   * Gentle, encouraging sound on wrong answer + friendly retry prompt.
   */
  fun playWrong() {
    if (!_isSoundEnabled.value || !isAppActive) return
    playTrack(trackGentleOops)
    speakEncouragement()
    vibrateDouble()
  }

  /**
   * Triumphant level-up fanfare
   */
  fun playLevelUp() {
    if (!_isSoundEnabled.value || !isAppActive) return
    playTrack(trackFanfare)
    speakText("ما شاء الله! ترقية ممتازة يا بطل!")
    vibrateQuick(70)
  }

  /**
   * Chalk snap sound when dropped on board
   */
  fun playChalkSnap() {
    playTrack(trackChalkSnap)
  }

  /**
   * Click / Tap sound
   */
  fun playTap() {
    playTrack(trackCardLift)
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
}
