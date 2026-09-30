package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun LevelCardButton(
  level: Int,
  isUnlocked: Boolean,
  earnedStars: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scale = remember { Animatable(1f) }
  val scope = rememberCoroutineScope()

  // Gradient themes per level
  val backgroundBrush = when (level) {
    1 -> Brush.horizontalGradient(
      colors = listOf(Color(0xFF0284C7), Color(0xFF0EA5E9), Color(0xFF38BDF8))
    )
    2 -> Brush.horizontalGradient(
      colors = listOf(Color(0xFFEA580C), Color(0xFFF97316), Color(0xFFFBBF24))
    )
    else -> Brush.horizontalGradient(
      colors = listOf(Color(0xFF7E22CE), Color(0xFFA855F7), Color(0xFFEC4899))
    )
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(115.dp)
      .scale(scale.value)
      .shadow(10.dp, RoundedCornerShape(26.dp))
      .clip(RoundedCornerShape(26.dp))
      .background(backgroundBrush)
      .border(
        3.5.dp,
        if (isUnlocked) Color.White.copy(alpha = 0.9f) else Color(0x60FFFFFF),
        RoundedCornerShape(26.dp)
      )
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = {
          scope.launch {
            scale.animateTo(0.94f, tween(80, easing = FastOutSlowInEasing))
            scale.animateTo(1f, tween(120, easing = FastOutSlowInEasing))
            onClick()
          }
        }
      )
      .padding(horizontal = 18.dp, vertical = 10.dp)
      .testTag("level_button_$level"),
    contentAlignment = Alignment.Center
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left: Giant Level Number
      Box(
        modifier = Modifier
          .size(76.dp)
          .shadow(6.dp, CircleShape)
          .clip(CircleShape)
          .background(Color.White)
          .border(
            3.dp,
            when (level) {
              1 -> Color(0xFF0284C7)
              2 -> Color(0xFFEA580C)
              else -> Color(0xFF7E22CE)
            },
            CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$level",
          fontSize = 46.sp,
          fontWeight = FontWeight.Black,
          color = when (level) {
            1 -> Color(0xFF0284C7)
            2 -> Color(0xFFEA580C)
            else -> Color(0xFF7E22CE)
          }
        )
      }

      // Center: Level Cartoon Drawing & Stars
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // Visual theme badge
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          when (level) {
            1 -> {
              Text(text = "🚀", fontSize = 28.sp)
              Text(
                text = "١ - ٥",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
            }
            2 -> {
              Text(text = "🪐", fontSize = 28.sp)
              Text(
                text = "١ - ١٠",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
            }
            3 -> {
              Text(text = "🌌", fontSize = 28.sp)
              Text(
                text = "١ - ٢٠",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
            }
          }
        }

        // Stars Earned Rating (⭐⭐⭐)
        Row(
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          for (starIndex in 1..3) {
            val isEarned = isUnlocked && starIndex <= earnedStars
            Text(
              text = if (isEarned) "⭐" else "☆",
              fontSize = 20.sp,
              color = if (isEarned) Color(0xFFFDE047) else Color(0x80FFFFFF)
            )
          }
        }
      }

      // Right: Level Planet Icon or Lock
      Box(
        modifier = Modifier.size(72.dp),
        contentAlignment = Alignment.Center
      ) {
        if (!isUnlocked) {
          // Cute lock badge
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(Color(0x60000000)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "🔒", fontSize = 28.sp)
          }
        } else {
          CartoonPlanet(level = level, sizeDp = 70.dp)
        }
      }
    }
  }
}
