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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class MonsterExpression {
  HAPPY,
  HUNGRY,
  CHEWING,
  CELEBRATING,
  OOPS
}

enum class ClayFoodType(val titleAr: String, val singularAr: String) {
  STRAWBERRY("فراولة طازجة", "حبة فراولة"),
  CUPCAKE("كب كيك لذيذ", "قطعة كب كيك"),
  DONUT("دونات ملون", "دونات"),
  STAR_CANDY("حلوى النجوم", "حلوى نجمية")
}

/**
 * Modern Claymorphic Monster "بوبو" (Bobo) with expressive eyes, reactive mouth and bouncy animations.
 */
@Composable
fun ClayMonsterCharacter(
  modifier: Modifier = Modifier,
  expression: MonsterExpression = MonsterExpression.HAPPY,
  sizeDp: Dp = 140.dp
) {
  val infiniteTransition = rememberInfiniteTransition(label = "monster_idle")
  val breatheScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.04f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "monster_breathe"
  )

  val chewMouth = remember { Animatable(0f) }
  LaunchedEffect(expression) {
    if (expression == MonsterExpression.CHEWING) {
      for (i in 1..4) {
        chewMouth.animateTo(1f, tween(110))
        chewMouth.animateTo(0.2f, tween(110))
      }
      chewMouth.animateTo(0f, tween(100))
    } else {
      chewMouth.snapTo(0f)
    }
  }

  val celebrationJump = remember { Animatable(0f) }
  LaunchedEffect(expression) {
    if (expression == MonsterExpression.CELEBRATING) {
      celebrationJump.animateTo(-24f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
      celebrationJump.animateTo(0f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }
  }

  Canvas(
    modifier = modifier
      .size(sizeDp)
      .offset(y = celebrationJump.value.dp)
      .scale(breatheScale)
  ) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Soft clay shadow
    drawOval(
      color = Color(0x301E293B),
      topLeft = Offset(w * 0.15f, h * 0.88f),
      size = Size(w * 0.7f, h * 0.12f)
    )

    // Left and Right Clay Horns / Antennae
    drawCircle(
      color = Color(0xFFFBBF24),
      radius = w * 0.08f,
      center = Offset(cx - w * 0.28f, h * 0.18f)
    )
    drawLine(
      color = Color(0xFF0284C7),
      start = Offset(cx - w * 0.22f, h * 0.32f),
      end = Offset(cx - w * 0.28f, h * 0.18f),
      strokeWidth = w * 0.06f,
      cap = StrokeCap.Round
    )

    drawCircle(
      color = Color(0xFFFBBF24),
      radius = w * 0.08f,
      center = Offset(cx + w * 0.28f, h * 0.18f)
    )
    drawLine(
      color = Color(0xFF0284C7),
      start = Offset(cx + w * 0.22f, h * 0.32f),
      end = Offset(cx + w * 0.28f, h * 0.18f),
      strokeWidth = w * 0.06f,
      cap = StrokeCap.Round
    )

    // Plump Monster Body (Claymorphic Radial Shading)
    val bodyRadius = w * 0.42f
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(
          Color(0xFF38BDF8), // Bright cyan top highlight
          Color(0xFF0284C7), // Rich oceanic cyan
          Color(0xFF0369A1)  // Deep bevel edge
        ),
        center = Offset(cx - bodyRadius * 0.25f, h * 0.55f - bodyRadius * 0.3f),
        radius = bodyRadius * 1.35f
      ),
      radius = bodyRadius,
      center = Offset(cx, h * 0.58f)
    )

    // Top Glossy Clay Sheen
    drawOval(
      brush = Brush.verticalGradient(
        colors = listOf(Color(0x60FFFFFF), Color(0x00FFFFFF))
      ),
      topLeft = Offset(cx - bodyRadius * 0.6f, h * 0.26f),
      size = Size(bodyRadius * 1.2f, bodyRadius * 0.5f)
    )

    // Rosy Clay Cheeks
    drawCircle(
      color = Color(0x40F43F5E),
      radius = w * 0.08f,
      center = Offset(cx - w * 0.26f, h * 0.64f)
    )
    drawCircle(
      color = Color(0x40F43F5E),
      radius = w * 0.08f,
      center = Offset(cx + w * 0.26f, h * 0.64f)
    )

    // Big Expressive Clay Eyes
    val eyeY = h * 0.48f
    val eyeSpacing = w * 0.16f
    for (dir in listOf(-1, 1)) {
      val eyeX = cx + dir * eyeSpacing
      // White eye sphere
      drawCircle(
        color = Color.White,
        radius = w * 0.12f,
        center = Offset(eyeX, eyeY)
      )
      // Dark Iris with expression
      val pupilOffset = when (expression) {
        MonsterExpression.HUNGRY -> Offset(0f, w * 0.03f)
        MonsterExpression.CELEBRATING -> Offset(0f, -w * 0.02f)
        else -> Offset.Zero
      }
      drawCircle(
        color = Color(0xFF0F172A),
        radius = w * 0.07f,
        center = Offset(eyeX + pupilOffset.x, eyeY + pupilOffset.y)
      )
      // Big Twinkle Specular
      drawCircle(
        color = Color.White,
        radius = w * 0.035f,
        center = Offset(eyeX - w * 0.03f, eyeY - w * 0.03f)
      )
      // Small Twinkle
      drawCircle(
        color = Color.White,
        radius = w * 0.015f,
        center = Offset(eyeX + w * 0.02f, eyeY + w * 0.02f)
      )
    }

    // Interactive Animated Mouth
    when (expression) {
      MonsterExpression.HUNGRY -> {
        // Wide open mouth ready to eat
        drawOval(
          color = Color(0xFF881337),
          topLeft = Offset(cx - w * 0.18f, h * 0.62f),
          size = Size(w * 0.36f, h * 0.22f)
        )
        // Cute pink tongue inside
        drawOval(
          color = Color(0xFFFB7185),
          topLeft = Offset(cx - w * 0.12f, h * 0.73f),
          size = Size(w * 0.24f, h * 0.10f)
        )
      }
      MonsterExpression.CHEWING -> {
        val openH = (h * 0.16f) * chewMouth.value + h * 0.04f
        drawOval(
          color = Color(0xFF881337),
          topLeft = Offset(cx - w * 0.14f, h * 0.66f - openH * 0.5f),
          size = Size(w * 0.28f, openH)
        )
      }
      MonsterExpression.CELEBRATING -> {
        // Big ecstatic grin
        val mouthPath = Path().apply {
          moveTo(cx - w * 0.20f, h * 0.65f)
          quadraticBezierTo(cx, h * 0.85f, cx + w * 0.20f, h * 0.65f)
          close()
        }
        drawPath(mouthPath, color = Color(0xFF881337))
        drawOval(
          color = Color(0xFFFB7185),
          topLeft = Offset(cx - w * 0.10f, h * 0.74f),
          size = Size(w * 0.20f, h * 0.08f)
        )
      }
      MonsterExpression.OOPS -> {
        // Gentle wavy line
        val oopsPath = Path().apply {
          moveTo(cx - w * 0.12f, h * 0.72f)
          cubicTo(cx - w * 0.06f, h * 0.69f, cx + w * 0.06f, h * 0.75f, cx + w * 0.12f, h * 0.72f)
        }
        drawPath(oopsPath, color = Color(0xFF0F172A), style = Stroke(width = w * 0.04f, cap = StrokeCap.Round))
      }
      else -> {
        // Friendly smile
        val smilePath = Path().apply {
          moveTo(cx - w * 0.16f, h * 0.68f)
          quadraticBezierTo(cx, h * 0.80f, cx + w * 0.16f, h * 0.68f)
        }
        drawPath(smilePath, color = Color(0xFF0F172A), style = Stroke(width = w * 0.05f, cap = StrokeCap.Round))
      }
    }
  }
}

/**
 * 3D Clay Strawberry with realistic seeds and soft specular highlights.
 */
@Composable
fun ClayStrawberry(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 48.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Soft drop shadow
    drawOval(
      color = Color(0x35000000),
      topLeft = Offset(w * 0.2f, h * 0.86f),
      size = Size(w * 0.6f, h * 0.14f)
    )

    // Green Calyx / Leaves
    for (i in -2..2) {
      val leafPath = Path().apply {
        moveTo(cx, h * 0.24f)
        quadraticBezierTo(cx + i * w * 0.18f, h * 0.10f, cx + i * w * 0.22f, h * 0.20f)
        close()
      }
      drawPath(leafPath, color = Color(0xFF22C55E))
    }

    // Strawberry Body (Plump clay heart)
    val bodyPath = Path().apply {
      moveTo(cx, h * 0.22f)
      cubicTo(w * 0.92f, h * 0.24f, w * 0.85f, h * 0.75f, cx, h * 0.92f)
      cubicTo(w * 0.15f, h * 0.75f, w * 0.08f, h * 0.24f, cx, h * 0.22f)
      close()
    }

    drawPath(
      path = bodyPath,
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFB7185), Color(0xFFF43F5E), Color(0xFFBE123C)),
        center = Offset(cx - w * 0.15f, h * 0.45f),
        radius = w * 0.6f
      )
    )

    // Glossy clay gleam
    drawOval(
      color = Color(0x70FFFFFF),
      topLeft = Offset(cx - w * 0.32f, h * 0.32f),
      size = Size(w * 0.26f, h * 0.35f)
    )

    // Golden Seeds
    val seedOffsets = listOf(
      Offset(cx - w * 0.15f, h * 0.45f),
      Offset(cx + w * 0.15f, h * 0.45f),
      Offset(cx, h * 0.58f),
      Offset(cx - w * 0.14f, h * 0.68f),
      Offset(cx + w * 0.14f, h * 0.68f),
      Offset(cx, h * 0.78f)
    )
    seedOffsets.forEach { pos ->
      drawOval(
        color = Color(0xFFFEF08A),
        topLeft = Offset(pos.x - w * 0.025f, pos.y - h * 0.04f),
        size = Size(w * 0.05f, h * 0.07f)
      )
    }
  }
}

/**
 * 3D Clay Cupcake with delicious swirl frosting and cherry.
 */
@Composable
fun ClayCupcake(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 48.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Soft shadow
    drawOval(
      color = Color(0x35000000),
      topLeft = Offset(w * 0.2f, h * 0.88f),
      size = Size(w * 0.6f, h * 0.12f)
    )

    // Cupcake Base (Pastel clay cup)
    val cupPath = Path().apply {
      moveTo(w * 0.22f, h * 0.55f)
      lineTo(w * 0.78f, h * 0.55f)
      lineTo(w * 0.68f, h * 0.90f)
      lineTo(w * 0.32f, h * 0.90f)
      close()
    }
    drawPath(
      path = cupPath,
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFDE68A), Color(0xFFD97706))
      )
    )

    // Fluffy Frosting Swirls (Pink Marshmallow)
    drawRoundRect(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFBCFE8), Color(0xFFF472B6), Color(0xFFDB2777)),
        center = Offset(cx - w * 0.1f, h * 0.45f),
        radius = w * 0.5f
      ),
      topLeft = Offset(w * 0.15f, h * 0.38f),
      size = Size(w * 0.70f, h * 0.25f),
      cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
    )

    // Top Swirl
    drawCircle(
      color = Color(0xFFF472B6),
      radius = w * 0.22f,
      center = Offset(cx, h * 0.32f)
    )

    // Red Cherry on Top
    drawCircle(
      color = Color(0xFFE11D48),
      radius = w * 0.10f,
      center = Offset(cx, h * 0.16f)
    )
    drawCircle(
      color = Color.White,
      radius = w * 0.03f,
      center = Offset(cx - w * 0.03f, h * 0.14f)
    )
  }
}

/**
 * 3D Clay Glazed Donut with colorful candy sprinkles.
 */
@Composable
fun ClayDonut(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 48.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f
    val cy = h * 0.52f
    val outerR = w * 0.42f
    val innerR = w * 0.16f

    // Shadow
    drawOval(
      color = Color(0x35000000),
      topLeft = Offset(w * 0.12f, h * 0.86f),
      size = Size(w * 0.76f, h * 0.14f)
    )

    // Dough
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFDE68A), Color(0xFFF59E0B), Color(0xFFB45309)),
        center = Offset(cx - outerR * 0.3f, cy - outerR * 0.3f),
        radius = outerR * 1.3f
      ),
      radius = outerR,
      center = Offset(cx, cy)
    )

    // Glaze
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFF67E8F9), Color(0xFF06B6D4), Color(0xFF0891B2)),
        center = Offset(cx - outerR * 0.2f, cy - outerR * 0.2f),
        radius = outerR * 1.1f
      ),
      radius = outerR * 0.88f,
      center = Offset(cx, cy)
    )

    // Donut Center Hole
    drawCircle(color = Color(0xFFF8FAFC), radius = innerR, center = Offset(cx, cy))

    // Sprinkles
    val sprinkleColors = listOf(Color(0xFFF43F5E), Color(0xFFFBBF24), Color(0xFF22C55E), Color(0xFFFFFFFF))
    val angles = listOf(30.0, 80.0, 140.0, 200.0, 260.0, 320.0)
    angles.forEachIndexed { i, a ->
      val rad = a * PI / 180.0
      val dist = (outerR + innerR) * 0.5f
      val sx = (cx + dist * cos(rad)).toFloat()
      val sy = (cy + dist * sin(rad)).toFloat()
      drawLine(
        color = sprinkleColors[i % sprinkleColors.size],
        start = Offset(sx - 3f, sy - 3f),
        end = Offset(sx + 3f, sy + 3f),
        strokeWidth = w * 0.05f,
        cap = StrokeCap.Round
      )
    }
  }
}

/**
 * 3D Clay Star Candy.
 */
@Composable
fun ClayStarCandy(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 48.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f
    val cy = h * 0.5f
    val outerR = w * 0.44f
    val innerR = outerR * 0.46f

    // Shadow
    drawOval(
      color = Color(0x35000000),
      topLeft = Offset(w * 0.15f, h * 0.88f),
      size = Size(w * 0.7f, h * 0.12f)
    )

    val path = Path()
    var angle = -PI / 2.0
    val step = PI / 5
    for (i in 0 until 10) {
      val r = if (i % 2 == 0) outerR else innerR
      val x = (cx + r * cos(angle)).toFloat()
      val y = (cy + r * sin(angle)).toFloat()
      if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
      angle += step
    }
    path.close()

    drawPath(
      path = path,
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFFEF08A), Color(0xFFFBBF24), Color(0xFFD97706)),
        center = Offset(cx - outerR * 0.3f, cy - outerR * 0.3f),
        radius = outerR * 1.4f
      )
    )

    // Soft clay highlight dot
    drawCircle(
      color = Color.White.copy(alpha = 0.8f),
      radius = outerR * 0.16f,
      center = Offset(cx - outerR * 0.2f, cy - outerR * 0.2f)
    )
  }
}

@Composable
fun ClayFoodItemIcon(
  foodType: ClayFoodType,
  modifier: Modifier = Modifier,
  sizeDp: Dp = 48.dp
) {
  when (foodType) {
    ClayFoodType.STRAWBERRY -> ClayStrawberry(modifier = modifier, sizeDp = sizeDp)
    ClayFoodType.CUPCAKE -> ClayCupcake(modifier = modifier, sizeDp = sizeDp)
    ClayFoodType.DONUT -> ClayDonut(modifier = modifier, sizeDp = sizeDp)
    ClayFoodType.STAR_CANDY -> ClayStarCandy(modifier = modifier, sizeDp = sizeDp)
  }
}

/**
 * Clay Plate / Bowl where the monster's food is placed.
 */
@Composable
fun ClayMonsterBowl(
  modifier: Modifier = Modifier,
  sizeDp: Dp = 180.dp
) {
  Canvas(modifier = modifier.size(sizeDp)) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f

    // Soft Bowl Drop Shadow
    drawOval(
      color = Color(0x28000000),
      topLeft = Offset(w * 0.05f, h * 0.55f),
      size = Size(w * 0.90f, h * 0.42f)
    )

    // Outer Rim of Clay Bowl (Marshmallow Cream)
    drawOval(
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFFE2E8F0))
      ),
      topLeft = Offset(w * 0.04f, h * 0.38f),
      size = Size(w * 0.92f, h * 0.54f)
    )

    // Inner Dish Cavity (Soft pastel turquoise)
    drawOval(
      brush = Brush.radialGradient(
        colors = listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD), Color(0xFF7DD3FC)),
        center = Offset(cx, h * 0.65f),
        radius = w * 0.45f
      ),
      topLeft = Offset(w * 0.10f, h * 0.44f),
      size = Size(w * 0.80f, h * 0.42f)
    )
  }
}
