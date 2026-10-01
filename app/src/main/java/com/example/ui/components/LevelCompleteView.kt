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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
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
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.BrandSkyBlueBg
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.CardBorder
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.TextDark
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
  val star1Scale = remember { Animatable(0f) }
  val star2Scale = remember { Animatable(0f) }
  val star3Scale = remember { Animatable(0f) }

  LaunchedEffect(Unit) {
    delay(200)
    if (starsEarned >= 1) {
      star1Scale.animateTo(1.3f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
      star1Scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }
    delay(160)
    if (starsEarned >= 2) {
      star2Scale.animateTo(1.3f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
      star2Scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }
    delay(160)
    if (starsEarned >= 3) {
      star3Scale.animateTo(1.3f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
      star3Scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0x800F172A)),
    contentAlignment = Alignment.Center
  ) {
    // Dynamic celebration confetti
    ConfettiParticleExplosion(triggerKey = 999)

    Column(
      modifier = Modifier
        .fillMaxWidth(0.9f)
        .shadow(20.dp, RoundedCornerShape(36.dp), spotColor = Color(0x4038BDF8))
        .clip(RoundedCornerShape(36.dp))
        .background(Color.White)
        .border(3.dp, BrandSkyBlue.copy(alpha = 0.4f), RoundedCornerShape(36.dp))
        .padding(horizontal = 24.dp, vertical = 28.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Celebrating Cartoon Mascot
      CartoonAstronautMascot(
        sizeDp = 125.dp,
        mood = MascotMood.CELEBRATING
      )

      // 2. Victory Cheer Title (Zero Unicode emoji)
      Text(
        text = "أحسنت يا بطل!",
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 30.sp,
        color = TextDark
      )

      // 3. Staggered 3D Golden Illustrated Stars (Zero Unicode emoji)
      Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Star 1
        Box(
          modifier = Modifier
            .size(56.dp)
            .scale(star1Scale.value),
          contentAlignment = Alignment.Center
        ) {
          IllustratedStar(
            isFilled = starsEarned >= 1,
            sizeDp = 50.dp
          )
        }
        // Center Star (larger)
        Box(
          modifier = Modifier
            .size(72.dp)
            .scale(star2Scale.value),
          contentAlignment = Alignment.Center
        ) {
          IllustratedStar(
            isFilled = starsEarned >= 2,
            sizeDp = 64.dp
          )
        }
        // Star 3
        Box(
          modifier = Modifier
            .size(56.dp)
            .scale(star3Scale.value),
          contentAlignment = Alignment.Center
        ) {
          IllustratedStar(
            isFilled = starsEarned >= 3,
            sizeDp = 50.dp
          )
        }
      }

      // 4. Rewards Summary Card with custom illustrated icons
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(22.dp))
          .background(Color(0xFFF8FAFC))
          .border(1.5.dp, CardBorder, RoundedCornerShape(22.dp))
          .padding(horizontal = 26.dp, vertical = 12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            IllustratedStar(sizeDp = 26.dp, isFilled = true)
            Text(
              text = "+$starsEarned",
              fontFamily = FredokaFontFamily,
              fontSize = 26.sp,
              fontWeight = FontWeight.Bold,
              color = BrandAmber
            )
          }
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            IllustratedCoin(sizeDp = 26.dp)
            Text(
              text = "+${starsEarned * 3}",
              fontFamily = FredokaFontFamily,
              fontSize = 26.sp,
              fontWeight = FontWeight.Bold,
              color = BrandSkyBlue
            )
          }
        }
      }

      // 5. Action Buttons (Replay, Next Level, Home) with vector icons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Replay Button (Illustrated Vector)
        Box(
          modifier = Modifier
            .size(68.dp)
            .shadow(6.dp, CircleShape, spotColor = Color(0x40F97316))
            .clip(CircleShape)
            .background(Color(0xFFFFEDD5))
            .border(2.dp, Color(0xFFF97316), CircleShape)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null,
              onClick = onReplay
            )
            .testTag("replay_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "إعادة",
            tint = Color(0xFFEA580C),
            modifier = Modifier.size(32.dp)
          )
        }

        // Next Level Button (Illustrated Rocket Vector)
        if (onNextLevel != null) {
          Box(
            modifier = Modifier
              .size(78.dp)
              .shadow(10.dp, CircleShape, spotColor = BrandEmerald)
              .clip(CircleShape)
              .background(BrandEmerald)
              .border(3.dp, Color.White, CircleShape)
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onNextLevel
              )
              .testTag("next_level_button"),
            contentAlignment = Alignment.Center
          ) {
            CuteRocket(sizeDp = 48.dp)
          }
        }

        // Home Button (Illustrated Home Vector)
        Box(
          modifier = Modifier
            .size(68.dp)
            .shadow(6.dp, CircleShape, spotColor = BrandSkyBlue)
            .clip(CircleShape)
            .background(BrandSkyBlueBg)
            .border(2.dp, BrandSkyBlue, CircleShape)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null,
              onClick = onHome
            )
            .testTag("home_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "الرئيسية",
            tint = BrandSkyBlue,
            modifier = Modifier.size(32.dp)
          )
        }
      }
    }
  }
}
