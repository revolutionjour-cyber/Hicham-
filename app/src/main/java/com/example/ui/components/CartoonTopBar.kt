package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandAmberBg
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandEmeraldBg
import com.example.ui.theme.BrandEmeraldDark
import com.example.ui.theme.BrandRose
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.BrandSkyBlueBg
import com.example.ui.theme.CardBorder
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.TextDark

@Composable
fun CartoonTopBar(
  totalStars: Int,
  totalCoins: Int,
  isSoundEnabled: Boolean,
  onToggleSound: () -> Unit,
  onBackClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val starScale = remember { Animatable(1f) }
  LaunchedEffect(totalStars) {
    if (totalStars > 0) {
      starScale.animateTo(1.3f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
      starScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }
  }

  val coinScale = remember { Animatable(1f) }
  LaunchedEffect(totalCoins) {
    if (totalCoins > 0) {
      coinScale.animateTo(1.3f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
      coinScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }
  }

  Row(
    modifier = modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Left: Back Button OR Mini Avatar Badge
    if (onBackClick != null) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .shadow(4.dp, CircleShape, spotColor = BrandRose)
          .clip(CircleShape)
          .background(Color.White)
          .border(2.dp, BrandRose.copy(alpha = 0.4f), CircleShape)
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
          tint = BrandRose,
          modifier = Modifier.size(24.dp)
        )
      }
    } else {
      // Clean, modern mascot avatar token
      Box(
        modifier = Modifier
          .size(50.dp)
          .shadow(4.dp, CircleShape, spotColor = BrandSkyBlue)
          .clip(CircleShape)
          .background(BrandSkyBlueBg)
          .border(2.dp, BrandSkyBlue.copy(alpha = 0.6f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        CartoonAstronautMascot(sizeDp = 44.dp, mood = MascotMood.IDLE)
      }
    }

    // Right: Clean modern pill badges for Stars, Coins, and Sound
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Stars Badge (Warm Sunshine Card)
      Box(
        modifier = Modifier
          .scale(starScale.value)
          .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = BrandAmber)
          .clip(RoundedCornerShape(20.dp))
          .background(BrandAmberBg)
          .border(1.5.dp, BrandAmber.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .testTag("stars_badge"),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          IllustratedStar(sizeDp = 18.dp, isFilled = true)
          Text(
            text = "$totalStars",
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color(0xFFB45309)
          )
        }
      }

      // Coins Badge (Mint Card)
      Box(
        modifier = Modifier
          .scale(coinScale.value)
          .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = BrandEmerald)
          .clip(RoundedCornerShape(20.dp))
          .background(BrandEmeraldBg)
          .border(1.5.dp, BrandEmerald.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
          .padding(horizontal = 12.dp, vertical = 6.dp)
          .testTag("coins_badge"),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          IllustratedCoin(sizeDp = 18.dp)
          Text(
            text = "$totalCoins",
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color(0xFF047857)
          )
        }
      }

      // Modern Sound Toggle Button
      Box(
        modifier = Modifier
          .size(46.dp)
          .shadow(3.dp, CircleShape, spotColor = Color(0x30000000))
          .clip(CircleShape)
          .background(if (isSoundEnabled) BrandEmerald else Color.White)
          .border(
            2.dp,
            if (isSoundEnabled) BrandEmeraldDark else CardBorder,
            CircleShape
          )
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
          tint = if (isSoundEnabled) Color.White else Color(0xFF64748B),
          modifier = Modifier.size(22.dp)
        )
      }
    }
  }
}
