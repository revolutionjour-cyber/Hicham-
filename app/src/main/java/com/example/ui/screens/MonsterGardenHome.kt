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
import com.example.ui.components.ClayCupcake
import com.example.ui.components.ClayDonut
import com.example.ui.components.ClayFoodType
import com.example.ui.components.ClayMonsterCharacter
import com.example.ui.components.ClayStarCandy
import com.example.ui.components.ClayStrawberry
import com.example.ui.components.IllustratedLock
import com.example.ui.components.IllustratedStar
import com.example.ui.components.MonsterExpression
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCoral
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandEmeraldDark
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

@Composable
fun MonsterGardenHome(
  gameState: KidGameState,
  onStartLevel: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val nextLevel = when {
    gameState.unlockedLevels.contains(3) && (gameState.levelStars[3] ?: 0) < 3 -> 3
    gameState.unlockedLevels.contains(2) && (gameState.levelStars[2] ?: 0) < 3 -> 2
    gameState.unlockedLevels.contains(2) -> 2
    else -> 1
  }

  val startInteraction = remember { MutableInteractionSource() }
  val isStartPressed by startInteraction.collectIsPressedAsState()
  val startScale by animateFloatAsState(
    targetValue = if (isStartPressed) 0.94f else 1f,
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
    // 1. Warm Clay Character Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(8.dp, RoundedCornerShape(32.dp), spotColor = Color(0x2538BDF8))
        .clip(RoundedCornerShape(32.dp))
        .background(Color.White)
        .border(2.dp, CardBorder, RoundedCornerShape(32.dp))
        .padding(horizontal = 20.dp, vertical = 16.dp)
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
            text = "مرحباً يا ${gameState.playerName}!",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = TextDark
          )

          Text(
            text = "بوبو ينتظر وجبته اللذيذة!",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = BrandSkyBlue
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

        ClayMonsterCharacter(
          sizeDp = 90.dp,
          expression = MonsterExpression.HAPPY
        )
      }
    }

    // 2. Big 3D Clay Play Button
    Box(
      modifier = Modifier
        .scale(startScale)
        .fillMaxWidth()
        .height(72.dp)
        .shadow(10.dp, RoundedCornerShape(26.dp), spotColor = BrandEmeraldDark)
        .clip(RoundedCornerShape(26.dp))
        .background(BrandEmeraldDark)
        .clickable(
          interactionSource = startInteraction,
          indication = null,
          onClick = { onStartLevel(nextLevel) }
        )
        .testTag("start_feeding_button"),
      contentAlignment = Alignment.TopCenter
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(66.dp)
          .clip(RoundedCornerShape(26.dp))
          .background(
            Brush.verticalGradient(
              listOf(BrandEmerald, Color(0xFF059669))
            )
          )
          .border(2.dp, Color.White.copy(alpha = 0.65f), RoundedCornerShape(26.dp)),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          ClayStrawberry(sizeDp = 34.dp)
          Text(
            text = "أطعم بوبو الآن!",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = Color.White
          )
        }
      }
    }

    // 3. Tactile Stages (Mouthwatering themed food challenges)
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      ClayStageCard(
        level = 1,
        title = "مزرعة الفراولة (١ - ٥)",
        foodType = ClayFoodType.STRAWBERRY,
        isUnlocked = gameState.unlockedLevels.contains(1),
        isCurrent = nextLevel == 1,
        starsEarned = gameState.levelStars[1] ?: 0,
        color = BrandCoral,
        onClick = { onStartLevel(1) }
      )

      ClayStageCard(
        level = 2,
        title = "مخبز الكعك اللذيذ (١ - ١٠)",
        foodType = ClayFoodType.CUPCAKE,
        isUnlocked = gameState.unlockedLevels.contains(2),
        isCurrent = nextLevel == 2,
        starsEarned = gameState.levelStars[2] ?: 0,
        color = BrandSkyBlue,
        onClick = { onStartLevel(2) }
      )

      ClayStageCard(
        level = 3,
        title = "وليمة الحلوى الكبرى (١ - ١٥)",
        foodType = ClayFoodType.DONUT,
        isUnlocked = gameState.unlockedLevels.contains(3),
        isCurrent = nextLevel == 3,
        starsEarned = gameState.levelStars[3] ?: 0,
        color = BrandPurple,
        onClick = { onStartLevel(3) }
      )
    }

    Spacer(modifier = Modifier.height(14.dp))
  }
}

@Composable
private fun ClayStageCard(
  level: Int,
  title: String,
  foodType: ClayFoodType,
  isUnlocked: Boolean,
  isCurrent: Boolean,
  starsEarned: Int,
  color: Color,
  onClick: () -> Unit
) {
  val interaction = remember { MutableInteractionSource() }
  val isPressed by interaction.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.96f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "card_scale"
  )

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .scale(scale)
      .shadow(
        elevation = if (isCurrent) 8.dp else if (isUnlocked) 4.dp else 1.dp,
        shape = RoundedCornerShape(26.dp),
        spotColor = if (isUnlocked) color.copy(alpha = 0.35f) else Color(0x15000000)
      )
      .clip(RoundedCornerShape(26.dp))
      .background(Color.White)
      .border(
        width = if (isCurrent) 2.5.dp else 1.5.dp,
        color = if (isCurrent) color else if (isUnlocked) color.copy(alpha = 0.35f) else CardBorder,
        shape = RoundedCornerShape(26.dp)
      )
      .clickable(
        interactionSource = interaction,
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 18.dp, vertical = 14.dp)
      .testTag("stage_card_$level")
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Disc Number
      Box(
        modifier = Modifier
          .size(54.dp)
          .shadow(3.dp, CircleShape, spotColor = color)
          .clip(CircleShape)
          .background(if (isUnlocked) color else Color(0xFFF1F5F9)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$level",
          fontFamily = FredokaFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 30.sp,
          color = if (isUnlocked) Color.White else TextMuted
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
          color = if (isUnlocked) TextDark else TextMuted
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

      // Food Illustration or Lock
      Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
      ) {
        if (!isUnlocked) {
          IllustratedLock(sizeDp = 26.dp)
        } else {
          when (foodType) {
            ClayFoodType.STRAWBERRY -> ClayStrawberry(sizeDp = 44.dp)
            ClayFoodType.CUPCAKE -> ClayCupcake(sizeDp = 44.dp)
            ClayFoodType.DONUT -> ClayDonut(sizeDp = 44.dp)
            ClayFoodType.STAR_CANDY -> ClayStarCandy(sizeDp = 44.dp)
          }
        }
      }
    }
  }
}
