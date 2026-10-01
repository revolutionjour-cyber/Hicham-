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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FredokaFontFamily

@Composable
fun CuteRocket(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 56.dp
) {
  val infiniteTransition = rememberInfiniteTransition(label = "rocket_fx")
  val flameScale by infiniteTransition.animateFloat(
    initialValue = 0.8f,
    targetValue = 1.35f,
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
    val cy = h * 0.44f

    // 1. Triple Flame Exhaust (Orange, Yellow, Cyan core)
    val flameLength = h * 0.38f * flameScale
    val outerFlame = Path().apply {
      moveTo(cx - w * 0.14f, cy + h * 0.28f)
      lineTo(cx, cy + h * 0.28f + flameLength)
      lineTo(cx + w * 0.14f, cy + h * 0.28f)
      close()
    }
    drawPath(
      outerFlame,
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFF9100), Color(0xFFFF3D00), Color.Transparent)
      )
    )

    val midFlame = Path().apply {
      moveTo(cx - w * 0.08f, cy + h * 0.28f)
      lineTo(cx, cy + h * 0.28f + flameLength * 0.65f)
      lineTo(cx + w * 0.08f, cy + h * 0.28f)
      close()
    }
    drawPath(midFlame, color = Color(0xFFFFEA00))

    val innerFlame = Path().apply {
      moveTo(cx - w * 0.04f, cy + h * 0.28f)
      lineTo(cx, cy + h * 0.28f + flameLength * 0.35f)
      lineTo(cx + w * 0.04f, cy + h * 0.28f)
      close()
    }
    drawPath(innerFlame, color = Color(0xFF00E5FF))

    // 2. Aerodynamic Stabilizer Fins (Coral Magenta)
    val leftFin = Path().apply {
      moveTo(cx - w * 0.18f, cy + h * 0.06f)
      lineTo(cx - w * 0.4f, cy + h * 0.3f)
      lineTo(cx - w * 0.18f, cy + h * 0.26f)
      close()
    }
    drawPath(leftFin, color = Color(0xFFF43F5E))

    val rightFin = Path().apply {
      moveTo(cx + w * 0.18f, cy + h * 0.06f)
      lineTo(cx + w * 0.4f, cy + h * 0.3f)
      lineTo(cx + w * 0.18f, cy + h * 0.26f)
      close()
    }
    drawPath(rightFin, color = Color(0xFFF43F5E))

    // 3. Rocket Fuselage (Glossy Ceramic White)
    val bodyPath = Path().apply {
      moveTo(cx, cy - h * 0.4f)
      cubicTo(cx + w * 0.25f, cy - h * 0.15f, cx + w * 0.25f, cy + h * 0.2f, cx + w * 0.18f, cy + h * 0.28f)
      lineTo(cx - w * 0.18f, cy + h * 0.28f)
      cubicTo(cx - w * 0.25f, cy + h * 0.2f, cx - w * 0.25f, cy - h * 0.15f, cx, cy - h * 0.4f)
      close()
    }
    drawPath(
      bodyPath,
      brush = Brush.horizontalGradient(
        colors = listOf(Color(0xFFE2E8F0), Color.White, Color(0xFFCBD5E1))
      )
    )

    // 4. Nose Cone (Coral Red)
    val nosePath = Path().apply {
      moveTo(cx, cy - h * 0.4f)
      cubicTo(cx + w * 0.15f, cy - h * 0.26f, cx + w * 0.19f, cy - h * 0.15f, cx + w * 0.2f, cy - h * 0.1f)
      lineTo(cx - w * 0.2f, cy - h * 0.1f)
      cubicTo(cx - w * 0.19f, cy - h * 0.15f, cx - w * 0.15f, cy - h * 0.26f, cx, cy - h * 0.4f)
      close()
    }
    drawPath(nosePath, color = Color(0xFFE11D48))

    // 5. Porthole Window with Glass Glint
    drawCircle(
      color = Color(0xFF0284C7),
      radius = w * 0.12f,
      center = Offset(cx, cy + h * 0.02f)
    )
    drawCircle(
      color = Color(0xFF38BDF8),
      radius = w * 0.09f,
      center = Offset(cx, cy + h * 0.02f)
    )
    drawCircle(
      color = Color.White,
      radius = w * 0.035f,
      center = Offset(cx - w * 0.035f, cy - h * 0.01f)
    )
  }
}

/**
 * Checkpoint progress bar where the rocket advances from start to destination planet
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
      .padding(horizontal = 16.dp, vertical = 6.dp),
    contentAlignment = Alignment.CenterStart
  ) {
    // 1. Frosted Glass Background Track
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(16.dp)
        .shadow(4.dp, RoundedCornerShape(8.dp))
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0x351E1B4B))
        .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(8.dp))
    )

    // 2. Radiant Progress Fill
    Box(
      modifier = Modifier
        .fillMaxWidth(fraction = animatedProgress)
        .height(16.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(
          brush = Brush.horizontalGradient(
            colors = listOf(Color(0xFF38BDF8), Color(0xFFFBBF24), Color(0xFFF97316))
          )
        )
    )

    // 3. Staggered Checkpoint Stars along track
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      for (step in 1..totalSteps) {
        val isPassed = step <= currentStep
        Box(
          modifier = Modifier
            .size(28.dp)
            .shadow(if (isPassed) 6.dp else 2.dp, CircleShape, spotColor = Color(0x80F59E0B))
            .clip(CircleShape)
            .background(
              if (isPassed) Color(0xFFFBBF24) else Color(0xFF1E1B4B)
            )
            .border(
              1.5.dp,
              if (isPassed) Color.White else Color(0x40FFFFFF),
              CircleShape
            ),
          contentAlignment = Alignment.Center
        ) {
          if (step == totalSteps) {
            IllustratedPlanetOrb(sizeDp = 18.dp, level = 2)
          } else {
            if (isPassed) {
              IllustratedStar(sizeDp = 16.dp, isFilled = true)
            } else {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF94A3B8))
              )
            }
          }
        }
      }
    }
  }
}
