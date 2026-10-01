package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandCoral
import com.example.ui.theme.BrandCoralDark
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandEmeraldDark
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleDark
import com.example.ui.theme.BrandRose
import com.example.ui.theme.BrandRoseDark
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.BrandSkyBlueDark
import com.example.ui.theme.FredokaFontFamily
import kotlin.math.roundToInt

enum class AnswerButtonState {
  DEFAULT,
  CORRECT,
  WRONG
}

@Composable
fun KidAnswerButton(
  value: Int,
  index: Int,
  state: AnswerButtonState,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  // 3D tactile press down offset (Duolingo-like feel)
  val pressOffset by animateDpAsState(
    targetValue = if (isPressed) 4.dp else 0.dp,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessMedium
    ),
    label = "tactile_press"
  )

  // Gentle horizontal shake on wrong answer
  val shakeOffset = remember { Animatable(0f) }
  LaunchedEffect(state) {
    if (state == AnswerButtonState.WRONG) {
      shakeOffset.animateTo(-10f, tween(40))
      shakeOffset.animateTo(10f, tween(40))
      shakeOffset.animateTo(-6f, tween(40))
      shakeOffset.animateTo(6f, tween(40))
      shakeOffset.animateTo(0f, tween(40))
    } else {
      shakeOffset.snapTo(0f)
    }
  }

  // 4 Cheerful Modern Palettes
  val (faceColor, bevelColor) = when (index % 4) {
    0 -> Pair(BrandSkyBlue, BrandSkyBlueDark)
    1 -> Pair(BrandCoral, BrandCoralDark)
    2 -> Pair(BrandEmerald, BrandEmeraldDark)
    else -> Pair(BrandPurple, BrandPurpleDark)
  }

  val targetFace = when (state) {
    AnswerButtonState.CORRECT -> BrandEmerald
    AnswerButtonState.WRONG -> BrandRose
    AnswerButtonState.DEFAULT -> faceColor
  }

  val targetBevel = when (state) {
    AnswerButtonState.CORRECT -> BrandEmeraldDark
    AnswerButtonState.WRONG -> BrandRoseDark
    AnswerButtonState.DEFAULT -> bevelColor
  }

  val animatedFace by animateColorAsState(targetFace, label = "face_c")
  val animatedBevel by animateColorAsState(targetBevel, label = "bevel_c")

  // Outer container: responsive width and comfortable height
  Box(
    modifier = modifier
      .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
      .height(72.dp)
      .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = animatedBevel)
      .clip(RoundedCornerShape(22.dp))
      .background(animatedBevel)
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .testTag("answer_button_$value"),
    contentAlignment = Alignment.TopCenter
  ) {
    // Top Push Face that moves down when pressed
    Box(
      modifier = Modifier
        .offset(y = pressOffset)
        .fillMaxWidth()
        .height(66.dp)
        .clip(RoundedCornerShape(22.dp))
        .background(animatedFace)
        .border(1.5.dp, Color.White.copy(alpha = 0.65f), RoundedCornerShape(22.dp)),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "$value",
        fontFamily = FredokaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = Color.White
      )
    }
  }
}
