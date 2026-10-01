package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlin.random.Random

private data class StaticDoodle(
  val xRatio: Float,
  val yRatio: Float,
  val size: Float,
  val color: Color,
  val isStar: Boolean
)

@Composable
fun PlayfulCosmicBackground(modifier: Modifier = Modifier) {
  val doodles = remember {
    val rnd = Random(2026)
    val pastelColors = listOf(
      Color(0x3038BDF8), // Soft Cyan
      Color(0x30FBBF24), // Soft Gold
      Color(0x30F472B6), // Soft Pink
      Color(0x30A78BFA), // Soft Lavender
      Color(0x3034D399)  // Soft Mint
    )
    List(18) {
      StaticDoodle(
        xRatio = rnd.nextFloat(),
        yRatio = rnd.nextFloat(),
        size = rnd.nextFloat() * 16f + 10f,
        color = pastelColors[rnd.nextInt(pastelColors.size)],
        isStar = rnd.nextBoolean()
      )
    }
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    // 1. Fresh uplifting modern sky gradient
    drawRect(
      brush = Brush.verticalGradient(
        colors = listOf(
          Color(0xFFE0F2FE), // Airy Pale Cyan
          Color(0xFFF0FDF4), // Fresh Mint hint
          Color(0xFFFAF5FF), // Soft Lilac
          Color(0xFFF8FAFC)  // Clean Base
        )
      )
    )

    // 2. Soft organic background blobs
    drawCircle(
      color = Color(0x1838BDF8),
      radius = w * 0.55f,
      center = Offset(w * 0.15f, h * 0.2f)
    )
    drawCircle(
      color = Color(0x18FBBF24),
      radius = w * 0.45f,
      center = Offset(w * 0.85f, h * 0.45f)
    )
    drawCircle(
      color = Color(0x15A78BFA),
      radius = w * 0.6f,
      center = Offset(w * 0.5f, h * 0.85f)
    )

    // 3. Subtle floating doodles (Zero CPU overhead, instantaneous rendering)
    doodles.forEach { doodle ->
      val x = doodle.xRatio * w
      val y = doodle.yRatio * h

      if (doodle.isStar) {
        val r = doodle.size
        val starPath = Path().apply {
          moveTo(x, y - r)
          cubicTo(x, y - r * 0.3f, x + r * 0.3f, y, x + r, y)
          cubicTo(x + r * 0.3f, y, x, y + r * 0.3f, x, y + r)
          cubicTo(x, y + r * 0.3f, x - r * 0.3f, y, x - r, y)
          cubicTo(x - r * 0.3f, y, x, y - r * 0.3f, x, y - r)
          close()
        }
        drawPath(starPath, color = doodle.color)
      } else {
        drawCircle(
          color = doodle.color,
          radius = doodle.size * 0.5f,
          center = Offset(x, y)
        )
      }
    }
  }
}
