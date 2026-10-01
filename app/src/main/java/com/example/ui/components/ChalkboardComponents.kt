package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.FredokaFontFamily

/**
 * Chalk handwriting text effect with soft dust shadow and authentic chalkboard feel.
 */
@Composable
fun ChalkText(
  text: String,
  fontSize: TextUnit,
  color: Color = Color(0xFFF8FAFC),
  fontWeight: FontWeight = FontWeight.Bold,
  fontFamily: FontFamily = FredokaFontFamily,
  modifier: Modifier = Modifier
) {
  Text(
    text = text,
    fontSize = fontSize,
    fontWeight = fontWeight,
    fontFamily = fontFamily,
    color = color,
    style = TextStyle(
      shadow = Shadow(
        color = color.copy(alpha = 0.55f),
        offset = Offset(0f, 0f),
        blurRadius = 4f
      )
    ),
    modifier = modifier
  )
}

/**
 * Chalkboard Slate surface with subtle chalk dust texture, wooden frame, and chalk ledge.
 */
@Composable
fun ChalkboardSlate(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  Box(
    modifier = modifier
      .shadow(12.dp, RoundedCornerShape(12.dp), spotColor = Color(0x50000000))
      // Outer Natural Pine Wood Frame
      .background(
        Brush.linearGradient(
          colors = listOf(
            Color(0xFFE6CCA9), // Warm birch
            Color(0xFFD6B588), // Natural pine
            Color(0xFFC69C6D), // Pine shadow
            Color(0xFFDCC196)
          ),
          start = Offset(0f, 0f),
          end = Offset(400f, 400f)
        ),
        shape = RoundedCornerShape(12.dp)
      )
      .border(
        width = 2.dp,
        brush = Brush.verticalGradient(
          listOf(Color(0xFFF5E3C8), Color(0xFFAC8053))
        ),
        shape = RoundedCornerShape(12.dp)
      )
      .padding(14.dp) // Thickness of the wooden frame
  ) {
    // Inner Chalkboard Slate
    Box(
      modifier = Modifier
        .shadow(6.dp, RoundedCornerShape(4.dp), clip = false)
        .clip(RoundedCornerShape(4.dp))
        .background(
          Brush.radialGradient(
            colors = listOf(
              Color(0xFF2C3035), // Subtle center light
              Color(0xFF22252A), // Dark slate
              Color(0xFF1B1E22)  // Deep edge
            ),
            center = Offset(200f, 150f),
            radius = 600f
          )
        )
        .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(4.dp))
    ) {
      // Chalk dust smudges overlay
      Canvas(modifier = Modifier.matchParentSize()) {
        val w = size.width
        val h = size.height
        // Soft white chalk eraser smudges
        drawOval(
          color = Color(0x08FFFFFF),
          topLeft = Offset(w * 0.1f, h * 0.2f),
          size = Size(w * 0.7f, h * 0.5f)
        )
        drawOval(
          color = Color(0x06FFFFFF),
          topLeft = Offset(w * 0.3f, h * 0.4f),
          size = Size(w * 0.6f, h * 0.4f)
        )
      }

      content()
    }
  }
}

/**
 * Dashed Chalk Target Slot on the board where the answer must be dropped.
 */
@Composable
fun ChalkTargetDropSlot(
  isHovered: Boolean,
  currentValue: Int?,
  isWrong: Boolean = false,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "slot_pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 0.9f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  val borderColor = when {
    isWrong -> Color(0xFFF43F5E) // Red chalk border on error
    isHovered -> Color(0xFFFEF08A)
    else -> Color(0xFFE2E8F0).copy(alpha = pulseAlpha)
  }

  val chalkColor = when {
    isWrong -> Color(0xFFFB7185) // Reddish chalk on error
    else -> Color(0xFFFEF08A)    // Glowing yellow chalk
  }

  val bgColor = when {
    isWrong -> Color(0x30F43F5E)
    isHovered -> Color(0x25FDE047)
    else -> Color(0x10FFFFFF)
  }

  Box(
    modifier = modifier
      .size(width = 68.dp, height = 62.dp)
      .background(bgColor, shape = RoundedCornerShape(10.dp))
      .border(
        width = if (isHovered || isWrong) 2.5.dp else 1.8.dp,
        color = borderColor,
        shape = RoundedCornerShape(10.dp)
      ),
    contentAlignment = Alignment.Center
  ) {
    if (currentValue != null) {
      ChalkText(
        text = "$currentValue",
        fontSize = 36.sp,
        color = chalkColor
      )
    } else {
      ChalkText(
        text = "؟",
        fontSize = 32.sp,
        color = Color(0xFFCBD5E1).copy(alpha = 0.7f)
      )
    }
  }
}

/**
 * Potted cactus succulent on the desk, replicating the user's reference photo.
 */
@Composable
fun PottedCactusDecor(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 64.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Soft Shadow
    drawOval(
      color = Color(0x30000000),
      topLeft = Offset(w * 0.2f, h * 0.88f),
      size = Size(w * 0.6f, h * 0.12f)
    )

    // White Ceramic Pot
    val potPath = Path().apply {
      moveTo(w * 0.26f, h * 0.55f)
      lineTo(w * 0.74f, h * 0.55f)
      lineTo(w * 0.66f, h * 0.92f)
      lineTo(w * 0.34f, h * 0.92f)
      close()
    }
    drawPath(
      path = potPath,
      brush = Brush.horizontalGradient(
        colors = listOf(Color(0xFFE2E8F0), Color(0xFFFFFFFF), Color(0xFFCBD5E1))
      )
    )

    // Green Cactus Succulent
    val cactusPath = Path().apply {
      moveTo(cx, h * 0.14f)
      cubicTo(w * 0.72f, h * 0.16f, w * 0.68f, h * 0.56f, cx, h * 0.56f)
      cubicTo(w * 0.32f, h * 0.56f, w * 0.28f, h * 0.16f, cx, h * 0.14f)
      close()
    }
    drawPath(
      path = cactusPath,
      brush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF15803D), Color(0xFF22C55E), Color(0xFF166534))
      )
    )

    // Cactus Ribs / Rib lines
    drawLine(
      color = Color(0xFF166534),
      start = Offset(cx, h * 0.14f),
      end = Offset(cx, h * 0.56f),
      strokeWidth = 2f
    )
    drawLine(
      color = Color(0xFF166534),
      start = Offset(cx - w * 0.08f, h * 0.18f),
      end = Offset(cx - w * 0.08f, h * 0.54f),
      strokeWidth = 1.5f
    )
    drawLine(
      color = Color(0xFF166534),
      start = Offset(cx + w * 0.08f, h * 0.18f),
      end = Offset(cx + w * 0.08f, h * 0.54f),
      strokeWidth = 1.5f
    )
  }
}

/**
 * Chalk Sticks and Eraser sitting on the desk ledge.
 */
@Composable
fun ChalkLedgeDecor(
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Wooden duster eraser
    Box(
      modifier = Modifier
        .size(width = 46.dp, height = 18.dp)
        .shadow(2.dp, RoundedCornerShape(3.dp))
        .background(Color(0xFFB45309), RoundedCornerShape(3.dp))
        .border(1.dp, Color(0xFF78350F), RoundedCornerShape(3.dp))
    ) {
      Box(
        modifier = Modifier
          .matchParentSize()
          .padding(bottom = 4.dp)
          .background(Color(0xFFD97706), RoundedCornerShape(3.dp))
      )
    }

    Box(modifier = Modifier.size(8.dp))

    // White, Yellow, Blue Chalk sticks
    val chalkColors = listOf(Color(0xFFFFFFFF), Color(0xFFFEF08A), Color(0xFF7DD3FC), Color(0xFFF472B6))
    chalkColors.forEach { col ->
      Box(
        modifier = Modifier
          .padding(horizontal = 2.dp)
          .size(width = 18.dp, height = 7.dp)
          .shadow(1.dp, RoundedCornerShape(2.dp))
          .background(col, RoundedCornerShape(2.dp))
      )
    }
  }
}
