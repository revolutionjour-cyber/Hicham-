package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CartoonPlanet(
  level: Int,
  modifier: Modifier = Modifier,
  sizeDp: Dp = 90.dp
) {
  val infiniteTransition = rememberInfiniteTransition(label = "planet_rot")
  val rotOffset by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(12000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "ring_spin"
  )

  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val center = Offset(w * 0.5f, h * 0.5f)
    val radius = w * 0.36f

    when (level) {
      1 -> {
        // Earth / Moon friendly Blue & Green
        // Outer glow
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0x6038BDF8), Color(0x0038BDF8)),
            center = center,
            radius = radius * 1.3f
          ),
          center = center,
          radius = radius * 1.3f
        )
        // Planet body
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF0F172A)),
            center = Offset(center.x - radius * 0.3f, center.y - radius * 0.3f),
            radius = radius * 1.4f
          ),
          center = center,
          radius = radius
        )
        // Continents / green craters
        drawCircle(
          color = Color(0xFF4ADE80),
          radius = radius * 0.28f,
          center = Offset(center.x - radius * 0.3f, center.y - radius * 0.2f)
        )
        drawCircle(
          color = Color(0xFF22C55E),
          radius = radius * 0.22f,
          center = Offset(center.x + radius * 0.32f, center.y + radius * 0.25f)
        )
        // Cute smiling face
        drawCircle(
          color = Color(0xFF0F172A),
          radius = radius * 0.08f,
          center = Offset(center.x - radius * 0.2f, center.y + radius * 0.05f)
        )
        drawCircle(
          color = Color(0xFF0F172A),
          radius = radius * 0.08f,
          center = Offset(center.x + radius * 0.2f, center.y + radius * 0.05f)
        )
        // White glint
        drawCircle(
          color = Color.White,
          radius = radius * 0.035f,
          center = Offset(center.x - radius * 0.22f, center.y + radius * 0.03f)
        )
        drawCircle(
          color = Color.White,
          radius = radius * 0.035f,
          center = Offset(center.x + radius * 0.18f, center.y + radius * 0.03f)
        )
        // Smile arc
        drawArc(
          color = Color(0xFF0F172A),
          startAngle = 10f,
          sweepAngle = 160f,
          useCenter = false,
          topLeft = Offset(center.x - radius * 0.16f, center.y + radius * 0.12f),
          size = Size(radius * 0.32f, radius * 0.2f),
          style = Stroke(width = 3.5f)
        )
      }
      2 -> {
        // Mars Explorer Red & Orange with rings
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0x60F97316), Color(0x00F97316)),
            center = center,
            radius = radius * 1.3f
          ),
          center = center,
          radius = radius * 1.3f
        )
        // Back of ring
        drawArc(
          color = Color(0xFFFDE047),
          startAngle = 180f,
          sweepAngle = 180f,
          useCenter = false,
          topLeft = Offset(center.x - radius * 1.4f, center.y - radius * 0.45f),
          size = Size(radius * 2.8f, radius * 0.9f),
          style = Stroke(width = 6f)
        )
        // Planet body
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFB923C), Color(0xFFEA580C), Color(0xFF7C2D12)),
            center = Offset(center.x - radius * 0.3f, center.y - radius * 0.3f),
            radius = radius * 1.3f
          ),
          center = center,
          radius = radius
        )
        // Craters
        drawCircle(
          color = Color(0xFF9A3412),
          radius = radius * 0.22f,
          center = Offset(center.x - radius * 0.35f, center.y + radius * 0.1f)
        )
        drawCircle(
          color = Color(0xFFC2410C),
          radius = radius * 0.16f,
          center = Offset(center.x + radius * 0.25f, center.y - radius * 0.28f)
        )
        // Front of ring
        drawArc(
          color = Color(0xFFFDE047),
          startAngle = 0f,
          sweepAngle = 180f,
          useCenter = false,
          topLeft = Offset(center.x - radius * 1.4f, center.y - radius * 0.45f),
          size = Size(radius * 2.8f, radius * 0.9f),
          style = Stroke(width = 6f)
        )
      }
      3 -> {
        // Deep Galaxy Crystal Violet
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0x80C084FC), Color(0x00C084FC)),
            center = center,
            radius = radius * 1.4f
          ),
          center = center,
          radius = radius * 1.4f
        )
        // Multi-color planetary ring
        drawArc(
          brush = Brush.linearGradient(
            colors = listOf(Color(0xFFE879F9), Color(0xFF38BDF8), Color(0xFFFDE047))
          ),
          startAngle = 160f,
          sweepAngle = 200f,
          useCenter = false,
          topLeft = Offset(center.x - radius * 1.5f, center.y - radius * 0.5f),
          size = Size(radius * 3f, radius * 1f),
          style = Stroke(width = 8f)
        )
        // Planet body
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFFE879F9), Color(0xFF8B5CF6), Color(0xFF3B0764)),
            center = Offset(center.x - radius * 0.25f, center.y - radius * 0.25f),
            radius = radius * 1.3f
          ),
          center = center,
          radius = radius
        )
        // Glowing star crystals on planet
        drawCircle(
          color = Color(0xFFFDE047),
          radius = radius * 0.1f,
          center = Offset(center.x + radius * 0.15f, center.y - radius * 0.15f)
        )
        drawCircle(
          color = Color.White,
          radius = radius * 0.08f,
          center = Offset(center.x - radius * 0.3f, center.y + radius * 0.25f)
        )
      }
    }
  }
}
