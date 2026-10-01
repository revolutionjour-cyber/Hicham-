package com.example.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KidGameState
import com.example.ui.components.IllustratedLock
import com.example.ui.components.IllustratedStar
import com.example.ui.components.LuxuryGemIcon
import com.example.ui.components.LuxuryGemType
import com.example.ui.components.RoyalCrownArtifact
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.FredokaFontFamily

@Composable
fun RoyalHallHome(
  gameState: KidGameState,
  onStartChamber: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val nextChamber = when {
    gameState.unlockedLevels.contains(3) && (gameState.levelStars[3] ?: 0) < 3 -> 3
    gameState.unlockedLevels.contains(2) && (gameState.levelStars[2] ?: 0) < 3 -> 2
    gameState.unlockedLevels.contains(2) -> 2
    else -> 1
  }

  val startInteraction = remember { MutableInteractionSource() }
  val isStartPressed by startInteraction.collectIsPressedAsState()
  val startScale by animateFloatAsState(
    targetValue = if (isStartPressed) 0.95f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "royal_enter_scale"
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Royal Pedestal & Crown Card (Grand Palace Hall Banner)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(12.dp, RoundedCornerShape(32.dp), spotColor = Color(0x60FBBF24))
        .clip(RoundedCornerShape(32.dp))
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color(0xFF1E293B),
              Color(0xFF0F172A)
            )
          )
        )
        .border(2.dp, Color(0xFFFDE68A).copy(alpha = 0.5f), RoundedCornerShape(32.dp))
        .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(
          verticalArrangement = Arrangement.spacedBy(6.dp),
          horizontalAlignment = Alignment.Start
        ) {
          Text(
            text = "أهلاً بك في قصر الأرقام",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = Color(0xFFFDE68A)
          )

          Text(
            text = "الأمير/ة ${gameState.playerName}",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF38BDF8)
          )

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            IllustratedStar(sizeDp = 18.dp, isFilled = true)
            val totalStars = gameState.levelStars.values.sum()
            Text(
              text = "$totalStars من 9 أوسمة ملكية",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color(0xFFFBBF24)
            )
          }
        }

        RoyalCrownArtifact(sizeDp = 80.dp)
      }
    }

    // 2. Master Gold Entrance Button (Enter the Royal Sanctuary)
    Box(
      modifier = Modifier
        .scale(startScale)
        .fillMaxWidth()
        .height(72.dp)
        .shadow(12.dp, RoundedCornerShape(26.dp), spotColor = Color(0xFFD97706))
        .clip(RoundedCornerShape(26.dp))
        .background(Color(0xFF78350F))
        .clickable(
          interactionSource = startInteraction,
          indication = null,
          onClick = { onStartChamber(nextChamber) }
        )
        .testTag("enter_palace_button"),
      contentAlignment = Alignment.TopCenter
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(66.dp)
          .clip(RoundedCornerShape(26.dp))
          .background(
            Brush.verticalGradient(
              listOf(Color(0xFFFDE047), Color(0xFFD97706))
            )
          )
          .border(2.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(26.dp)),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          LuxuryGemIcon(gemType = LuxuryGemType.TOPAZ, sizeDp = 34.dp)
          Text(
            text = "ادخل قاعة الألغاز الملكية",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = Color(0xFF451A03)
          )
        }
      }
    }

    // 3. The 3 Royal Chambers
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      RoyalChamberCard(
        chamberNumber = 1,
        title = "قاعة الياقوت الإمبراطوري (١ - ٥)",
        gemType = LuxuryGemType.RUBY,
        isUnlocked = gameState.unlockedLevels.contains(1),
        isCurrent = nextChamber == 1,
        starsEarned = gameState.levelStars[1] ?: 0,
        accentColor = Color(0xFFF43F5E),
        onClick = { onStartChamber(1) }
      )

      RoyalChamberCard(
        chamberNumber = 2,
        title = "بهو الزمرد الفاخر (١ - ١٠)",
        gemType = LuxuryGemType.EMERALD,
        isUnlocked = gameState.unlockedLevels.contains(2),
        isCurrent = nextChamber == 2,
        starsEarned = gameState.levelStars[2] ?: 0,
        accentColor = Color(0xFF10B981),
        onClick = { onStartChamber(2) }
      )

      RoyalChamberCard(
        chamberNumber = 3,
        title = "قبة الألماس والياقوت الأزرق (١ - ١٥)",
        gemType = LuxuryGemType.SAPPHIRE,
        isUnlocked = gameState.unlockedLevels.contains(3),
        isCurrent = nextChamber == 3,
        starsEarned = gameState.levelStars[3] ?: 0,
        accentColor = Color(0xFF38BDF8),
        onClick = { onStartChamber(3) }
      )
    }

    Spacer(modifier = Modifier.height(14.dp))
  }
}

@Composable
private fun RoyalChamberCard(
  chamberNumber: Int,
  title: String,
  gemType: LuxuryGemType,
  isUnlocked: Boolean,
  isCurrent: Boolean,
  starsEarned: Int,
  accentColor: Color,
  onClick: () -> Unit
) {
  val interaction = remember { MutableInteractionSource() }
  val isPressed by interaction.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.96f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "chamber_scale"
  )

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .scale(scale)
      .shadow(
        elevation = if (isCurrent) 10.dp else if (isUnlocked) 4.dp else 1.dp,
        shape = RoundedCornerShape(26.dp),
        spotColor = if (isUnlocked) accentColor.copy(alpha = 0.5f) else Color(0x20000000)
      )
      .clip(RoundedCornerShape(26.dp))
      .background(Color(0xFF0F172A).copy(alpha = 0.90f))
      .border(
        width = if (isCurrent) 2.5.dp else 1.5.dp,
        color = if (isCurrent) accentColor else if (isUnlocked) accentColor.copy(alpha = 0.4f) else Color(0xFF334155),
        shape = RoundedCornerShape(26.dp)
      )
      .clickable(
        interactionSource = interaction,
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 18.dp, vertical = 14.dp)
      .testTag("chamber_card_$chamberNumber")
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Golden Disc Number
      Box(
        modifier = Modifier
          .size(54.dp)
          .shadow(4.dp, CircleShape, spotColor = accentColor)
          .clip(CircleShape)
          .background(if (isUnlocked) accentColor else Color(0xFF1E293B)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$chamberNumber",
          fontFamily = FredokaFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 30.sp,
          color = if (isUnlocked) Color.White else Color(0xFF64748B)
        )
      }

      // Title & Stars
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
      ) {
        Text(
          text = title,
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = if (isUnlocked) Color(0xFFF8FAFC) else Color(0xFF64748B)
        )

        Row(
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          for (s in 1..3) {
            IllustratedStar(isFilled = isUnlocked && s <= starsEarned, sizeDp = 18.dp)
          }
        }
      }

      // Cut Gemstone or Lock
      Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
      ) {
        if (!isUnlocked) {
          IllustratedLock(sizeDp = 26.dp)
        } else {
          LuxuryGemIcon(gemType = gemType, sizeDp = 44.dp)
        }
      }
    }
  }
}
