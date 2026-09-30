package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
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

@Composable
fun CartoonTopBar(
  totalStars: Int,
  totalCoins: Int,
  isSoundEnabled: Boolean,
  onToggleSound: () -> Unit,
  onBackClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  // Bounce animation when stars change
  val starScale = remember { Animatable(1f) }
  LaunchedEffect(totalStars) {
    if (totalStars > 0) {
      starScale.animateTo(1.35f, tween(120, easing = FastOutSlowInEasing))
      starScale.animateTo(1f, tween(150, easing = FastOutSlowInEasing))
    }
  }

  // Bounce animation when coins change
  val coinScale = remember { Animatable(1f) }
  LaunchedEffect(totalCoins) {
    if (totalCoins > 0) {
      coinScale.animateTo(1.35f, tween(120, easing = FastOutSlowInEasing))
      coinScale.animateTo(1f, tween(150, easing = FastOutSlowInEasing))
    }
  }

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Left side: Back button or Mascot Avatar
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      if (onBackClick != null) {
        // Big kid-friendly Back button
        Box(
          modifier = Modifier
            .size(52.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFEC4899), Color(0xFFBE185D))
              )
            )
            .border(2.5.dp, Color.White.copy(alpha = 0.8f), CircleShape)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null,
              onClick = onBackClick
            )
            .testTag("back_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "رجوع",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
          )
        }
      } else {
        // Mini animated mascot head badge
        Box(
          modifier = Modifier
            .size(52.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7))
              )
            )
            .border(2.5.dp, Color.White.copy(alpha = 0.8f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          CartoonAstronautMascot(
            sizeDp = 48.dp,
            mood = MascotMood.IDLE
          )
        }
      }
    }

    // Center / Right: Counters (Stars & Coins) + Sound Toggle
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Stars Badge
      Box(
        modifier = Modifier
          .scale(starScale.value)
          .shadow(4.dp, RoundedCornerShape(24.dp))
          .clip(RoundedCornerShape(24.dp))
          .background(
            brush = Brush.horizontalGradient(
              colors = listOf(Color(0xFFFDE047), Color(0xFFF59E0B))
            )
          )
          .border(2.dp, Color.White, RoundedCornerShape(24.dp))
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .testTag("stars_badge"),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = "⭐",
            fontSize = 18.sp
          )
          Text(
            text = "$totalStars",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF78350F)
          )
        }
      }

      // Coins Badge
      Box(
        modifier = Modifier
          .scale(coinScale.value)
          .shadow(4.dp, RoundedCornerShape(24.dp))
          .clip(RoundedCornerShape(24.dp))
          .background(
            brush = Brush.horizontalGradient(
              colors = listOf(Color(0xFF38BDF8), Color(0xFF0284C7))
            )
          )
          .border(2.dp, Color.White, RoundedCornerShape(24.dp))
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .testTag("coins_badge"),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = "🪙",
            fontSize = 18.sp
          )
          Text(
            text = "$totalCoins",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
          )
        }
      }

      // Big Sound Button 🔊 / 🔇
      Box(
        modifier = Modifier
          .size(52.dp)
          .shadow(6.dp, CircleShape)
          .clip(CircleShape)
          .background(
            brush = Brush.verticalGradient(
              colors = if (isSoundEnabled) {
                listOf(Color(0xFF10B981), Color(0xFF059669))
              } else {
                listOf(Color(0xFF64748B), Color(0xFF475569))
              }
            )
          )
          .border(2.5.dp, Color.White.copy(alpha = 0.8f), CircleShape)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onToggleSound
          )
          .testTag("sound_toggle_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isSoundEnabled) Icons.Filled.VolumeUp else Icons.Filled.VolumeOff,
          contentDescription = if (isSoundEnabled) "صوت مفعّل" else "صوت معطّل",
          tint = Color.White,
          modifier = Modifier.size(28.dp)
        )
      }
    }
  }
}
