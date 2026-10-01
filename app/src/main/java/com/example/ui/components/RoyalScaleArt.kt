package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class LuxuryGemType(val titleAr: String, val singularAr: String) {
  RUBY("ياقوت أحمر ملكي", "حبة ياقوت"),
  EMERALD("زمرد أخضر فاخر", "حبة زمرد"),
  SAPPHIRE("ياقوت أزرق سماوي", "حبة ياقوت"),
  TOPAZ("ألماس ذهبي مشع", "حبة ألماس")
}

/**
 * Cut faceted Imperial Ruby (Octagonal cut)
 */
@Composable
fun RoyalRubyGem(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 46.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Soft radiant red aura
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0x50F43F5E), Color(0x00F43F5E)),
        center = Offset(cx, h * 0.5f),
        radius = w * 0.55f
      ),
      radius = w * 0.55f,
      center = Offset(cx, h * 0.5f)
    )

    // Shadow
    drawOval(
      color = Color(0x35000000),
      topLeft = Offset(w * 0.15f, h * 0.84f),
      size = Size(w * 0.7f, h * 0.16f)
    )

    // Faceted Ruby Polygon
    val p1 = Offset(w * 0.28f, h * 0.12f)
    val p2 = Offset(w * 0.72f, h * 0.12f)
    val p3 = Offset(w * 0.92f, h * 0.40f)
    val p4 = Offset(cx, h * 0.90f)
    val p5 = Offset(w * 0.08f, h * 0.40f)

    // Base body
    val body = Path().apply {
      moveTo(p1.x, p1.y)
      lineTo(p2.x, p2.y)
      lineTo(p3.x, p3.y)
      lineTo(p4.x, p4.y)
      lineTo(p5.x, p5.y)
      close()
    }
    drawPath(
      body,
      brush = Brush.verticalGradient(
        listOf(Color(0xFFFB7185), Color(0xFFE11D48), Color(0xFF881337))
      )
    )

    // Inner Facets
    val tableCenter = Offset(cx, h * 0.38f)
    val facet1 = Path().apply {
      moveTo(p1.x, p1.y); lineTo(p2.x, p2.y); lineTo(tableCenter.x, tableCenter.y); close()
    }
    drawPath(facet1, color = Color(0x40FFFFFF))

    val facetLeft = Path().apply {
      moveTo(p1.x, p1.y); lineTo(p5.x, p5.y); lineTo(tableCenter.x, tableCenter.y); close()
    }
    drawPath(facetLeft, color = Color(0x20FFFFFF))

    val facetRight = Path().apply {
      moveTo(p2.x, p2.y); lineTo(p3.x, p3.y); lineTo(tableCenter.x, tableCenter.y); close()
    }
    drawPath(facetRight, color = Color(0x30000000))

    // Facet lines
    drawLine(Color.White.copy(alpha = 0.6f), p1, p4, strokeWidth = 1.5f)
    drawLine(Color.White.copy(alpha = 0.6f), p2, p4, strokeWidth = 1.5f)
    drawLine(Color.White.copy(alpha = 0.6f), p5, p4, strokeWidth = 1.5f)
    drawLine(Color.White.copy(alpha = 0.6f), p3, p4, strokeWidth = 1.5f)

    // Specular brilliant sparkle
    drawCircle(Color.White, radius = w * 0.05f, center = Offset(cx - w * 0.12f, h * 0.28f))
    drawCircle(Color.White, radius = w * 0.02f, center = Offset(cx + w * 0.12f, h * 0.50f))
  }
}

/**
 * Cut faceted Royal Emerald (Rectangular Cut)
 */
@Composable
fun RoyalEmeraldGem(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 46.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Soft green aura
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0x5010B981), Color(0x0010B981)),
        center = Offset(cx, h * 0.5f),
        radius = w * 0.55f
      ),
      radius = w * 0.55f,
      center = Offset(cx, h * 0.5f)
    )

    // Base body
    val body = Path().apply {
      moveTo(w * 0.22f, h * 0.15f)
      lineTo(w * 0.78f, h * 0.15f)
      lineTo(w * 0.88f, h * 0.32f)
      lineTo(w * 0.88f, h * 0.68f)
      lineTo(w * 0.78f, h * 0.85f)
      lineTo(w * 0.22f, h * 0.85f)
      lineTo(w * 0.12f, h * 0.68f)
      lineTo(w * 0.12f, h * 0.32f)
      close()
    }
    drawPath(
      body,
      brush = Brush.verticalGradient(
        listOf(Color(0xFF34D399), Color(0xFF059669), Color(0xFF064E3B))
      )
    )

    // Inner table
    val table = Path().apply {
      moveTo(w * 0.28f, h * 0.28f)
      lineTo(w * 0.72f, h * 0.28f)
      lineTo(w * 0.72f, h * 0.72f)
      lineTo(w * 0.28f, h * 0.72f)
      close()
    }
    drawPath(table, color = Color(0x30FFFFFF))
    drawPath(table, color = Color.White.copy(alpha = 0.5f), style = Stroke(width = 1.5f))

    // Corner facet lines
    drawLine(Color.White.copy(alpha = 0.5f), Offset(w * 0.22f, h * 0.15f), Offset(w * 0.28f, h * 0.28f), strokeWidth = 1.5f)
    drawLine(Color.White.copy(alpha = 0.5f), Offset(w * 0.78f, h * 0.15f), Offset(w * 0.72f, h * 0.28f), strokeWidth = 1.5f)
    drawLine(Color.White.copy(alpha = 0.5f), Offset(w * 0.78f, h * 0.85f), Offset(w * 0.72f, h * 0.72f), strokeWidth = 1.5f)
    drawLine(Color.White.copy(alpha = 0.5f), Offset(w * 0.22f, h * 0.85f), Offset(w * 0.28f, h * 0.72f), strokeWidth = 1.5f)

    // Specular highlight
    drawCircle(Color.White, radius = w * 0.05f, center = Offset(w * 0.35f, h * 0.35f))
  }
}

/**
 * Cut faceted Celestial Sapphire (Cobalt Cushion Cut)
 */
@Composable
fun RoyalSapphireGem(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 46.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Soft blue aura
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0x5038BDF8), Color(0x0038BDF8)),
        center = Offset(cx, h * 0.5f),
        radius = w * 0.55f
      ),
      radius = w * 0.55f,
      center = Offset(cx, h * 0.5f)
    )

    // Diamond Body
    val body = Path().apply {
      moveTo(cx, h * 0.10f)
      lineTo(w * 0.88f, h * 0.50f)
      lineTo(cx, h * 0.90f)
      lineTo(w * 0.12f, h * 0.50f)
      close()
    }
    drawPath(
      body,
      brush = Brush.verticalGradient(
        listOf(Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF0C4A6E))
      )
    )

    // Facet lines
    drawLine(Color.White.copy(alpha = 0.6f), Offset(cx, h * 0.10f), Offset(cx, h * 0.90f), strokeWidth = 1.5f)
    drawLine(Color.White.copy(alpha = 0.6f), Offset(w * 0.12f, h * 0.50f), Offset(w * 0.88f, h * 0.50f), strokeWidth = 1.5f)

    // Top table facet
    val topFacet = Path().apply {
      moveTo(cx, h * 0.25f)
      lineTo(w * 0.72f, h * 0.50f)
      lineTo(cx, h * 0.75f)
      lineTo(w * 0.28f, h * 0.50f)
      close()
    }
    drawPath(topFacet, color = Color(0x35FFFFFF))
    drawPath(topFacet, color = Color.White.copy(alpha = 0.5f), style = Stroke(width = 1.5f))

    // Gleam
    drawCircle(Color.White, radius = w * 0.05f, center = Offset(cx - w * 0.08f, h * 0.35f))
  }
}

/**
 * Cut faceted Golden Topaz / Radiant Diamond
 */
@Composable
fun RoyalTopazGem(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 46.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Soft warm golden aura
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0x60FBBF24), Color(0x00FBBF24)),
        center = Offset(cx, h * 0.5f),
        radius = w * 0.55f
      ),
      radius = w * 0.55f,
      center = Offset(cx, h * 0.5f)
    )

    // Star Facet
    val path = Path()
    var angle = -PI / 2.0
    val step = PI / 4
    for (i in 0 until 8) {
      val r = if (i % 2 == 0) w * 0.42f else w * 0.22f
      val x = (cx + r * cos(angle)).toFloat()
      val y = (h * 0.5f + r * sin(angle)).toFloat()
      if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
      angle += step
    }
    path.close()

    drawPath(
      path,
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFEF08A), Color(0xFFF59E0B), Color(0xFFB45309)),
        center = Offset(cx - w * 0.1f, h * 0.4f),
        radius = w * 0.5f
      )
    )

    // Center jewel
    drawCircle(
      color = Color.White.copy(alpha = 0.85f),
      radius = w * 0.12f,
      center = Offset(cx, h * 0.5f)
    )
  }
}

@Composable
fun LuxuryGemIcon(
  gemType: LuxuryGemType,
  modifier: Modifier = Modifier,
  sizeDp: Dp = 46.dp
) {
  when (gemType) {
    LuxuryGemType.RUBY -> RoyalRubyGem(modifier = modifier, sizeDp = sizeDp)
    LuxuryGemType.EMERALD -> RoyalEmeraldGem(modifier = modifier, sizeDp = sizeDp)
    LuxuryGemType.SAPPHIRE -> RoyalSapphireGem(modifier = modifier, sizeDp = sizeDp)
    LuxuryGemType.TOPAZ -> RoyalTopazGem(modifier = modifier, sizeDp = sizeDp)
  }
}

/**
 * The Magnificent Floating Golden Balance (الميزان الذهبي الفاره)
 * Renders with physics damping angle based on difference between left and right weights.
 */
@Composable
fun RoyalGoldenBalance(
  leftCount: Int,
  rightCount: Int,
  leftGem: LuxuryGemType,
  rightGem: LuxuryGemType,
  isBalanced: Boolean,
  onLeftPanClick: () -> Unit,
  onRightPanClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  // Compute tilt angle with physics (-16 deg to +16 deg)
  val weightDiff = (leftCount - rightCount).coerceIn(-4, 4)
  val targetAngle = if (isBalanced) 0f else weightDiff * 3.8f

  val animatedAngle by animateFloatAsState(
    targetValue = targetAngle,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "scale_tilt"
  )

  // Floating gentle hover for the entire balance
  val infiniteTransition = rememberInfiniteTransition(label = "scale_hover")
  val hoverY by infiniteTransition.animateFloat(
    initialValue = -5f,
    targetValue = 5f,
    animationSpec = infiniteRepeatable(
      animation = tween(2600, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale_hover_y"
  )

  // Balanced golden halo animation
  val haloAlpha by infiniteTransition.animateFloat(
    initialValue = 0.35f,
    targetValue = 0.85f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "halo_glow"
  )

  Box(
    modifier = modifier
      .offset(y = hoverY.dp)
      .size(width = 330.dp, height = 240.dp),
    contentAlignment = Alignment.Center
  ) {
    // 1. Radiant Golden Light Halo when Balanced
    if (isBalanced) {
      Canvas(modifier = Modifier.size(310.dp)) {
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(
              Color(0x90FDE047),
              Color(0x40FBBF24),
              Color(0x00FBBF24)
            ),
            radius = size.width * 0.5f
          ),
          alpha = haloAlpha
        )
      }
    }

    // 2. The Golden Stand & Pillar (Fixed)
    Canvas(modifier = Modifier.size(width = 330.dp, height = 240.dp)) {
      val w = size.width
      val h = size.height
      val cx = w * 0.5f

      // Golden Stand Pedestal Base
      drawOval(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xFFFEF08A), Color(0xFFD97706), Color(0xFF78350F)),
          center = Offset(cx, h * 0.88f),
          radius = w * 0.25f
        ),
        topLeft = Offset(cx - w * 0.22f, h * 0.82f),
        size = Size(w * 0.44f, h * 0.12f)
      )

      // Central Pillar
      val pillar = Path().apply {
        moveTo(cx - w * 0.035f, h * 0.22f)
        lineTo(cx + w * 0.035f, h * 0.22f)
        lineTo(cx + w * 0.055f, h * 0.84f)
        lineTo(cx - w * 0.055f, h * 0.84f)
        close()
      }
      drawPath(
        pillar,
        brush = Brush.horizontalGradient(
          colors = listOf(Color(0xFFB45309), Color(0xFFFDE68A), Color(0xFFD97706))
        )
      )

      // Top Crown Jewel Pivot
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xFFFEF08A), Color(0xFFD97706)),
          center = Offset(cx - 3f, h * 0.20f - 3f),
          radius = 16f
        ),
        radius = 14f,
        center = Offset(cx, h * 0.20f)
      )
      drawCircle(
        color = Color(0xFFE11D48),
        radius = 7f,
        center = Offset(cx, h * 0.20f)
      )
    }

    // 3. Tilting Beam & Hanging Pans (Rotates by animatedAngle)
    Box(
      modifier = Modifier
        .size(width = 330.dp, height = 240.dp)
        .rotate(animatedAngle),
      contentAlignment = Alignment.Center
    ) {
      Canvas(modifier = Modifier.size(width = 330.dp, height = 240.dp)) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val pivotY = h * 0.20f
        val beamHalfLength = w * 0.40f

        val leftEnd = Offset(cx - beamHalfLength, pivotY)
        val rightEnd = Offset(cx + beamHalfLength, pivotY)

        // Ornate Golden Balance Beam
        drawLine(
          brush = Brush.horizontalGradient(
            listOf(Color(0xFFD97706), Color(0xFFFEF08A), Color(0xFFD97706))
          ),
          start = leftEnd,
          end = rightEnd,
          strokeWidth = 7f,
          cap = StrokeCap.Round
        )

        // Golden chains to left pan
        val chainLeftBottom = Offset(leftEnd.x, leftEnd.y + h * 0.35f)
        drawLine(Color(0xFFD97706), leftEnd, Offset(chainLeftBottom.x - 28f, chainLeftBottom.y), strokeWidth = 2.5f)
        drawLine(Color(0xFFD97706), leftEnd, Offset(chainLeftBottom.x + 28f, chainLeftBottom.y), strokeWidth = 2.5f)

        // Golden chains to right pan
        val chainRightBottom = Offset(rightEnd.x, rightEnd.y + h * 0.35f)
        drawLine(Color(0xFFD97706), rightEnd, Offset(chainRightBottom.x - 28f, chainRightBottom.y), strokeWidth = 2.5f)
        drawLine(Color(0xFFD97706), rightEnd, Offset(chainRightBottom.x + 28f, chainRightBottom.y), strokeWidth = 2.5f)

        // Left Golden Pan (Liquid Gold Dish)
        drawOval(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFEF08A), Color(0xFFF59E0B), Color(0xFF92400E)),
            center = Offset(chainLeftBottom.x, chainLeftBottom.y),
            radius = 38f
          ),
          topLeft = Offset(chainLeftBottom.x - 38f, chainLeftBottom.y - 10f),
          size = Size(76f, 24f)
        )

        // Right Golden Pan (Liquid Gold Dish)
        drawOval(
          brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFEF08A), Color(0xFFF59E0B), Color(0xFF92400E)),
            center = Offset(chainRightBottom.x, chainRightBottom.y),
            radius = 38f
          ),
          topLeft = Offset(chainRightBottom.x - 38f, chainRightBottom.y - 10f),
          size = Size(76f, 24f)
        )
      }

      // Gems resting in Left Pan
      Box(
        modifier = Modifier
          .align(Alignment.CenterStart)
          .offset(x = 18.dp, y = 14.dp)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onLeftPanClick
          ),
        contentAlignment = Alignment.Center
      ) {
        LuxuryGemIcon(gemType = leftGem, sizeDp = 42.dp)
      }

      // Gems resting in Right Pan
      Box(
        modifier = Modifier
          .align(Alignment.CenterEnd)
          .offset(x = (-18).dp, y = 14.dp)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onRightPanClick
          ),
        contentAlignment = Alignment.Center
      ) {
        if (rightCount > 0) {
          LuxuryGemIcon(gemType = rightGem, sizeDp = 42.dp)
        }
      }
    }
  }
}

/**
 * Royal Crown Artifact (تاج ملكي فاخر مرصع بالياقوت والزمرد)
 */
@Composable
fun RoyalCrownArtifact(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 80.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Soft drop shadow
    drawOval(
      color = Color(0x35000000),
      topLeft = Offset(w * 0.15f, h * 0.85f),
      size = Size(w * 0.7f, h * 0.15f)
    )

    // Crown Peak Path
    val crown = Path().apply {
      moveTo(w * 0.15f, h * 0.75f)
      lineTo(w * 0.10f, h * 0.35f)
      lineTo(w * 0.30f, h * 0.55f)
      lineTo(cx, h * 0.20f)
      lineTo(w * 0.70f, h * 0.55f)
      lineTo(w * 0.90f, h * 0.35f)
      lineTo(w * 0.85f, h * 0.75f)
      close()
    }

    drawPath(
      crown,
      brush = Brush.verticalGradient(
        listOf(Color(0xFFFEF08A), Color(0xFFF59E0B), Color(0xFFB45309))
      )
    )

    // Crown Base Rim
    drawRoundRect(
      brush = Brush.horizontalGradient(
        listOf(Color(0xFFB45309), Color(0xFFFEF08A), Color(0xFFB45309))
      ),
      topLeft = Offset(w * 0.12f, h * 0.72f),
      size = Size(w * 0.76f, h * 0.14f),
      cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
    )

    // Rubies & Emeralds mounted on the Crown
    drawCircle(Color(0xFFE11D48), radius = w * 0.05f, center = Offset(cx, h * 0.22f))
    drawCircle(Color(0xFF10B981), radius = w * 0.04f, center = Offset(w * 0.10f, h * 0.36f))
    drawCircle(Color(0xFF10B981), radius = w * 0.04f, center = Offset(w * 0.90f, h * 0.36f))

    // Base Rim Jewels
    val rimJewels = listOf(Color(0xFF38BDF8), Color(0xFFE11D48), Color(0xFF10B981), Color(0xFFE11D48), Color(0xFF38BDF8))
    rimJewels.forEachIndexed { i, col ->
      val jx = w * 0.22f + i * (w * 0.14f)
      drawCircle(col, radius = w * 0.03f, center = Offset(jx, h * 0.79f))
    }
  }
}
