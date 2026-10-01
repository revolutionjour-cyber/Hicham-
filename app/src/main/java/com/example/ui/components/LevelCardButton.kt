package com.example.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandCoral
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.CardBorder
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

@Composable
fun LevelCardButton(
  level: Int,
  isUnlocked: Boolean,
  earnedStars: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.96f else 1f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessMedium
    ),
    label = "level_card_scale"
  )

  // Level theme metadata
  val (primaryColor, titleAr, subtitleAr, rangeAr) = when (level) {
    1 -> listOf(
      BrandSkyBlue,
      "عالم الأعداد الأولى",
      "العد والجمع البسيط",
      "١ - ٥"
    )
    2 -> listOf(
      BrandCoral,
      "مغامرة الجمع والطرح",
      "أرقام حتى عشرة",
      "١ - ١٠"
    )
    else -> listOf(
      BrandPurple,
      "تحدي أبطال الفضاء",
      "مسائل كبرى حتى عشرين",
      "١ - ٢٠"
    )
  }

  val accentColor = primaryColor as Color

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(116.dp)
      .scale(scale)
      .shadow(
        elevation = if (isUnlocked) 8.dp else 2.dp,
        shape = RoundedCornerShape(26.dp),
        spotColor = if (isUnlocked) accentColor.copy(alpha = 0.4f) else Color(0x20000000)
      )
      .clip(RoundedCornerShape(26.dp))
      .background(Color.White)
      .border(
        width = if (isUnlocked) 2.5.dp else 1.5.dp,
        color = if (isUnlocked) accentColor.copy(alpha = 0.45f) else CardBorder,
        shape = RoundedCornerShape(26.dp)
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .testTag("level_button_$level"),
    contentAlignment = Alignment.Center
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 1. Giant 3D Number Disc (Left)
      Box(
        modifier = Modifier
          .size(72.dp)
          .shadow(4.dp, CircleShape, spotColor = accentColor)
          .clip(CircleShape)
          .background(if (isUnlocked) accentColor else Color(0xFFF1F5F9))
          .border(
            3.dp,
            if (isUnlocked) accentColor.copy(alpha = 0.3f) else CardBorder,
            CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$level",
          fontFamily = FredokaFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 42.sp,
          color = if (isUnlocked) Color.White else TextMuted
        )
      }

      // 2. Middle Details (Title, Subtitle, Illustrated Stars)
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
      ) {
        Text(
          text = titleAr as String,
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = if (isUnlocked) TextDark else TextMuted
        )

        // Subtitle + Range Badge
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .background(if (isUnlocked) accentColor.copy(alpha = 0.12f) else Color(0xFFF1F5F9))
              .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = rangeAr as String,
              fontFamily = FredokaFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = if (isUnlocked) accentColor else TextMuted
            )
          }

          Text(
            text = subtitleAr as String,
            fontFamily = CairoFontFamily,
            fontSize = 13.sp,
            color = TextMuted
          )
        }

        // Custom Illustrated Stars (Zero Unicode emoji)
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          for (starIndex in 1..3) {
            val isEarned = isUnlocked && starIndex <= earnedStars
            IllustratedStar(isFilled = isEarned, sizeDp = 20.dp)
          }
        }
      }

      // 3. Right: Level Planet Vector or Illustrated Lock
      Box(
        modifier = Modifier.size(68.dp),
        contentAlignment = Alignment.Center
      ) {
        if (!isUnlocked) {
          Box(
            modifier = Modifier
              .size(50.dp)
              .shadow(2.dp, CircleShape)
              .clip(CircleShape)
              .background(Color(0xFFF8FAFC))
              .border(1.5.dp, CardBorder, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            IllustratedLock(sizeDp = 26.dp)
          }
        } else {
          IllustratedPlanetOrb(level = level, sizeDp = 58.dp)
        }
      }
    }
  }
}
