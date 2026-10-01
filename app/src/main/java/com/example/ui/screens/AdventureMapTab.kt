package com.example.ui.screens

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KidGameState
import com.example.ui.components.CartoonAstronautMascot
import com.example.ui.components.CuteRocket
import com.example.ui.components.IllustratedLock
import com.example.ui.components.IllustratedPlanetOrb
import com.example.ui.components.IllustratedStar
import com.example.ui.components.MascotMood
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCoral
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandEmeraldDark
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.CardBorder
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

@Composable
fun AdventureMapTab(
  gameState: KidGameState,
  onSelectLevel: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val nextLevel = when {
    gameState.unlockedLevels.contains(3) && (gameState.levelStars[3] ?: 0) < 3 -> 3
    gameState.unlockedLevels.contains(2) && (gameState.levelStars[2] ?: 0) < 3 -> 2
    gameState.unlockedLevels.contains(2) -> 2
    else -> 1
  }

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseAnim by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.025f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  val startBtnInteraction = remember { MutableInteractionSource() }
  val isStartPressed by startBtnInteraction.collectIsPressedAsState()
  val startBtnScale by animateFloatAsState(
    targetValue = if (isStartPressed) 0.94f else pulseAnim,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "start_press"
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. CLEAN, MINIMAL HERO GREETING (No walls of text, no cluttered tags)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(6.dp, RoundedCornerShape(28.dp), spotColor = Color(0x2538BDF8))
        .clip(RoundedCornerShape(28.dp))
        .background(Color.White)
        .border(1.5.dp, CardBorder, RoundedCornerShape(28.dp))
        .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(
          verticalArrangement = Arrangement.spacedBy(4.dp),
          horizontalAlignment = Alignment.Start
        ) {
          Text(
            text = "مرحباً يا ${gameState.playerName}!",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = TextDark
          )

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            IllustratedStar(sizeDp = 18.dp, isFilled = true)
            val totalStars = gameState.levelStars.values.sum()
            Text(
              text = "$totalStars من 9 نجوم",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = BrandAmber
            )
          }
        }

        // Mascot Character
        CartoonAstronautMascot(
          sizeDp = 80.dp,
          mood = MascotMood.IDLE
        )
      }
    }

    // 2. LARGE 3D START BUTTON (Direct, clear, satisfying)
    Box(
      modifier = Modifier
        .scale(startBtnScale)
        .fillMaxWidth()
        .height(70.dp)
        .shadow(10.dp, RoundedCornerShape(24.dp), spotColor = BrandEmeraldDark)
        .clip(RoundedCornerShape(24.dp))
        .background(BrandEmeraldDark)
        .clickable(
          interactionSource = startBtnInteraction,
          indication = null,
          onClick = { onSelectLevel(nextLevel) }
        )
        .testTag("start_learning_button"),
      contentAlignment = Alignment.TopCenter
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(64.dp)
          .clip(RoundedCornerShape(24.dp))
          .background(
            Brush.verticalGradient(
              listOf(BrandEmerald, Color(0xFF059669))
            )
          )
          .border(2.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          CuteRocket(sizeDp = 34.dp)
          Text(
            text = "انطلق في المغامرة!",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = Color.White
          )
        }
      }
    }

    // 3. CLEAN INTERACTIVE ADVENTURE ISLANDS (Minimal text, clear numbers, authentic art)
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Stage 1 Island
      CleanMapIsland(
        level = 1,
        title = "الأعداد الأولى (١ - ٥)",
        isUnlocked = gameState.unlockedLevels.contains(1),
        isCurrent = nextLevel == 1,
        starsEarned = gameState.levelStars[1] ?: 0,
        color = BrandSkyBlue,
        onClick = { onSelectLevel(1) }
      )

      TrailDots(isUnlocked = gameState.unlockedLevels.contains(2))

      // Stage 2 Island
      CleanMapIsland(
        level = 2,
        title = "الجمع والطرح (١ - ١٠)",
        isUnlocked = gameState.unlockedLevels.contains(2),
        isCurrent = nextLevel == 2,
        starsEarned = gameState.levelStars[2] ?: 0,
        color = BrandCoral,
        onClick = { onSelectLevel(2) }
      )

      TrailDots(isUnlocked = gameState.unlockedLevels.contains(3))

      // Stage 3 Island
      CleanMapIsland(
        level = 3,
        title = "تحدي الأبطال (١ - ٢٠)",
        isUnlocked = gameState.unlockedLevels.contains(3),
        isCurrent = nextLevel == 3,
        starsEarned = gameState.levelStars[3] ?: 0,
        color = BrandPurple,
        onClick = { onSelectLevel(3) }
      )
    }

    Spacer(modifier = Modifier.height(14.dp))
  }
}

@Composable
private fun CleanMapIsland(
  level: Int,
  title: String,
  isUnlocked: Boolean,
  isCurrent: Boolean,
  starsEarned: Int,
  color: Color,
  onClick: () -> Unit
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.96f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "island_scale"
  )

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .scale(scale)
      .shadow(
        elevation = if (isCurrent) 8.dp else if (isUnlocked) 4.dp else 1.dp,
        shape = RoundedCornerShape(24.dp),
        spotColor = if (isUnlocked) color.copy(alpha = 0.35f) else Color(0x15000000)
      )
      .clip(RoundedCornerShape(24.dp))
      .background(Color.White)
      .border(
        width = if (isCurrent) 2.5.dp else 1.5.dp,
        color = if (isCurrent) color else if (isUnlocked) color.copy(alpha = 0.35f) else CardBorder,
        shape = RoundedCornerShape(24.dp)
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 18.dp, vertical = 14.dp)
      .testTag("map_stage_$level")
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Disc Number
      Box(
        modifier = Modifier
          .size(56.dp)
          .shadow(3.dp, CircleShape, spotColor = color)
          .clip(CircleShape)
          .background(if (isUnlocked) color else Color(0xFFF1F5F9)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$level",
          fontFamily = FredokaFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 32.sp,
          color = if (isUnlocked) Color.White else TextMuted
        )
      }

      // Title & 3 Stars
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
      ) {
        Text(
          text = title,
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = if (isUnlocked) TextDark else TextMuted
        )

        Row(
          horizontalArrangement = Arrangement.spacedBy(5.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          for (s in 1..3) {
            IllustratedStar(isFilled = isUnlocked && s <= starsEarned, sizeDp = 18.dp)
          }
        }
      }

      // Real Planet Illustration or Lock
      Box(
        modifier = Modifier.size(50.dp),
        contentAlignment = Alignment.Center
      ) {
        if (!isUnlocked) {
          IllustratedLock(sizeDp = 26.dp)
        } else {
          IllustratedPlanetOrb(level = level, sizeDp = 44.dp)
        }
      }
    }
  }
}

@Composable
private fun TrailDots(isUnlocked: Boolean) {
  Canvas(
    modifier = Modifier.size(width = 24.dp, height = 18.dp)
  ) {
    val h = size.height
    val cx = size.width * 0.5f
    val c = if (isUnlocked) Color(0xFF38BDF8) else Color(0xFFCBD5E1)
    drawLine(
      color = c,
      start = Offset(cx, 2f),
      end = Offset(cx, h - 2f),
      strokeWidth = 6f,
      cap = StrokeCap.Round
    )
  }
}
