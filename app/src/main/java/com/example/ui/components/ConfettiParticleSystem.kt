package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Particle(
  val startX: Float,
  val startY: Float,
  val velocityX: Float,
  val velocityY: Float,
  val color: Color,
  val size: Float,
  val isStar: Boolean
)

@Composable
fun ConfettiParticleExplosion(
  triggerKey: Int,
  modifier: Modifier = Modifier,
  origin: Offset? = null
) {
  if (triggerKey == 0) return

  val progress = remember(triggerKey) { Animatable(0f) }

  val particles = remember(triggerKey) {
    val rnd = Random(triggerKey.toLong())
    val colors = listOf(
      Color(0xFFFDE047), // Gold
      Color(0xFF38BDF8), // Cyan
      Color(0xFFF43F5E), // Rose Pink
      Color(0xFF4ADE80), // Lime Green
      Color(0xFFA855F7), // Purple
      Color(0xFFFB923C)  // Orange
    )
    List(40) {
      val angle = rnd.nextFloat() * 2f * Math.PI.toFloat()
      val speed = rnd.nextFloat() * 700f + 250f
      Particle(
        startX = origin?.x ?: 500f,
        startY = origin?.y ?: 800f,
        velocityX = cos(angle) * speed,
        velocityY = sin(angle) * speed - 150f, // upward bias
        color = colors[rnd.nextInt(colors.size)],
        size = rnd.nextFloat() * 14f + 8f,
        isStar = rnd.nextBoolean()
      )
    }
  }

  LaunchedEffect(triggerKey) {
    progress.snapTo(0f)
    progress.animateTo(
      targetValue = 1f,
      animationSpec = tween(900, easing = LinearEasing)
    )
  }

  if (progress.value < 1f) {
    Canvas(modifier = modifier.fillMaxSize()) {
      val t = progress.value
      val alpha = (1f - t * t).coerceIn(0f, 1f)

      particles.forEach { p ->
        // Physics: position = start + v*t + 0.5*g*t^2
        val currentX = p.startX + p.velocityX * t
        val currentY = p.startY + p.velocityY * t + 0.5f * 980f * t * t

        if (p.isStar) {
          // Draw mini star
          val starPath = Path().apply {
            val r = p.size * (1f - t * 0.3f)
            val cx = currentX
            val cy = currentY
            for (i in 0 until 5) {
              val outerAngle = (i * 72 - 90) * Math.PI / 180.0
              val innerAngle = (i * 72 + 36 - 90) * Math.PI / 180.0
              val ox = cx + (r * cos(outerAngle)).toFloat()
              val oy = cy + (r * sin(outerAngle)).toFloat()
              val ix = cx + (r * 0.45f * cos(innerAngle)).toFloat()
              val iy = cy + (r * 0.45f * sin(innerAngle)).toFloat()
              if (i == 0) moveTo(ox, oy) else lineTo(ox, oy)
              lineTo(ix, iy)
            }
            close()
          }
          drawPath(starPath, color = p.color.copy(alpha = alpha))
        } else {
          drawCircle(
            color = p.color.copy(alpha = alpha),
            radius = p.size * 0.5f * (1f - t * 0.2f),
            center = Offset(currentX, currentY)
          )
        }
      }
    }
  }
}
