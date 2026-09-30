package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CuteRocket(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 56.dp,
  tiltDegrees: Float = 45f
) {
  val infiniteTransition = rememberInfiniteTransition(label = "rocket_flame")
  val flameScale by infiniteTransition.animateFloat(
    initialValue = 0.8f,
    targetValue = 1.3f,
    animationSpec = infiniteRepeatable(
      animation = tween(120, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "flame"
  )

  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f
    val cy = h * 0.45f

    // Flame
    val flameLength = h * 0.35f * flameScale
    val flamePath = Path().apply {
      moveTo(cx - w * 0.12f, cy + h * 0.28f)
      lineTo(cx, cy + h * 0.28f + flameLength)
      lineTo(cx + w * 0.12f, cy + h * 0.28f)
      close()
    }
    drawPath(flamePath, color = Color(0xFFFF9100))

    val innerFlamePath = Path().apply {
      moveTo(cx - w * 0.06f, cy + h * 0.28f)
      lineTo(cx, cy + h * 0.28f + flameLength * 0.6f)
      lineTo(cx + w * 0.06f, cy + h * 0.28f)
      close()
    }
    drawPath(innerFlamePath, color = Color(0xFFFFEA00))

    // Fins
    val leftFin = Path().apply {
      moveTo(cx - w * 0.18f, cy + h * 0.08f)
      lineTo(cx - w * 0.38f, cy + h * 0.3f)
      lineTo(cx - w * 0.18f, cy + h * 0.26f)
      close()
    }
    drawPath(leftFin, color = Color(0xFFE11D48))

    val rightFin = Path().apply {
      moveTo(cx + w * 0.18f, cy + h * 0.08f)
      lineTo(cx + w * 0.38f, cy + h * 0.3f)
      lineTo(cx + w * 0.18f, cy + h * 0.26f)
      close()
    }
    drawPath(rightFin, color = Color(0xFFE11D48))

    // Rocket Body
    val bodyPath = Path().apply {
      moveTo(cx, cy - h * 0.38f)
      cubicTo(cx + w * 0.24f, cy - h * 0.15f, cx + w * 0.24f, cy + h * 0.2f, cx + w * 0.18f, cy + h * 0.28f)
      lineTo(cx - w * 0.18f, cy + h * 0.28f)
      cubicTo(cx - w * 0.24f, cy + h * 0.2f, cx - w * 0.24f, cy - h * 0.15f, cx, cy - h * 0.38f)
      close()
    }
    drawPath(bodyPath, color = Color.White)

    // Nose Cone
    val nosePath = Path().apply {
      moveTo(cx, cy - h * 0.38f)
      cubicTo(cx + w * 0.14f, cy - h * 0.25f, cx + w * 0.18f, cy - h * 0.15f, cx + w * 0.19f, cy - h * 0.1f)
      lineTo(cx - w * 0.19f, cy - h * 0.1f)
      cubicTo(cx - w * 0.18f, cy - h * 0.15f, cx - w * 0.14f, cy - h * 0.25f, cx, cy - h * 0.38f)
      close()
    }
    drawPath(nosePath, color = Color(0xFFE11D48))

    // Porthole Window
    drawCircle(
      color = Color(0xFF38BDF8),
      radius = w * 0.11f,
      center = Offset(cx, cy + h * 0.02f)
    )
    drawCircle(
      color = Color(0xFF0284C7),
      radius = w * 0.085f,
      center = Offset(cx, cy + h * 0.02f)
    )
    drawCircle(
      color = Color.White,
      radius = w * 0.03f,
      center = Offset(cx - w * 0.03f, cy - h * 0.005f)
    )
  }
}

/**
 * Visual checkpoint bar where the rocket advances from planet start to goal planet
 */
@Composable
fun RocketProgressBar(
  currentStep: Int,
  totalSteps: Int,
  modifier: Modifier = Modifier
) {
  val animatedProgress by animateFloatAsState(
    targetValue = (currentStep.toFloat() / totalSteps.toFloat()).coerceIn(0f, 1f),
    animationSpec = tween(500, easing = FastOutSlowInEasing),
    label = "progress"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 6.dp),
    contentAlignment = Alignment.CenterStart
  ) {
    // Background track
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(14.dp)
        .shadow(3.dp, RoundedCornerShape(7.dp))
        .clip(RoundedCornerShape(7.dp))
        .background(Color(0xFF312E81))
        .border(1.5.dp, Color(0xFF6366F1).copy(alpha = 0.5f), RoundedCornerShape(7.dp))
    )

    // Glowing Fill track
    Box(
      modifier = Modifier
        .fillMaxWidth(fraction = animatedProgress)
        .height(14.dp)
        .clip(RoundedCornerShape(7.dp))
        .background(
          brush = Brush.horizontalGradient(
            colors = listOf(Color(0xFF38BDF8), Color(0xFFFACC15), Color(0xFFF97316))
          )
        )
    )

    // Checkpoint icons
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      for (step in 1..totalSteps) {
        val isPassed = step <= currentStep
        Box(
          modifier = Modifier
            .size(24.dp)
            .shadow(2.dp, CircleShape)
            .clip(CircleShape)
            .background(
              if (isPassed) Color(0xFFFACC15) else Color(0xFF1E1B4B)
            )
            .border(
              1.5.dp,
              if (isPassed) Color.White else Color(0xFF475569),
              CircleShape
            ),
          contentAlignment = Alignment.Center
        ) {
          if (step == totalSteps) {
            Text(text = "🪐", fontSize = 12.sp)
          } else {
            Text(
              text = if (isPassed) "⭐" else "•",
              fontSize = if (isPassed) 11.sp else 14.sp,
              color = Color.White
            )
          }
        }
      }
    }
  }
}
