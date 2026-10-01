package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KidGameState
import com.example.ui.components.CuteRocket
import com.example.ui.components.IllustratedCoin
import com.example.ui.components.IllustratedGem
import com.example.ui.components.IllustratedLock
import com.example.ui.components.IllustratedPlanetOrb
import com.example.ui.components.IllustratedRobot
import com.example.ui.components.IllustratedStar
import com.example.ui.components.IllustratedTrophy
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.BrandSkyBlueBg
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.CardBorder
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMedium
import com.example.ui.theme.TextMuted

private data class RewardBadge(
  val id: String,
  val title: String,
  val description: String,
  val isUnlocked: Boolean,
  val iconType: Int // 0: trophy, 1: star, 2: gem, 3: rocket, 4: coin, 5: robot
)

@Composable
fun RewardsCabinetTab(
  gameState: KidGameState,
  modifier: Modifier = Modifier
) {
  val badges = listOf(
    RewardBadge(
      id = "b1",
      title = "مستكشف الأرقام",
      description = "الانطلاق في أول رحلة على خريطة الرياضيات",
      isUnlocked = true,
      iconType = 3
    ),
    RewardBadge(
      id = "b2",
      title = "نجم الجمع السريع",
      description = "الحصول على 3 نجوم في جزيرة الأعداد الأولى",
      isUnlocked = (gameState.levelStars[1] ?: 0) >= 3,
      iconType = 1
    ),
    RewardBadge(
      id = "b3",
      title = "مستكشف الواحة الذكية",
      description = "فتح واحة الجمع والطرح بنجاح",
      isUnlocked = gameState.unlockedLevels.contains(2),
      iconType = 2
    ),
    RewardBadge(
      id = "b4",
      title = "بطل الرياضيات الخارق",
      description = "إتقان قمة أبطال الرياضيات بـ 3 نجوم",
      isUnlocked = (gameState.levelStars[3] ?: 0) >= 3,
      iconType = 0
    ),
    RewardBadge(
      id = "b5",
      title = "جامع الكنوز الذهبية",
      description = "جمع 15 عملة ذهبية من الإجابات الصحيحة",
      isUnlocked = gameState.totalCoins >= 15,
      iconType = 4
    ),
    RewardBadge(
      id = "b6",
      title = "صديق الروبوتات",
      description = "إجابة 10 مسائل حسابية بشكل متقن",
      isUnlocked = gameState.correctCount >= 10,
      iconType = 5
    )
  )

  val unlockedCount = badges.count { it.isUnlocked }

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Trophy Cabinet Header Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(8.dp, RoundedCornerShape(32.dp), spotColor = Color(0x3038BDF8))
        .clip(RoundedCornerShape(32.dp))
        .background(Color.White)
        .border(2.dp, CardBorder, RoundedCornerShape(32.dp))
        .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = "خزانة الجوائز والأوسمة",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
            color = TextDark
          )

          Text(
            text = "اجمع الأوسمة وحقق إنجازاتك في كل تمرين",
            fontFamily = CairoFontFamily,
            fontSize = 13.sp,
            color = TextMuted
          )

          // Unlocked counter pill
          Box(
            modifier = Modifier
              .padding(top = 4.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(BrandSkyBlueBg)
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = "الأوسمة المفتوحة: $unlockedCount من 6",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = BrandSkyBlue
            )
          }
        }

        IllustratedTrophy(sizeDp = 64.dp)
      }
    }

    // 2. Badges Grid / Cards
    badges.forEach { badge ->
      BadgeItemCard(badge = badge)
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
private fun BadgeItemCard(badge: RewardBadge) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = if (badge.isUnlocked) Color(0x40FBBF24) else Color(0x10000000))
      .clip(RoundedCornerShape(24.dp))
      .background(Color.White)
      .border(
        width = if (badge.isUnlocked) 2.dp else 1.5.dp,
        color = if (badge.isUnlocked) Color(0xFFFDE68A) else CardBorder,
        shape = RoundedCornerShape(24.dp)
      )
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Illustrated Badge Icon Disc
      Box(
        modifier = Modifier
          .size(58.dp)
          .shadow(if (badge.isUnlocked) 4.dp else 1.dp, CircleShape)
          .clip(CircleShape)
          .background(if (badge.isUnlocked) Color(0xFFFFFBEB) else Color(0xFFF1F5F9))
          .border(
            2.dp,
            if (badge.isUnlocked) Color(0xFFFBBF24) else CardBorder,
            CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        if (!badge.isUnlocked) {
          IllustratedLock(sizeDp = 24.dp)
        } else {
          when (badge.iconType) {
            0 -> IllustratedTrophy(sizeDp = 34.dp)
            1 -> IllustratedStar(sizeDp = 30.dp, isFilled = true)
            2 -> IllustratedGem(sizeDp = 30.dp)
            3 -> CuteRocket(sizeDp = 32.dp)
            4 -> IllustratedCoin(sizeDp = 30.dp)
            else -> IllustratedRobot(sizeDp = 32.dp)
          }
        }
      }

      // Details
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalAlignment = Alignment.Start
      ) {
        Text(
          text = badge.title,
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = if (badge.isUnlocked) TextDark else TextMuted
        )

        Text(
          text = badge.description,
          fontFamily = CairoFontFamily,
          fontSize = 12.sp,
          color = TextMuted
        )
      }

      // Status Badge
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(if (badge.isUnlocked) Color(0xFFD1FAE5) else Color(0xFFF1F5F9))
          .padding(horizontal = 10.dp, vertical = 4.dp)
      ) {
        Text(
          text = if (badge.isUnlocked) "مكتمل" else "مغلق",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          color = if (badge.isUnlocked) BrandEmerald else TextMuted
        )
      }
    }
  }
}
