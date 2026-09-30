package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * KidSoundManager handles synthesized kid-friendly sound effects and a gentle
 * background lullaby melody using Android's native AudioTrack with zero external assets.
 */
class KidSoundManager(private val context: Context) {
  private val sampleRate = 44100
  private val scope = CoroutineScope(Dispatchers.Default)

  private val _isSoundEnabled = MutableStateFlow(true)
  val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

  private var musicJob: Job? = null
  private var isAppActive = true

  private val vibrator: Vibrator? by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
      vibratorManager?.defaultVibrator
    } else {
      @Suppress("DEPRECATION")
      context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
  }

  // Pre-generated sound buffers for instantaneous zero-latency playback
  private val popBuffer: ShortArray by lazy { generatePopSound() }
  private val successBuffer: ShortArray by lazy { generateSuccessSound() }
  private val wrongBuffer: ShortArray by lazy { generateWrongSound() }
  private val starBuffer: ShortArray by lazy { generateStarSound() }
  private val fanfareBuffer: ShortArray by lazy { generateFanfareSound() }

  init {
    startBackgroundMelody()
  }

  fun toggleSound() {
    _isSoundEnabled.value = !_isSoundEnabled.value
    if (_isSoundEnabled.value) {
      playPop()
      startBackgroundMelody()
    } else {
      stopBackgroundMelody()
    }
  }

  fun setAppActive(active: Boolean) {
    isAppActive = active
    if (!active) {
      stopBackgroundMelody()
    } else if (_isSoundEnabled.value) {
      startBackgroundMelody()
    }
  }

  fun playPop() {
    if (!_isSoundEnabled.value) return
    playSoundAsync(popBuffer, volume = 0.5f)
  }

  fun playSuccess() {
    if (_isSoundEnabled.value) {
      playSoundAsync(successBuffer, volume = 0.8f)
    }
    vibrateGentle(longPulse = false)
  }

  fun playWrong() {
    if (_isSoundEnabled.value) {
      playSoundAsync(wrongBuffer, volume = 0.6f)
    }
    vibrateGentle(longPulse = true)
  }

  fun playStarCollect() {
    if (!_isSoundEnabled.value) return
    playSoundAsync(starBuffer, volume = 0.75f)
  }

  fun playLevelComplete() {
    if (!_isSoundEnabled.value) return
    playSoundAsync(fanfareBuffer, volume = 0.9f)
  }

  private fun vibrateGentle(longPulse: Boolean) {
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (longPulse) {
          vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 40, 60, 40), -1))
        } else {
          vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
      } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(if (longPulse) 100 else 40)
      }
    } catch (_: Exception) {}
  }

  private fun playSoundAsync(buffer: ShortArray, volume: Float) {
    scope.launch {
      try {
        val audioTrack = AudioTrack.Builder()
          .setAudioAttributes(
            AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_GAME)
              .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
              .build()
          )
          .setAudioFormat(
            AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
              .setSampleRate(sampleRate)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
              .build()
          )
          .setBufferSizeInBytes(buffer.size * 2)
          .setTransferMode(AudioTrack.MODE_STATIC)
          .build()

        audioTrack.setVolume(volume)
        audioTrack.write(buffer, 0, buffer.size)
        audioTrack.play()
        delay(buffer.size * 1000L / sampleRate + 50)
        audioTrack.stop()
        audioTrack.release()
      } catch (_: Exception) {}
    }
  }

  private fun startBackgroundMelody() {
    if (musicJob?.isActive == true) return
    musicJob = scope.launch {
      // Gentle music box pentatonic lullaby notes (Hz)
      // C4, E4, G4, A4, C5, G4, E4, D4
      val melodyNotes = listOf(
        Pair(261.63, 350L), // C4
        Pair(329.63, 350L), // E4
        Pair(392.00, 350L), // G4
        Pair(523.25, 450L), // C5
        Pair(440.00, 350L), // A4
        Pair(392.00, 400L), // G4
        Pair(329.63, 350L), // E4
        Pair(293.66, 500L), // D4

        Pair(261.63, 350L), // C4
        Pair(392.00, 350L), // G4
        Pair(440.00, 350L), // A4
        Pair(523.25, 600L), // C5
        Pair(392.00, 350L), // G4
        Pair(329.63, 400L), // E4
        Pair(261.63, 700L)  // C4
      )

      while (isActive && _isSoundEnabled.value && isAppActive) {
        for ((freq, duration) in melodyNotes) {
          if (!isActive || !_isSoundEnabled.value || !isAppActive) break
          playNoteAsync(freq, duration, volume = 0.08f)
          delay(duration + 80)
        }
        delay(600) // Brief breath between melody loops
      }
    }
  }

  private fun stopBackgroundMelody() {
    musicJob?.cancel()
    musicJob = null
  }

  private fun playNoteAsync(freq: Double, durationMs: Long, volume: Float) {
    scope.launch {
      try {
        val count = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(count)
        val attackSamples = (sampleRate * 0.015).toInt()

        for (i in 0 until count) {
          val t = i.toDouble() / sampleRate
          val env = if (i < attackSamples) {
            i.toDouble() / attackSamples
          } else {
            exp(-3.5 * (i - attackSamples).toDouble() / count)
          }
          // Warm music box sound (fundamental + soft octave harmonic)
          val sampleVal = sin(2.0 * PI * freq * t) + 0.3 * sin(4.0 * PI * freq * t)
          val s = (sampleVal * env * Short.MAX_VALUE * volume).toInt()
          buffer[i] = s.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        val audioTrack = AudioTrack.Builder()
          .setAudioAttributes(
            AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_MEDIA)
              .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
              .build()
          )
          .setAudioFormat(
            AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
              .setSampleRate(sampleRate)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
              .build()
          )
          .setBufferSizeInBytes(buffer.size * 2)
          .setTransferMode(AudioTrack.MODE_STATIC)
          .build()

        audioTrack.write(buffer, 0, buffer.size)
        audioTrack.play()
        delay(durationMs + 20)
        audioTrack.stop()
        audioTrack.release()
      } catch (_: Exception) {}
    }
  }

  // --- Procedural Sound Synthesis ---

  private fun generatePopSound(): ShortArray {
    val durationMs = 70
    val count = (sampleRate * (durationMs / 1000.0)).toInt()
    val buffer = ShortArray(count)
    for (i in 0 until count) {
      val t = i.toDouble() / sampleRate
      val progress = i.toDouble() / count
      val freq = 550.0 - (progress * 280.0)
      val env = exp(-8.0 * progress)
      val sample = sin(2.0 * PI * freq * t) * env
      buffer[i] = (sample * Short.MAX_VALUE * 0.6).toInt().toShort()
    }
    return buffer
  }

  private fun generateSuccessSound(): ShortArray {
    // 4 quick bright ascending chime notes: C5, E5, G5, C6
    val notes = listOf(523.25, 659.25, 783.99, 1046.50)
    val noteDuration = 0.11
    val totalCount = (sampleRate * noteDuration * notes.size).toInt()
    val buffer = ShortArray(totalCount)
    val noteSamples = (sampleRate * noteDuration).toInt()

    for (n in notes.indices) {
      val freq = notes[n]
      for (i in 0 until noteSamples) {
        val idx = n * noteSamples + i
        if (idx >= totalCount) break
        val t = i.toDouble() / sampleRate
        val env = exp(-4.0 * (i.toDouble() / noteSamples))
        // Bell chime tone
        val sample = (sin(2.0 * PI * freq * t) + 0.4 * sin(4.0 * PI * freq * t)) * env
        buffer[idx] = (sample * Short.MAX_VALUE * 0.5).toInt().toShort()
      }
    }
    return buffer
  }

  private fun generateWrongSound(): ShortArray {
    // Friendly, funny cartoon boing: pitch dips gently 280Hz -> 140Hz with soft wobble
    val durationMs = 280
    val count = (sampleRate * (durationMs / 1000.0)).toInt()
    val buffer = ShortArray(count)
    for (i in 0 until count) {
      val t = i.toDouble() / sampleRate
      val progress = i.toDouble() / count
      val freq = 260.0 - 110.0 * progress + 20.0 * sin(2.0 * PI * 14.0 * t)
      val env = exp(-2.5 * progress) * (1.0 - progress * 0.5)
      val sample = sin(2.0 * PI * freq * t) * env
      buffer[i] = (sample * Short.MAX_VALUE * 0.5).toInt().toShort()
    }
    return buffer
  }

  private fun generateStarSound(): ShortArray {
    // Shimmering high sparkle
    val notes = listOf(1318.51, 1567.98, 1975.53, 2093.00)
    val noteDuration = 0.07
    val totalCount = (sampleRate * noteDuration * notes.size).toInt()
    val buffer = ShortArray(totalCount)
    val noteSamples = (sampleRate * noteDuration).toInt()

    for (n in notes.indices) {
      val freq = notes[n]
      for (i in 0 until noteSamples) {
        val idx = n * noteSamples + i
        if (idx >= totalCount) break
        val t = i.toDouble() / sampleRate
        val env = exp(-6.0 * (i.toDouble() / noteSamples))
        val sample = sin(2.0 * PI * freq * t) * env
        buffer[idx] = (sample * Short.MAX_VALUE * 0.5).toInt().toShort()
      }
    }
    return buffer
  }

  private fun generateFanfareSound(): ShortArray {
    // Level Complete celebration: G4, C5, E5, G5, high C6 sustain
    val notes = listOf(
      Pair(392.00, 0.12),
      Pair(523.25, 0.12),
      Pair(659.25, 0.12),
      Pair(783.99, 0.18),
      Pair(1046.50, 0.45)
    )
    val totalCount = (sampleRate * notes.sumOf { it.second }).toInt()
    val buffer = ShortArray(totalCount)

    var offset = 0
    for ((freq, dur) in notes) {
      val noteSamples = (sampleRate * dur).toInt()
      for (i in 0 until noteSamples) {
        val idx = offset + i
        if (idx >= totalCount) break
        val t = i.toDouble() / sampleRate
        val progress = i.toDouble() / noteSamples
        val env = if (dur > 0.3) exp(-2.0 * progress) else exp(-3.0 * progress)
        val sample = (sin(2.0 * PI * freq * t) + 0.35 * sin(4.0 * PI * freq * t) + 0.15 * sin(6.0 * PI * freq * t)) * env
        buffer[idx] = (sample * Short.MAX_VALUE * 0.5).toInt().toShort()
      }
      offset += noteSamples
    }
    return buffer
  }
}
