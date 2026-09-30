package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

private data class StarParticle(
  val xRatio: Float,
  val yRatio: Float,
  val radius: Float,
  val baseAlpha: Float,
  val pulseSpeed: Int,
  val color: Color
)

@Composable
fun PlayfulCosmicBackground(modifier: Modifier = Modifier) {
  val infiniteTransition = rememberInfiniteTransition(label = "stars")

  // Gentle background drift & twinkle
  val twinkleAnim by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(2400, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "twinkle"
  )

  val floatAnim by infiniteTransition.animateFloat(
    initialValue = -10f,
    targetValue = 10f,
    animationSpec = infiniteRepeatable(
      animation = tween(4000, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "float"
  )

  // Seeded stars for consistency
  val stars = remember {
    val rnd = Random(42)
    val starColors = listOf(
      Color(0xFFFFF9C4), // Soft Gold
      Color(0xFFE1F5FE), // Soft Cyan
      Color(0xFFFCE4EC), // Soft Pink
      Color(0xFFFFFFFF), // Pure White
      Color(0xFFB39DDB)  // Lavender
    )
    List(55) {
      StarParticle(
        xRatio = rnd.nextFloat(),
        yRatio = rnd.nextFloat(),
        radius = rnd.nextFloat() * 3.5f + 1.5f,
        baseAlpha = rnd.nextFloat() * 0.5f + 0.4f,
        pulseSpeed = rnd.nextInt(1500, 3500),
        color = starColors[rnd.nextInt(starColors.size)]
      )
    }
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    val width = size.width
    val height = size.height

    // Deep vibrant cosmic sky gradient
    drawRect(
      brush = Brush.verticalGradient(
        colors = listOf(
          Color(0xFF13092D), // Deep Midnight Purple
          Color(0xFF1E1045), // Royal Indigo
          Color(0xFF281158), // Rich Violet
          Color(0xFF180A38)  // Dark Star Base
        )
      )
    )

    // Soft glowing nebula clouds
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0x35E040FB), Color(0x00E040FB)),
        center = Offset(width * 0.2f, height * 0.25f + floatAnim),
        radius = width * 0.6f
      ),
      center = Offset(width * 0.2f, height * 0.25f + floatAnim),
      radius = width * 0.6f
    )

    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0x3000E5FF), Color(0x0000E5FF)),
        center = Offset(width * 0.85f, height * 0.7f - floatAnim),
        radius = width * 0.5f
      ),
      center = Offset(width * 0.85f, height * 0.7f - floatAnim),
      radius = width * 0.5f
    )

    // Draw twinkling stars
    stars.forEachIndexed { idx, star ->
      val x = star.xRatio * width
      val y = (star.yRatio * height + if (idx % 2 == 0) floatAnim * 0.5f else -floatAnim * 0.5f) % height
      val alphaMultiplier = if (idx % 3 == 0) twinkleAnim else (1.4f - twinkleAnim)
      val finalAlpha = (star.baseAlpha * alphaMultiplier).coerceIn(0.15f, 1f)

      drawCircle(
        color = star.color.copy(alpha = finalAlpha),
        radius = star.radius,
        center = Offset(x, y)
      )

      // Sparkle cross on larger stars
      if (star.radius > 3.8f && finalAlpha > 0.6f) {
        val crossLen = star.radius * 2.2f
        drawLine(
          color = star.color.copy(alpha = finalAlpha * 0.7f),
          start = Offset(x - crossLen, y),
          end = Offset(x + crossLen, y),
          strokeWidth = 1.2f
        )
        drawLine(
          color = star.color.copy(alpha = finalAlpha * 0.7f),
          start = Offset(x, y - crossLen),
          end = Offset(x, y + crossLen),
          strokeWidth = 1.2f
        )
      }
    }
  }
}
