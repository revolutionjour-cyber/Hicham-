package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
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
  val infiniteTransition = rememberInfiniteTransition(label = "mascot_motion")

  // Gentle zero-gravity breathing float
  val floatY by infiniteTransition.animateFloat(
    initialValue = -8f,
    targetValue = 8f,
    animationSpec = infiniteRepeatable(
      animation = tween(2200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "mascot_float"
  )

  // Subtle hand wave
  val waveAngle by infiniteTransition.animateFloat(
    initialValue = -12f,
    targetValue = 18f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "mascot_wave"
  )

  // Thruster flame pulse
  val thrusterPulse by infiniteTransition.animateFloat(
    initialValue = 0.7f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(180, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "thruster"
  )

  // Celebration spring jump
  val jumpAnim = remember { Animatable(0f) }
  LaunchedEffect(mood) {
    if (mood == MascotMood.CELEBRATING) {
      jumpAnim.animateTo(-32f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
      jumpAnim.animateTo(0f, spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium))
      jumpAnim.animateTo(-16f, tween(160, easing = FastOutSlowInEasing))
      jumpAnim.animateTo(0f, tween(180, easing = FastOutSlowInEasing))
    }
  }

  // Wiggle on oops
  val wobbleAnim = remember { Animatable(0f) }
  LaunchedEffect(mood) {
    if (mood == MascotMood.OOPS) {
      wobbleAnim.animateTo(-14f, tween(70))
      wobbleAnim.animateTo(14f, tween(70))
      wobbleAnim.animateTo(-10f, tween(70))
      wobbleAnim.animateTo(10f, tween(70))
      wobbleAnim.animateTo(0f, tween(70))
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

      // 1. Plasma Thruster Ambient Glow
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0x9900E5FF), Color(0x4038BDF8), Color.Transparent),
          center = Offset(currentX, currentY + h * 0.32f),
          radius = w * 0.38f * thrusterPulse
        ),
        radius = w * 0.38f * thrusterPulse,
        center = Offset(currentX, currentY + h * 0.32f)
      )

      // Mini thruster exhaust flames (dual)
      val flameL = h * 0.12f * thrusterPulse
      drawOval(
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xFF38BDF8), Color(0xFFFBBF24), Color.Transparent)
        ),
        topLeft = Offset(currentX - w * 0.14f, currentY + h * 0.28f),
        size = Size(w * 0.08f, flameL)
      )
      drawOval(
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xFF38BDF8), Color(0xFFFBBF24), Color.Transparent)
        ),
        topLeft = Offset(currentX + w * 0.06f, currentY + h * 0.28f),
        size = Size(w * 0.08f, flameL)
      )

      // 2. High-Tech Oxygen Backpack
      drawRoundRect(
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xFF0284C7), Color(0xFF0369A1))
        ),
        topLeft = Offset(currentX - w * 0.32f, currentY - h * 0.04f),
        size = Size(w * 0.64f, h * 0.36f),
        cornerRadius = CornerRadius(w * 0.1f, w * 0.1f)
      )
      // Backpack vents
      drawRoundRect(
        color = Color(0xFF0C4A6E),
        topLeft = Offset(currentX - w * 0.24f, currentY + h * 0.04f),
        size = Size(w * 0.48f, h * 0.06f),
        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
      )

      // 3. Astronaut Suit Body
      drawRoundRect(
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xFFFFFFFF), Color(0xFFF1F5F9), Color(0xFFCBD5E1)),
          startY = currentY - h * 0.06f,
          endY = currentY + h * 0.32f
        ),
        topLeft = Offset(currentX - w * 0.24f, currentY + h * 0.02f),
        size = Size(w * 0.48f, h * 0.32f),
        cornerRadius = CornerRadius(w * 0.14f, w * 0.14f)
      )

      // Suit Center Control Panel (Cyan + Gold lights)
      drawRoundRect(
        color = Color(0xFF0F172A),
        topLeft = Offset(currentX - w * 0.12f, currentY + h * 0.1f),
        size = Size(w * 0.24f, h * 0.14f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
      )
      // LED status lights
      drawCircle(
        color = Color(0xFF22C55E),
        radius = w * 0.025f,
        center = Offset(currentX - w * 0.05f, currentY + h * 0.15f)
      )
      drawCircle(
        color = Color(0xFF38BDF8),
        radius = w * 0.025f,
        center = Offset(currentX + w * 0.05f, currentY + h * 0.15f)
      )
      drawRoundRect(
        color = Color(0xFFFBBF24),
        topLeft = Offset(currentX - w * 0.07f, currentY + h * 0.19f),
        size = Size(w * 0.14f, h * 0.025f),
        cornerRadius = CornerRadius(2f, 2f)
      )

      // 4. Arms & High-Grip Gloves
      val armY = currentY + h * 0.06f
      val armJump = if (mood == MascotMood.CELEBRATING) -h * 0.14f else 0f

      // Left Arm
      drawRoundRect(
        color = Color(0xFFE2E8F0),
        topLeft = Offset(currentX - w * 0.38f, armY + armJump),
        size = Size(w * 0.15f, h * 0.22f),
        cornerRadius = CornerRadius(w * 0.075f, w * 0.075f)
      )
      // Left Glove (Coral Pink)
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xFFFB7185), Color(0xFFE11D48)),
          center = Offset(currentX - w * 0.3f, armY + h * 0.22f + armJump),
          radius = w * 0.08f
        ),
        radius = w * 0.075f,
        center = Offset(currentX - w * 0.3f, armY + h * 0.22f + armJump)
      )

      // Right Arm (Animated Wave)
      val rightArmOffset = if (mood == MascotMood.CELEBRATING) -h * 0.16f else (waveAngle * 0.5f)
      drawRoundRect(
        color = Color(0xFFE2E8F0),
        topLeft = Offset(currentX + w * 0.23f, armY + rightArmOffset),
        size = Size(w * 0.15f, h * 0.22f),
        cornerRadius = CornerRadius(w * 0.075f, w * 0.075f)
      )
      // Right Glove
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xFFFB7185), Color(0xFFE11D48)),
          center = Offset(currentX + w * 0.31f, armY + h * 0.22f + rightArmOffset),
          radius = w * 0.08f
        ),
        radius = w * 0.075f,
        center = Offset(currentX + w * 0.31f, armY + h * 0.22f + rightArmOffset)
      )

      // 5. Big Spherical Helmet (Glossy 3D finish)
      val helmetCenter = Offset(currentX, currentY - h * 0.16f)
      val helmetRadius = w * 0.33f

      // Outer Helmet Shell
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color.White, Color(0xFFE2E8F0), Color(0xFF94A3B8)),
          center = Offset(helmetCenter.x - helmetRadius * 0.3f, helmetCenter.y - helmetRadius * 0.3f),
          radius = helmetRadius * 1.3f
        ),
        radius = helmetRadius,
        center = helmetCenter
      )

      // Top Communications Antenna
      drawLine(
        color = Color(0xFF64748B),
        start = Offset(currentX, helmetCenter.y - helmetRadius),
        end = Offset(currentX, helmetCenter.y - helmetRadius - h * 0.1f),
        strokeWidth = 7f
      )
      drawCircle(
        brush = Brush.radialGradient(
          colors = if (mood == MascotMood.CELEBRATING) {
            listOf(Color(0xFF86EFAC), Color(0xFF16A34A))
          } else {
            listOf(Color(0xFFFCA5A5), Color(0xFFDC2626))
          },
          center = Offset(currentX, helmetCenter.y - helmetRadius - h * 0.1f),
          radius = w * 0.05f
        ),
        radius = w * 0.045f,
        center = Offset(currentX, helmetCenter.y - helmetRadius - h * 0.1f)
      )

      // 6. Luxury Curved Visor (Deep Nebula Glass with Metallic Rim)
      val visorW = helmetRadius * 1.52f
      val visorH = helmetRadius * 1.32f
      val visorLeft = currentX - visorW * 0.5f
      val visorTop = helmetCenter.y - visorH * 0.52f

      // Metallic Rim
      drawRoundRect(
        brush = Brush.linearGradient(
          colors = listOf(Color(0xFF38BDF8), Color(0xFF818CF8), Color(0xFFC084FC))
        ),
        topLeft = Offset(visorLeft - 3f, visorTop - 3f),
        size = Size(visorW + 6f, visorH + 6f),
        cornerRadius = CornerRadius(visorH * 0.48f, visorH * 0.48f)
      )

      // Visor Glass (Cosmic Deep Indigo)
      drawRoundRect(
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xFF0F0B24), Color(0xFF1E1B4B), Color(0xFF312E81)),
          startY = visorTop,
          endY = visorTop + visorH
        ),
        topLeft = Offset(visorLeft, visorTop),
        size = Size(visorW, visorH),
        cornerRadius = CornerRadius(visorH * 0.46f, visorH * 0.46f)
      )

      // Visor 3D Specular Highlight (Curved glass reflection)
      val reflectionPath = Path().apply {
        moveTo(visorLeft + visorW * 0.14f, visorTop + visorH * 0.22f)
        cubicTo(
          visorLeft + visorW * 0.35f, visorTop + visorH * 0.14f,
          visorLeft + visorW * 0.65f, visorTop + visorH * 0.16f,
          visorLeft + visorW * 0.82f, visorTop + visorH * 0.28f
        )
        lineTo(visorLeft + visorW * 0.72f, visorTop + visorH * 0.42f)
        cubicTo(
          visorLeft + visorW * 0.55f, visorTop + visorH * 0.32f,
          visorLeft + visorW * 0.3f, visorTop + visorH * 0.3f,
          visorLeft + visorW * 0.14f, visorTop + visorH * 0.38f
        )
        close()
      }
      drawPath(reflectionPath, color = Color(0x45FFFFFF))

      // 7. Expressive Anime Eyes & Friendly Face
      val eyeCenterY = helmetCenter.y - h * 0.025f
      val eyeSpacing = w * 0.1f

      when (mood) {
        MascotMood.CELEBRATING -> {
          // Cheerful closed arched eyes (^_^)
          drawArc(
            color = Color(0xFFFDE047),
            startAngle = 190f,
            sweepAngle = 160f,
            useCenter = false,
            topLeft = Offset(currentX - eyeSpacing - w * 0.05f, eyeCenterY - h * 0.02f),
            size = Size(w * 0.1f, h * 0.05f),
            style = Stroke(width = 7f)
          )
          drawArc(
            color = Color(0xFFFDE047),
            startAngle = 190f,
            sweepAngle = 160f,
            useCenter = false,
            topLeft = Offset(currentX + eyeSpacing - w * 0.05f, eyeCenterY - h * 0.02f),
            size = Size(w * 0.1f, h * 0.05f),
            style = Stroke(width = 7f)
          )
          // Joyful open smile
          drawArc(
            color = Color(0xFFFB7185),
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = true,
            topLeft = Offset(currentX - w * 0.06f, eyeCenterY + h * 0.025f),
            size = Size(w * 0.12f, h * 0.06f)
          )
        }
        MascotMood.OOPS -> {
          // Curious wondering eyes (o_O)
          drawCircle(color = Color.White, radius = w * 0.045f, center = Offset(currentX - eyeSpacing, eyeCenterY))
          drawCircle(color = Color(0xFF0F172A), radius = w * 0.024f, center = Offset(currentX - eyeSpacing, eyeCenterY))

          drawCircle(color = Color.White, radius = w * 0.055f, center = Offset(currentX + eyeSpacing, eyeCenterY))
          drawCircle(color = Color(0xFF0F172A), radius = w * 0.03f, center = Offset(currentX + eyeSpacing, eyeCenterY))

          // Small "o" mouth
          drawCircle(color = Color(0xFFFB7185), radius = w * 0.025f, center = Offset(currentX, eyeCenterY + h * 0.05f))
        }
        else -> {
          // Cute gleaming anime eyes
          // Left Eye
          drawCircle(color = Color(0xFF38BDF8), radius = w * 0.048f, center = Offset(currentX - eyeSpacing, eyeCenterY))
          drawCircle(color = Color.White, radius = w * 0.02f, center = Offset(currentX - eyeSpacing - 3f, eyeCenterY - 3f))
          drawCircle(color = Color.White, radius = w * 0.009f, center = Offset(currentX - eyeSpacing + 4f, eyeCenterY + 4f))

          // Right Eye
          drawCircle(color = Color(0xFF38BDF8), radius = w * 0.048f, center = Offset(currentX + eyeSpacing, eyeCenterY))
          drawCircle(color = Color.White, radius = w * 0.02f, center = Offset(currentX + eyeSpacing - 3f, eyeCenterY - 3f))
          drawCircle(color = Color.White, radius = w * 0.009f, center = Offset(currentX + eyeSpacing + 4f, eyeCenterY + 4f))

          // Sweet curved smile
          drawArc(
            color = Color.White,
            startAngle = 15f,
            sweepAngle = 150f,
            useCenter = false,
            topLeft = Offset(currentX - w * 0.045f, eyeCenterY + h * 0.025f),
            size = Size(w * 0.09f, h * 0.035f),
            style = Stroke(width = 4.5f)
          )
        }
      }
    }
  }
}
