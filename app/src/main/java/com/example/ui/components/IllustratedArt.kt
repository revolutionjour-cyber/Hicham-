package com.example.ui.components

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.TangibleItemType
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Realistic 3D Shiny Red Apple with stem and leaf.
 */
@Composable
fun IllustratedShinyApple(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 44.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Soft cast shadow
    drawOval(
      color = Color(0x35000000),
      topLeft = Offset(w * 0.2f, h * 0.88f),
      size = Size(w * 0.6f, h * 0.12f)
    )

    // Curved Brown Stem
    val stemPath = Path().apply {
      moveTo(cx, h * 0.32f)
      cubicTo(cx - w * 0.05f, h * 0.18f, cx + w * 0.08f, h * 0.10f, cx + w * 0.12f, h * 0.04f)
    }
    drawPath(
      path = stemPath,
      color = Color(0xFF5D4037),
      style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
    )

    // Green Leaf
    val leafPath = Path().apply {
      moveTo(cx + w * 0.04f, h * 0.16f)
      cubicTo(cx + w * 0.28f, h * 0.08f, cx + w * 0.38f, h * 0.16f, cx + w * 0.40f, h * 0.24f)
      cubicTo(cx + w * 0.28f, h * 0.28f, cx + w * 0.12f, h * 0.22f, cx + w * 0.04f, h * 0.16f)
      close()
    }
    drawPath(
      path = leafPath,
      brush = Brush.linearGradient(
        colors = listOf(Color(0xFF4ADE80), Color(0xFF16A34A)),
        start = Offset(cx, h * 0.1f),
        end = Offset(cx + w * 0.4f, h * 0.25f)
      )
    )

    // Plump Apple Body (Realistic organic apple lobes)
    val applePath = Path().apply {
      moveTo(cx, h * 0.34f)
      // Top right dip to right cheek
      cubicTo(cx + w * 0.35f, h * 0.24f, w * 0.94f, h * 0.44f, w * 0.90f, h * 0.68f)
      // Right cheek to bottom lobe
      cubicTo(w * 0.86f, h * 0.88f, cx + w * 0.18f, h * 0.95f, cx, h * 0.90f)
      // Bottom lobe to left cheek
      cubicTo(cx - w * 0.18f, h * 0.95f, w * 0.14f, h * 0.88f, w * 0.10f, h * 0.68f)
      // Left cheek to top dip
      cubicTo(w * 0.06f, h * 0.44f, cx - w * 0.35f, h * 0.24f, cx, h * 0.34f)
      close()
    }

    drawPath(
      path = applePath,
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFF4D4D), Color(0xFFDC2626), Color(0xFF991B1B)),
        center = Offset(cx - w * 0.15f, h * 0.48f),
        radius = w * 0.6f
      )
    )

    // Specular Glass Highlight Curve
    val highlightPath = Path().apply {
      moveTo(cx - w * 0.28f, h * 0.42f)
      cubicTo(cx - w * 0.32f, h * 0.52f, cx - w * 0.24f, h * 0.64f, cx - w * 0.16f, h * 0.70f)
    }
    drawPath(
      path = highlightPath,
      color = Color(0x99FFFFFF),
      style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
    )

    // Small bright gleam dot
    drawCircle(
      color = Color.White,
      radius = w * 0.045f,
      center = Offset(cx - w * 0.22f, h * 0.38f)
    )
  }
}

/**
 * 3D Golden Star with beveled facets and gleaming sheen.
 */
@Composable
fun IllustratedStar(
  modifier: Modifier = Modifier,
  isFilled: Boolean = true,
  sizeDp: Dp = 24.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f
    val cy = h * 0.5f
    val outerR = w * 0.48f
    val innerR = outerR * 0.42f

    val path = Path()
    val points = 5
    val step = PI / points
    var angle = -PI / 2.0

    for (i in 0 until points * 2) {
      val r = if (i % 2 == 0) outerR else innerR
      val x = (cx + r * cos(angle)).toFloat()
      val y = (cy + r * sin(angle)).toFloat()
      if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
      angle += step
    }
    path.close()

    if (isFilled) {
      drawPath(
        path = path,
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xFFFFF59D), Color(0xFFFBBF24), Color(0xFFD97706))
        )
      )
      drawPath(
        path = path,
        color = Color(0xFFB45309),
        style = Stroke(width = w * 0.05f, join = StrokeJoin.Round)
      )
      // Specular highlight
      drawCircle(
        color = Color(0xA0FFFFFF),
        radius = outerR * 0.22f,
        center = Offset(cx - outerR * 0.22f, cy - outerR * 0.22f)
      )
    } else {
      drawPath(
        path = path,
        color = Color(0xFFCBD5E1),
        style = Stroke(width = w * 0.08f, join = StrokeJoin.Round)
      )
    }
  }
}

/**
 * Tangible Glowing Space Energy Battery.
 */
@Composable
fun IllustratedEnergyBattery(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 44.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Positive terminal cap
    drawRoundRect(
      color = Color(0xFF94A3B8),
      topLeft = Offset(cx - w * 0.14f, h * 0.06f),
      size = Size(w * 0.28f, h * 0.12f),
      cornerRadius = CornerRadius(4f, 4f)
    )

    // Battery Shell
    drawRoundRect(
      brush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF1E293B), Color(0xFF334155), Color(0xFF1E293B))
      ),
      topLeft = Offset(w * 0.15f, h * 0.16f),
      size = Size(w * 0.7f, h * 0.78f),
      cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
    )

    // Inner Glowing Cyan Power Chambers
    val pad = w * 0.08f
    val barW = w * 0.7f - pad * 2
    val barH = h * 0.18f
    for (i in 0..2) {
      val barY = h * 0.22f + i * (barH + h * 0.05f)
      drawRoundRect(
        brush = Brush.horizontalGradient(
          colors = listOf(Color(0xFF38BDF8), Color(0xFF00F0FF), Color(0xFF0284C7))
        ),
        topLeft = Offset(w * 0.15f + pad, barY),
        size = Size(barW, barH),
        cornerRadius = CornerRadius(6f, 6f)
      )
    }

    // Glass sheen
    drawLine(
      color = Color(0x60FFFFFF),
      start = Offset(w * 0.22f, h * 0.2f),
      end = Offset(w * 0.22f, h * 0.86f),
      strokeWidth = w * 0.05f,
      cap = StrokeCap.Round
    )
  }
}

/**
 * Faceted Shimmering Crystal Gem.
 */
@Composable
fun IllustratedGem(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 44.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    val gemPath = Path().apply {
      moveTo(cx, h * 0.94f)
      lineTo(w * 0.90f, h * 0.38f)
      lineTo(w * 0.74f, h * 0.10f)
      lineTo(w * 0.26f, h * 0.10f)
      lineTo(w * 0.10f, h * 0.38f)
      close()
    }

    drawPath(
      path = gemPath,
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF075985))
      )
    )

    // Facet lines
    drawLine(
      color = Color(0x70FFFFFF),
      start = Offset(w * 0.10f, h * 0.38f),
      end = Offset(w * 0.90f, h * 0.38f),
      strokeWidth = w * 0.04f
    )
    drawLine(
      color = Color(0x80FFFFFF),
      start = Offset(w * 0.26f, h * 0.10f),
      end = Offset(cx, h * 0.94f),
      strokeWidth = w * 0.03f
    )
    drawLine(
      color = Color(0x80FFFFFF),
      start = Offset(w * 0.74f, h * 0.10f),
      end = Offset(cx, h * 0.94f),
      strokeWidth = w * 0.03f
    )

    // Glint
    drawCircle(color = Color.White, radius = w * 0.08f, center = Offset(w * 0.32f, h * 0.28f))
  }
}

/**
 * Chunky Embossed 3D Golden Coin.
 */
@Composable
fun IllustratedCoin(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 24.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f
    val cy = h * 0.5f
    val r = w * 0.46f

    drawCircle(
      color = Color(0xFFB45309),
      radius = r,
      center = Offset(cx, cy + h * 0.05f)
    )

    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFFF59D), Color(0xFFFBBF24), Color(0xFFD97706)),
        center = Offset(cx - r * 0.3f, cy - r * 0.3f),
        radius = r * 1.3f
      ),
      radius = r,
      center = Offset(cx, cy)
    )

    drawCircle(
      color = Color(0xFFD97706),
      radius = r,
      center = Offset(cx, cy),
      style = Stroke(width = w * 0.06f)
    )

    drawCircle(
      color = Color(0xFFFFFBEB),
      radius = r * 0.72f,
      center = Offset(cx, cy),
      style = Stroke(width = w * 0.04f)
    )

    // Center 1
    val starR = r * 0.38f
    val starInner = starR * 0.42f
    val path = Path()
    var angle = -PI / 2.0
    val step = PI / 5
    for (i in 0 until 10) {
      val cr = if (i % 2 == 0) starR else starInner
      val x = (cx + cr * cos(angle)).toFloat()
      val y = (cy + cr * sin(angle)).toFloat()
      if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
      angle += step
    }
    path.close()
    drawPath(path, color = Color(0xFFD97706))
  }
}

/**
 * Golden Championship Trophy.
 */
@Composable
fun IllustratedTrophy(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 32.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Pedestal
    drawRoundRect(
      brush = Brush.horizontalGradient(
        colors = listOf(Color(0xFFB45309), Color(0xFFD97706), Color(0xFFB45309))
      ),
      topLeft = Offset(w * 0.22f, h * 0.78f),
      size = Size(w * 0.56f, h * 0.18f),
      cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )
    drawRoundRect(
      color = Color(0xFFD97706),
      topLeft = Offset(cx - w * 0.08f, h * 0.62f),
      size = Size(w * 0.16f, h * 0.18f),
      cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
    )

    // Cup Body
    val cupPath = Path().apply {
      moveTo(w * 0.2f, h * 0.15f)
      lineTo(w * 0.8f, h * 0.15f)
      lineTo(w * 0.72f, h * 0.48f)
      cubicTo(w * 0.7f, h * 0.64f, w * 0.3f, h * 0.64f, w * 0.28f, h * 0.48f)
      close()
    }
    drawPath(
      path = cupPath,
      brush = Brush.horizontalGradient(
        colors = listOf(Color(0xFFF59E0B), Color(0xFFFFF070), Color(0xFFF59E0B))
      )
    )

    // Handles
    drawArc(
      color = Color(0xFFD97706),
      startAngle = 120f,
      sweepAngle = 180f,
      useCenter = false,
      topLeft = Offset(w * 0.05f, h * 0.2f),
      size = Size(w * 0.24f, h * 0.32f),
      style = Stroke(width = w * 0.07f, cap = StrokeCap.Round)
    )
    drawArc(
      color = Color(0xFFD97706),
      startAngle = -60f,
      sweepAngle = 180f,
      useCenter = false,
      topLeft = Offset(w * 0.71f, h * 0.2f),
      size = Size(w * 0.24f, h * 0.32f),
      style = Stroke(width = w * 0.07f, cap = StrokeCap.Round)
    )
  }
}

/**
 * Planetary Orb with ring.
 */
@Composable
fun IllustratedPlanetOrb(
  modifier: Modifier = Modifier,
  level: Int = 1,
  sizeDp: Dp = 32.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f
    val cy = h * 0.5f
    val r = w * 0.34f

    val (pColors, ringColor) = when (level) {
      1 -> Pair(listOf(Color(0xFF38BDF8), Color(0xFF0284C7)), Color(0xFF7DD3FC))
      2 -> Pair(listOf(Color(0xFFFB923C), Color(0xFFEA580C)), Color(0xFFFED7AA))
      else -> Pair(listOf(Color(0xFFC084FC), Color(0xFF7E22CE)), Color(0xFFE9D5FF))
    }

    drawArc(
      color = ringColor.copy(alpha = 0.5f),
      startAngle = 170f,
      sweepAngle = 200f,
      useCenter = false,
      topLeft = Offset(cx - r * 1.5f, cy - r * 0.45f),
      size = Size(r * 3f, r * 0.9f),
      style = Stroke(width = w * 0.07f)
    )

    drawCircle(
      brush = Brush.radialGradient(
        colors = pColors,
        center = Offset(cx - r * 0.3f, cy - r * 0.3f),
        radius = r * 1.4f
      ),
      radius = r,
      center = Offset(cx, cy)
    )

    drawArc(
      color = ringColor,
      startAngle = -10f,
      sweepAngle = 200f,
      useCenter = false,
      topLeft = Offset(cx - r * 1.5f, cy - r * 0.45f),
      size = Size(r * 3f, r * 0.9f),
      style = Stroke(width = w * 0.07f)
    )
  }
}

/**
 * Cartoon Robot with friendly screen visor.
 */
@Composable
fun IllustratedRobot(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 32.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    drawLine(
      color = Color(0xFF94A3B8),
      start = Offset(cx, h * 0.28f),
      end = Offset(cx, h * 0.08f),
      strokeWidth = w * 0.06f
    )
    drawCircle(color = Color(0xFFF43F5E), radius = w * 0.08f, center = Offset(cx, h * 0.08f))

    drawRoundRect(
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1))
      ),
      topLeft = Offset(w * 0.18f, h * 0.25f),
      size = Size(w * 0.64f, h * 0.62f),
      cornerRadius = CornerRadius(w * 0.16f, w * 0.16f)
    )

    drawRoundRect(
      color = Color(0xFF0F172A),
      topLeft = Offset(w * 0.26f, h * 0.35f),
      size = Size(w * 0.48f, h * 0.34f),
      cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
    )

    drawCircle(color = Color(0xFF38BDF8), radius = w * 0.06f, center = Offset(w * 0.38f, h * 0.52f))
    drawCircle(color = Color(0xFF38BDF8), radius = w * 0.06f, center = Offset(w * 0.62f, h * 0.52f))
  }
}

/**
 * Metallic Padlock for locked stages.
 */
@Composable
fun IllustratedLock(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 24.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    drawArc(
      color = Color(0xFF94A3B8),
      startAngle = 180f,
      sweepAngle = 180f,
      useCenter = false,
      topLeft = Offset(w * 0.28f, h * 0.12f),
      size = Size(w * 0.44f, h * 0.48f),
      style = Stroke(width = w * 0.1f, cap = StrokeCap.Round)
    )

    drawRoundRect(
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFBBF24), Color(0xFFD97706))
      ),
      topLeft = Offset(w * 0.2f, h * 0.42f),
      size = Size(w * 0.6f, h * 0.48f),
      cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
    )

    drawCircle(color = Color(0xFF78350F), radius = w * 0.07f, center = Offset(cx, h * 0.62f))
  }
}

/**
 * Subtraction Cross Indicator.
 */
@Composable
fun IllustratedCross(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 20.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    drawLine(
      color = Color(0xFFF43F5E),
      start = Offset(w * 0.2f, h * 0.2f),
      end = Offset(w * 0.8f, h * 0.8f),
      strokeWidth = w * 0.18f,
      cap = StrokeCap.Round
    )
    drawLine(
      color = Color(0xFFF43F5E),
      start = Offset(w * 0.8f, h * 0.2f),
      end = Offset(w * 0.2f, h * 0.8f),
      strokeWidth = w * 0.18f,
      cap = StrokeCap.Round
    )
  }
}

/**
 * Realistic 3D Orange with leaf and stem.
 */
@Composable
fun IllustratedSweetOrange(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 44.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    drawOval(
      color = Color(0x35000000),
      topLeft = Offset(w * 0.2f, h * 0.88f),
      size = Size(w * 0.6f, h * 0.12f)
    )

    drawLine(
      color = Color(0xFF5D4037),
      start = Offset(cx, h * 0.25f),
      end = Offset(cx + w * 0.05f, h * 0.12f),
      strokeWidth = w * 0.08f,
      cap = StrokeCap.Round
    )

    val leaf = Path().apply {
      moveTo(cx + w * 0.02f, h * 0.18f)
      cubicTo(cx + w * 0.25f, h * 0.10f, cx + w * 0.35f, h * 0.20f, cx + w * 0.02f, h * 0.24f)
      close()
    }
    drawPath(leaf, color = Color(0xFF22C55E))

    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFDBA74), Color(0xFFF97316), Color(0xFFEA580C)),
        center = Offset(cx - w * 0.12f, h * 0.48f),
        radius = w * 0.42f
      ),
      radius = w * 0.38f,
      center = Offset(cx, h * 0.56f)
    )

    drawOval(
      color = Color(0x55FFFFFF),
      topLeft = Offset(cx - w * 0.22f, h * 0.38f),
      size = Size(w * 0.22f, h * 0.14f)
    )
  }
}

/**
 * Cheerful curved Yellow Banana.
 */
@Composable
fun IllustratedBanana(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 44.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height

    drawOval(
      color = Color(0x30000000),
      topLeft = Offset(w * 0.15f, h * 0.88f),
      size = Size(w * 0.7f, h * 0.12f)
    )

    val bananaPath = Path().apply {
      moveTo(w * 0.18f, h * 0.25f)
      cubicTo(w * 0.12f, h * 0.65f, w * 0.55f, h * 0.88f, w * 0.82f, h * 0.72f)
      cubicTo(w * 0.62f, h * 0.75f, w * 0.26f, h * 0.60f, w * 0.22f, h * 0.30f)
      close()
    }
    drawPath(
      path = bananaPath,
      brush = Brush.linearGradient(
        colors = listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFEAB308)),
        start = Offset(w * 0.2f, h * 0.3f),
        end = Offset(w * 0.7f, h * 0.8f)
      )
    )

    drawCircle(
      color = Color(0xFF854D0E),
      radius = w * 0.05f,
      center = Offset(w * 0.18f, h * 0.25f)
    )

    drawCircle(
      color = Color(0xFF713F12),
      radius = w * 0.04f,
      center = Offset(w * 0.82f, h * 0.72f)
    )
  }
}

/**
 * Cute Red Strawberry with seeds and green leafy top.
 */
@Composable
fun IllustratedStrawberry(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 44.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    drawOval(
      color = Color(0x30000000),
      topLeft = Offset(w * 0.2f, h * 0.88f),
      size = Size(w * 0.6f, h * 0.12f)
    )

    val body = Path().apply {
      moveTo(cx, h * 0.85f)
      cubicTo(w * 0.12f, h * 0.65f, w * 0.15f, h * 0.35f, cx, h * 0.30f)
      cubicTo(w * 0.85f, h * 0.35f, w * 0.88f, h * 0.65f, cx, h * 0.85f)
      close()
    }
    drawPath(
      path = body,
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFF87171), Color(0xFFEF4444), Color(0xFFDC2626)),
        center = Offset(cx - w * 0.1f, h * 0.45f),
        radius = w * 0.45f
      )
    )

    val seedColor = Color(0xFFFEF08A)
    drawCircle(seedColor, radius = w * 0.025f, center = Offset(cx - w * 0.15f, h * 0.50f))
    drawCircle(seedColor, radius = w * 0.025f, center = Offset(cx + w * 0.15f, h * 0.50f))
    drawCircle(seedColor, radius = w * 0.025f, center = Offset(cx, h * 0.62f))
    drawCircle(seedColor, radius = w * 0.025f, center = Offset(cx - w * 0.08f, h * 0.72f))
    drawCircle(seedColor, radius = w * 0.025f, center = Offset(cx + w * 0.08f, h * 0.72f))

    val cap = Path().apply {
      moveTo(cx, h * 0.30f)
      lineTo(cx - w * 0.22f, h * 0.22f)
      lineTo(cx - w * 0.08f, h * 0.30f)
      lineTo(cx, h * 0.18f)
      lineTo(cx + w * 0.08f, h * 0.30f)
      lineTo(cx + w * 0.22f, h * 0.22f)
      close()
    }
    drawPath(cap, color = Color(0xFF22C55E))
  }
}

/**
 * Juicy Purple Grapes cluster.
 */
@Composable
fun IllustratedGrapes(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 44.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    drawOval(
      color = Color(0x30000000),
      topLeft = Offset(w * 0.2f, h * 0.88f),
      size = Size(w * 0.6f, h * 0.12f)
    )

    drawLine(
      color = Color(0xFF65A30D),
      start = Offset(cx, h * 0.25f),
      end = Offset(cx + w * 0.08f, h * 0.12f),
      strokeWidth = w * 0.07f,
      cap = StrokeCap.Round
    )

    val grapeRadius = w * 0.14f
    val grapeColor1 = Color(0xFFA855F7)
    val grapeColor2 = Color(0xFF7E22CE)

    fun drawGrape(gx: Float, gy: Float) {
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(grapeColor1, grapeColor2),
          center = Offset(gx - grapeRadius * 0.3f, gy - grapeRadius * 0.3f),
          radius = grapeRadius
        ),
        radius = grapeRadius,
        center = Offset(gx, gy)
      )
    }

    drawGrape(cx - w * 0.18f, h * 0.36f)
    drawGrape(cx, h * 0.34f)
    drawGrape(cx + w * 0.18f, h * 0.36f)
    drawGrape(cx - w * 0.10f, h * 0.52f)
    drawGrape(cx + w * 0.10f, h * 0.52f)
    drawGrape(cx, h * 0.70f)
  }
}

/**
 * Refreshing Watermelon Slice with seeds.
 */
@Composable
fun IllustratedWatermelon(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 44.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    drawOval(
      color = Color(0x30000000),
      topLeft = Offset(w * 0.15f, h * 0.88f),
      size = Size(w * 0.7f, h * 0.12f)
    )

    val rind = Path().apply {
      moveTo(w * 0.15f, h * 0.40f)
      cubicTo(w * 0.25f, h * 0.85f, w * 0.75f, h * 0.85f, w * 0.85f, h * 0.40f)
      lineTo(w * 0.80f, h * 0.40f)
      cubicTo(w * 0.70f, h * 0.78f, w * 0.30f, h * 0.78f, w * 0.20f, h * 0.40f)
      close()
    }
    drawPath(rind, color = Color(0xFF16A34A))

    val flesh = Path().apply {
      moveTo(w * 0.20f, h * 0.40f)
      cubicTo(w * 0.30f, h * 0.76f, w * 0.70f, h * 0.76f, w * 0.80f, h * 0.40f)
      close()
    }
    drawPath(
      path = flesh,
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFB7185), Color(0xFFF43F5E), Color(0xFFE11D48)),
        center = Offset(cx, h * 0.52f),
        radius = w * 0.35f
      )
    )

    val seedColor = Color(0xFF1E293B)
    drawOval(seedColor, topLeft = Offset(cx - w * 0.12f, h * 0.48f), size = Size(w * 0.05f, h * 0.08f))
    drawOval(seedColor, topLeft = Offset(cx + w * 0.08f, h * 0.48f), size = Size(w * 0.05f, h * 0.08f))
    drawOval(seedColor, topLeft = Offset(cx - w * 0.02f, h * 0.58f), size = Size(w * 0.05f, h * 0.08f))
  }
}

/**
 * Master tangible item renderer.
 */
@Composable
fun IllustratedItemIcon(
  itemType: TangibleItemType,
  modifier: Modifier = Modifier,
  sizeDp: Dp = 44.dp
) {
  when (itemType) {
    TangibleItemType.SHINY_APPLE -> IllustratedShinyApple(modifier = modifier, sizeDp = sizeDp)
    TangibleItemType.SWEET_ORANGE -> IllustratedSweetOrange(modifier = modifier, sizeDp = sizeDp)
    TangibleItemType.YELLOW_BANANA -> IllustratedBanana(modifier = modifier, sizeDp = sizeDp)
    TangibleItemType.RED_STRAWBERRY -> IllustratedStrawberry(modifier = modifier, sizeDp = sizeDp)
    TangibleItemType.PURPLE_GRAPES -> IllustratedGrapes(modifier = modifier, sizeDp = sizeDp)
    TangibleItemType.JUICY_WATERMELON -> IllustratedWatermelon(modifier = modifier, sizeDp = sizeDp)

    // Fallbacks
    TangibleItemType.GOLDEN_STAR -> IllustratedStar(modifier = modifier, isFilled = true, sizeDp = sizeDp)
    TangibleItemType.ENERGY_BATTERY -> IllustratedEnergyBattery(modifier = modifier, sizeDp = sizeDp)
    TangibleItemType.MAGIC_CRYSTAL -> IllustratedGem(modifier = modifier, sizeDp = sizeDp)
    TangibleItemType.GOLD_COIN -> IllustratedCoin(modifier = modifier, sizeDp = sizeDp)
  }
}
