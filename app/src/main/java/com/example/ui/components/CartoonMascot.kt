package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class MascotMood {
  IDLE,
  CELEBRATING,
  OOPS,
  THINKING
}

@Composable
fun CartoonAstronautMascot(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 120.dp,
  mood: MascotMood = MascotMood.IDLE
) {
  val infiniteTransition = rememberInfiniteTransition(label = "mascot_idle")

  val floatY by infiniteTransition.animateFloat(
    initialValue = -6f,
    targetValue = 6f,
    animationSpec = infiniteRepeatable(
      animation = tween(1800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "mascot_float"
  )

  val waveAngle by infiniteTransition.animateFloat(
    initialValue = -15f,
    targetValue = 15f,
    animationSpec = infiniteRepeatable(
      animation = tween(600, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "mascot_wave"
  )

  // Celebration jump offset
  val jumpAnim = remember { Animatable(0f) }
  LaunchedEffect(mood) {
    if (mood == MascotMood.CELEBRATING) {
      jumpAnim.animateTo(-24f, tween(180, easing = FastOutSlowInEasing))
      jumpAnim.animateTo(0f, tween(200, easing = FastOutSlowInEasing))
      jumpAnim.animateTo(-16f, tween(160, easing = FastOutSlowInEasing))
      jumpAnim.animateTo(0f, tween(180, easing = FastOutSlowInEasing))
    }
  }

  // Wiggle on oops
  val wobbleAnim = remember { Animatable(0f) }
  LaunchedEffect(mood) {
    if (mood == MascotMood.OOPS) {
      wobbleAnim.animateTo(-12f, tween(80))
      wobbleAnim.animateTo(12f, tween(80))
      wobbleAnim.animateTo(-8f, tween(80))
      wobbleAnim.animateTo(8f, tween(80))
      wobbleAnim.animateTo(0f, tween(80))
    }
  }

  Box(
    modifier = modifier.size(sizeDp),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.size(sizeDp)) {
      val w = size.width
      val h = size.height
      val currentY = (h * 0.5f) + floatY + jumpAnim.value
      val currentX = (w * 0.5f) + wobbleAnim.value

      // --- Jetpack Glow behind astronaut ---
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0x90FF9100), Color(0x00FF9100)),
          center = Offset(currentX, currentY + h * 0.28f),
          radius = w * 0.35f
        ),
        radius = w * 0.35f,
        center = Offset(currentX, currentY + h * 0.28f)
      )

      // --- Cute Jetpack / Oxygen Tank ---
      drawRoundRect(
        color = Color(0xFF0284C7),
        topLeft = Offset(currentX - w * 0.28f, currentY - h * 0.05f),
        size = Size(w * 0.56f, h * 0.32f),
        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
      )

      // --- Body / Spacesuit ---
      drawRoundRect(
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xFFFFFFFF), Color(0xFFE2E8F0)),
          startY = currentY - h * 0.08f,
          endY = currentY + h * 0.3f
        ),
        topLeft = Offset(currentX - w * 0.22f, currentY),
        size = Size(w * 0.44f, h * 0.32f),
        cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
      )

      // Spacesuit badge (star badge)
      drawCircle(
        color = Color(0xFFFBBF24),
        radius = w * 0.045f,
        center = Offset(currentX, currentY + h * 0.15f)
      )

      // --- Waving Arms ---
      val armY = currentY + h * 0.06f
      val armOffset = if (mood == MascotMood.CELEBRATING) -h * 0.12f else 0f

      // Left Arm
      drawRoundRect(
        color = Color(0xFFF1F5F9),
        topLeft = Offset(currentX - w * 0.34f, armY + armOffset),
        size = Size(w * 0.14f, h * 0.2f),
        cornerRadius = CornerRadius(w * 0.07f, w * 0.07f)
      )
      // Left Glove
      drawCircle(
        color = Color(0xFFEC4899),
        radius = w * 0.06f,
        center = Offset(currentX - w * 0.27f, armY + h * 0.2f + armOffset)
      )

      // Right Arm (waving)
      val rightArmWave = if (mood == MascotMood.CELEBRATING) -h * 0.14f else (waveAngle * 0.4f)
      drawRoundRect(
        color = Color(0xFFF1F5F9),
        topLeft = Offset(currentX + w * 0.2f, armY + rightArmWave),
        size = Size(w * 0.14f, h * 0.2f),
        cornerRadius = CornerRadius(w * 0.07f, w * 0.07f)
      )
      // Right Glove
      drawCircle(
        color = Color(0xFFEC4899),
        radius = w * 0.06f,
        center = Offset(currentX + w * 0.27f, armY + h * 0.2f + rightArmWave)
      )

      // --- Big Cute Astronaut Helmet ---
      val helmetCenter = Offset(currentX, currentY - h * 0.16f)
      val helmetRadius = w * 0.32f

      // Helmet Base Outer Ring
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xFFFFFFFF), Color(0xFFCBD5E1)),
          center = helmetCenter,
          radius = helmetRadius
        ),
        radius = helmetRadius,
        center = helmetCenter
      )

      // Antenna on top
      drawLine(
        color = Color(0xFF94A3B8),
        start = Offset(currentX, helmetCenter.y - helmetRadius),
        end = Offset(currentX, helmetCenter.y - helmetRadius - h * 0.09f),
        strokeWidth = 6f
      )
      drawCircle(
        color = if (mood == MascotMood.CELEBRATING) Color(0xFF22C55E) else Color(0xFFEF4444),
        radius = w * 0.045f,
        center = Offset(currentX, helmetCenter.y - helmetRadius - h * 0.09f)
      )

      // Helmet Visor Glass (Big, Glossy, Smiling)
      val visorRect = RectHelper(
        left = currentX - helmetRadius * 0.72f,
        top = helmetCenter.y - helmetRadius * 0.65f,
        width = helmetRadius * 1.44f,
        height = helmetRadius * 1.3f
      )

      drawRoundRect(
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF4338CA)),
          startY = visorRect.top,
          endY = visorRect.top + visorRect.height
        ),
        topLeft = Offset(visorRect.left, visorRect.top),
        size = Size(visorRect.width, visorRect.height),
        cornerRadius = CornerRadius(visorRect.height * 0.45f, visorRect.height * 0.45f)
      )

      // Visor rim border
      drawRoundRect(
        color = Color(0xFF38BDF8),
        topLeft = Offset(visorRect.left, visorRect.top),
        size = Size(visorRect.width, visorRect.height),
        cornerRadius = CornerRadius(visorRect.height * 0.45f, visorRect.height * 0.45f),
        style = Stroke(width = 4f)
      )

      // Visor cute reflection streak
      val reflectionPath = Path().apply {
        moveTo(visorRect.left + visorRect.width * 0.15f, visorRect.top + visorRect.height * 0.2f)
        lineTo(visorRect.left + visorRect.width * 0.45f, visorRect.top + visorRect.height * 0.15f)
        lineTo(visorRect.left + visorRect.width * 0.35f, visorRect.top + visorRect.height * 0.45f)
        lineTo(visorRect.left + visorRect.width * 0.1f, visorRect.top + visorRect.height * 0.5f)
        close()
      }
      drawPath(reflectionPath, color = Color(0x60FFFFFF))

      // --- Expressive Eyes / Face inside Visor ---
      val eyeCenterY = helmetCenter.y - h * 0.03f
      val eyeSpacing = w * 0.09f

      when (mood) {
        MascotMood.CELEBRATING -> {
          // Happy arch closed eyes (^_^)
          drawArc(
            color = Color(0xFFFDE047),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(currentX - eyeSpacing - w * 0.045f, eyeCenterY - h * 0.02f),
            size = Size(w * 0.09f, h * 0.04f),
            style = Stroke(width = 6f)
          )
          drawArc(
            color = Color(0xFFFDE047),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(currentX + eyeSpacing - w * 0.045f, eyeCenterY - h * 0.02f),
            size = Size(w * 0.09f, h * 0.04f),
            style = Stroke(width = 6f)
          )

          // Happy wide open smile
          drawArc(
            color = Color(0xFFF43F5E),
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(currentX - w * 0.05f, eyeCenterY + h * 0.025f),
            size = Size(w * 0.1f, h * 0.05f)
          )
        }
        MascotMood.OOPS -> {
          // Curious wondering round eyes (o_O)
          drawCircle(
            color = Color.White,
            radius = w * 0.04f,
            center = Offset(currentX - eyeSpacing, eyeCenterY)
          )
          drawCircle(
            color = Color(0xFF1E293B),
            radius = w * 0.02f,
            center = Offset(currentX - eyeSpacing, eyeCenterY)
          )

          drawCircle(
            color = Color.White,
            radius = w * 0.048f,
            center = Offset(currentX + eyeSpacing, eyeCenterY)
          )
          drawCircle(
            color = Color(0xFF1E293B),
            radius = w * 0.025f,
            center = Offset(currentX + eyeSpacing, eyeCenterY)
          )

          // Small "o" mouth
          drawCircle(
            color = Color(0xFFF43F5E),
            radius = w * 0.02f,
            center = Offset(currentX, eyeCenterY + h * 0.045f)
          )
        }
        else -> {
          // Normal friendly big glowing anime eyes
          // Left Eye
          drawCircle(
            color = Color(0xFF38BDF8),
            radius = w * 0.042f,
            center = Offset(currentX - eyeSpacing, eyeCenterY)
          )
          drawCircle(
            color = Color.White,
            radius = w * 0.018f,
            center = Offset(currentX - eyeSpacing - 3f, eyeCenterY - 3f)
          )

          // Right Eye
          drawCircle(
            color = Color(0xFF38BDF8),
            radius = w * 0.042f,
            center = Offset(currentX + eyeSpacing, eyeCenterY)
          )
          drawCircle(
            color = Color.White,
            radius = w * 0.018f,
            center = Offset(currentX + eyeSpacing - 3f, eyeCenterY - 3f)
          )

          // Cute smile
          drawArc(
            color = Color.White,
            startAngle = 10f,
            sweepAngle = 160f,
            useCenter = false,
            topLeft = Offset(currentX - w * 0.04f, eyeCenterY + h * 0.02f),
            size = Size(w * 0.08f, h * 0.03f),
            style = Stroke(width = 4f)
          )
        }
      }
    }
  }
}

private data class RectHelper(
  val left: Float,
  val top: Float,
  val width: Float,
  val height: Float
)
