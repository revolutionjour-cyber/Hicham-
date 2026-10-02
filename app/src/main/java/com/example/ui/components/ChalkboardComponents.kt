package com.example.ui.components

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
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
 * Upgraded with animated dashed chalk border, breathing halo glow, bouncing 3D question mark,
 * twinkling magic sparkles, and magnetic suction reaction when hovered!
 */
@Composable
fun ChalkTargetDropSlot(
  isHovered: Boolean,
  currentValue: Int?,
  isWrong: Boolean = false,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "slot_effects")

  // 1. Breathing Glow Alpha & Scale
  val breathingAlpha by infiniteTransition.animateFloat(
    initialValue = 0.35f,
    targetValue = 0.85f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "breathing_alpha"
  )

  // 2. Animated Dashed Chalk Phase
  val dashPhase by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 40f,
    animationSpec = infiniteRepeatable(
      animation = tween(2400, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "dash_phase"
  )

  // 3. Bouncing Question Mark Vertical Bobbing
  val questionBobY by infiniteTransition.animateFloat(
    initialValue = -3.5f,
    targetValue = 3.5f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "question_bob"
  )

  // 4. Subtle Question Mark Rotation Wobble
  val questionRot by infiniteTransition.animateFloat(
    initialValue = -5f,
    targetValue = 5f,
    animationSpec = infiniteRepeatable(
      animation = tween(1100, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "question_rot"
  )

  // 5. Sparkle Star Pulse
  val sparkleScale by infiniteTransition.animateFloat(
    initialValue = 0.6f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "sparkle_scale"
  )

  // Magnetic Suction Scale when hovered with draggable tile
  val suctionScale by animateFloatAsState(
    targetValue = when {
      isHovered -> 1.14f
      isWrong -> 1.05f
      currentValue != null -> 1.06f
      else -> 1.0f
    },
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessMedium
    ),
    label = "suction_scale"
  )

  val haloColor = when {
    isWrong -> Color(0x60F43F5E)
    isHovered -> Color(0x7034D399) // Mint glow
    currentValue != null -> Color(0x60FEF08A)
    else -> Color(0x40FDE047).copy(alpha = breathingAlpha)
  }

  val chalkColor = when {
    isWrong -> Color(0xFFFB7185)
    isHovered -> Color(0xFF6EE7B7)
    currentValue != null -> Color(0xFFFEF08A)
    else -> Color(0xFFFEF08A)
  }

  val borderColor = when {
    isWrong -> Color(0xFFF43F5E)
    isHovered -> Color(0xFF34D399)
    currentValue != null -> Color(0xFFFDE047)
    else -> Color(0xFFE2E8F0)
  }

  Box(
    modifier = modifier
      .size(width = 72.dp, height = 66.dp)
      .scale(suctionScale),
    contentAlignment = Alignment.Center
  ) {
    // 1. Ambient Breathing Halo Glow Layer
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      drawRoundRect(
        brush = Brush.radialGradient(
          colors = listOf(haloColor, Color.Transparent),
          center = Offset(w * 0.5f, h * 0.5f),
          radius = w * 0.75f
        ),
        size = size,
        cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx())
      )
    }

    // 2. Translucent Slate Inset Background
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(3.dp)
        .background(
          color = when {
            isWrong -> Color(0x35F43F5E)
            isHovered -> Color(0x3034D399)
            currentValue != null -> Color(0x22FEF08A)
            else -> Color(0x18FFFFFF)
          },
          shape = RoundedCornerShape(12.dp)
        )
    )

    // 3. Hand-drawn Dashed Chalk Border Canvas
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .padding(3.dp)
    ) {
      val strokeWidth = if (isHovered || isWrong) 3.5.dp.toPx() else 2.2.dp.toPx()
      val dashInterval = if (currentValue != null) null else PathEffect.dashPathEffect(
        floatArrayOf(14.dp.toPx(), 8.dp.toPx()),
        dashPhase
      )

      drawRoundRect(
        color = borderColor,
        size = Size(size.width, size.height),
        cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
        style = Stroke(
          width = strokeWidth,
          pathEffect = dashInterval,
          cap = StrokeCap.Round
        )
      )
    }

    // 4. Magic Corner Sparkles (when waiting for answer)
    if (currentValue == null && !isHovered) {
      // Top-Right Twinkling Star
      Canvas(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .offset(x = 2.dp, y = (-2).dp)
          .size(10.dp)
          .scale(sparkleScale)
      ) {
        drawCircle(color = Color(0xFFFEF08A), radius = size.minDimension * 0.45f)
      }

      // Bottom-Left Twinkling Star
      Canvas(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .offset(x = (-2).dp, y = 2.dp)
          .size(8.dp)
          .scale(sparkleScale * 0.85f)
      ) {
        drawCircle(color = Color(0xFF6EE7B7), radius = size.minDimension * 0.45f)
      }
    }

    // 5. Center Content: Either the Answer or the Bouncing 3D Question Mark
    if (currentValue != null) {
      ChalkText(
        text = "$currentValue",
        fontSize = 38.sp,
        color = chalkColor
      )
    } else {
      // Bouncing Curious 3D Question Mark
      Text(
        text = "؟",
        fontFamily = FredokaFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 34.sp,
        color = if (isHovered) Color(0xFF34D399) else Color(0xFFFEF08A),
        style = TextStyle(
          shadow = Shadow(
            color = Color(0x80000000),
            offset = Offset(2f, 2f),
            blurRadius = 4f
          )
        ),
        modifier = Modifier
          .offset(y = questionBobY.dp)
          .rotate(questionRot)
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
