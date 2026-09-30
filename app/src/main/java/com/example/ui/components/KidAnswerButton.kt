package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
  // Tactile bounce scale
  val scale = remember { Animatable(1f) }
  // Shake offset on wrong
  val shakeOffset = remember { Animatable(0f) }

  LaunchedEffect(state) {
    when (state) {
      AnswerButtonState.CORRECT -> {
        scale.animateTo(1.18f, tween(120, easing = FastOutSlowInEasing))
        scale.animateTo(1f, tween(140, easing = FastOutSlowInEasing))
      }
      AnswerButtonState.WRONG -> {
        shakeOffset.animateTo(-16f, tween(60))
        shakeOffset.animateTo(16f, tween(60))
        shakeOffset.animateTo(-10f, tween(60))
        shakeOffset.animateTo(10f, tween(60))
        shakeOffset.animateTo(0f, tween(60))
      }
      AnswerButtonState.DEFAULT -> {
        shakeOffset.snapTo(0f)
      }
    }
  }

  // Cheerful colorful button palettes
  val colorPalette = when (index % 4) {
    0 -> Pair(Color(0xFF38BDF8), Color(0xFF0284C7)) // Sky Blue
    1 -> Pair(Color(0xFFFB923C), Color(0xFFEA580C)) // Tangerine Orange
    2 -> Pair(Color(0xFF4ADE80), Color(0xFF16A34A)) // Fresh Green
    else -> Pair(Color(0xFFA855F7), Color(0xFF7E22CE)) // Cosmic Purple
  }

  val targetColors = when (state) {
    AnswerButtonState.CORRECT -> Pair(Color(0xFF22C55E), Color(0xFF15803D))
    AnswerButtonState.WRONG -> Pair(Color(0xFFF43F5E), Color(0xFFBE123C))
    AnswerButtonState.DEFAULT -> colorPalette
  }

  val topColor by animateColorAsState(targetColors.first, label = "top_c")
  val bottomColor by animateColorAsState(targetColors.second, label = "bot_c")

  Box(
    modifier = modifier
      .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
      .scale(scale.value)
      .defaultMinSize(minWidth = 84.dp, minHeight = 78.dp)
      .shadow(8.dp, RoundedCornerShape(22.dp))
      .clip(RoundedCornerShape(22.dp))
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(topColor, bottomColor)
        )
      )
      .border(3.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(22.dp))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 20.dp, vertical = 12.dp)
      .testTag("answer_button_$value"),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "$value",
      fontSize = 42.sp,
      fontWeight = FontWeight.Black,
      color = Color.White
    )
  }
}
