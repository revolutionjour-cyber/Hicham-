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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun LevelCompleteView(
  level: Int,
  starsEarned: Int,
  totalStars: Int,
  totalCoins: Int,
  onReplay: () -> Unit,
  onNextLevel: (() -> Unit)?,
  onHome: () -> Unit,
  modifier: Modifier = Modifier
) {
  // Staggered star animation
  val star1Scale = remember { Animatable(0f) }
  val star2Scale = remember { Animatable(0f) }
  val star3Scale = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    delay(200)
    if (starsEarned >= 1) {
      star1Scale.animateTo(1.2f, tween(200, easing = FastOutSlowInEasing))
      star1Scale.animateTo(1f, tween(150, easing = FastOutSlowInEasing))
    }
    delay(150)
    if (starsEarned >= 2) {
      star2Scale.animateTo(1.2f, tween(200, easing = FastOutSlowInEasing))
      star2Scale.animateTo(1f, tween(150, easing = FastOutSlowInEasing))
    }
    delay(150)
    if (starsEarned >= 3) {
      star3Scale.animateTo(1.2f, tween(200, easing = FastOutSlowInEasing))
      star3Scale.animateTo(1f, tween(150, easing = FastOutSlowInEasing))
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xCC0B041E)),
    contentAlignment = Alignment.Center
  ) {
    // Confetti particles in background
    ConfettiParticleExplosion(triggerKey = 999)

    Column(
      modifier = Modifier
        .fillMaxWidth(0.9f)
        .shadow(16.dp, RoundedCornerShape(36.dp))
        .clip(RoundedCornerShape(36.dp))
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF2E1065), Color(0xFF1E1B4B), Color(0xFF0F172A))
          )
        )
        .border(4.dp, Color(0xFF818CF8), RoundedCornerShape(36.dp))
        .padding(horizontal = 24.dp, vertical = 28.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Jumping Happy Astronaut Mascot
      CartoonAstronautMascot(
        sizeDp = 130.dp,
        mood = MascotMood.CELEBRATING
      )

      // Short Arabic cheer
      Text(
        text = "أحسنت يا بطل! 🌟",
        fontSize = 32.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFFFDE047)
      )

      // 3 Big Animated Stars ⭐⭐⭐
      Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Star 1
        Box(
          modifier = Modifier
            .size(54.dp)
            .scale(star1Scale.value),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (starsEarned >= 1) "⭐" else "☆",
            fontSize = 44.sp,
            color = Color(0xFFFDE047)
          )
        }
        // Center Star (slightly larger)
        Box(
          modifier = Modifier
            .size(66.dp)
            .scale(star2Scale.value),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (starsEarned >= 2) "⭐" else "☆",
            fontSize = 56.sp,
            color = Color(0xFFFDE047)
          )
        }
        // Star 3
        Box(
          modifier = Modifier
            .size(54.dp)
            .scale(star3Scale.value),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (starsEarned >= 3) "⭐" else "☆",
            fontSize = 44.sp,
            color = Color(0xFFFDE047)
          )
        }
      }

      // Total Points Card
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0x356366F1))
          .border(2.dp, Color(0x60A5B4FC), RoundedCornerShape(20.dp))
          .padding(horizontal = 24.dp, vertical = 10.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = "⭐", fontSize = 24.sp)
            Text(text = "+$starsEarned", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFFFDE047))
          }
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = "🪙", fontSize = 24.sp)
            Text(text = "+${starsEarned * 3}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFF38BDF8))
          }
        }
      }

      // 3 Giant Action Buttons: Replay 🔄, Next 🚀, Home 🏠
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Replay Button 🔄
        Box(
          modifier = Modifier
            .size(68.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFB923C), Color(0xFFEA580C))
              )
            )
            .border(3.dp, Color.White, CircleShape)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null,
              onClick = onReplay
            )
            .testTag("replay_button"),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "🔄", fontSize = 32.sp)
        }

        // Next Level Button 🚀 ➡️ (if available)
        if (onNextLevel != null) {
          Box(
            modifier = Modifier
              .size(78.dp)
              .shadow(8.dp, CircleShape)
              .clip(CircleShape)
              .background(
                brush = Brush.verticalGradient(
                  colors = listOf(Color(0xFF22C55E), Color(0xFF16A34A))
                )
              )
              .border(3.5.dp, Color.White, CircleShape)
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onNextLevel
              )
              .testTag("next_level_button"),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "🚀", fontSize = 40.sp)
          }
        }

        // Home Button 🏠
        Box(
          modifier = Modifier
            .size(68.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7))
              )
            )
            .border(3.dp, Color.White, CircleShape)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null,
              onClick = onHome
            )
            .testTag("home_button"),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "🏠", fontSize = 32.sp)
        }
      }
    }
  }
}
