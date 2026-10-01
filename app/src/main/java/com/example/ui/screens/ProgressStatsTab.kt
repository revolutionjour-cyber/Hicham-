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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KidGameState
import com.example.ui.components.IllustratedCoin
import com.example.ui.components.IllustratedPlanetOrb
import com.example.ui.components.IllustratedStar
import com.example.ui.components.IllustratedTrophy
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.CardBorder
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMedium
import com.example.ui.theme.TextMuted

@Composable
fun ProgressStatsTab(
  gameState: KidGameState,
  modifier: Modifier = Modifier
) {
  val totalStars = gameState.levelStars.values.sum()
  val masteryPercent = ((totalStars.toFloat() / 9f) * 100).toInt().coerceIn(10, 100)

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Mastery Overview Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(8.dp, RoundedCornerShape(32.dp), spotColor = Color(0x3038BDF8))
        .clip(RoundedCornerShape(32.dp))
        .background(Color.White)
        .border(2.dp, CardBorder, RoundedCornerShape(32.dp))
        .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
              text = "مستوى الإتقان الحسابي",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Black,
              fontSize = 20.sp,
              color = TextDark
            )
            Text(
              text = "نسبة إنجازك لجميع التمارين",
              fontFamily = CairoFontFamily,
              fontSize = 13.sp,
              color = TextMuted
            )
          }

          Text(
            text = "$masteryPercent%",
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = BrandSkyBlue
          )
        }

        // Broad gradient progress bar
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(14.dp)
            .clip(CircleShape)
            .background(Color(0xFFF1F5F9))
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth(masteryPercent / 100f)
              .height(14.dp)
              .clip(CircleShape)
              .background(
                Brush.horizontalGradient(
                  listOf(Color(0xFF38BDF8), Color(0xFFFBBF24), Color(0xFF10B981))
                )
              )
          )
        }
      }
    }

    // 2. Metrics 2x2 Grid (Stars, Coins, Correct Answers, Streak)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Stars Card
      MetricCard(
        title = "النجوم المجمعة",
        value = "$totalStars / 9",
        icon = { IllustratedStar(sizeDp = 26.dp, isFilled = true) },
        color = BrandAmber,
        modifier = Modifier.weight(1f)
      )

      // Coins Card
      MetricCard(
        title = "العملات الذهبية",
        value = "${gameState.totalCoins}",
        icon = { IllustratedCoin(sizeDp = 26.dp) },
        color = BrandSkyBlue,
        modifier = Modifier.weight(1f)
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Correct answers
      MetricCard(
        title = "الإجابات الصحيحة",
        value = "${gameState.correctCount}",
        icon = { IllustratedTrophy(sizeDp = 26.dp) },
        color = BrandEmerald,
        modifier = Modifier.weight(1f)
      )

      // Streak Days
      MetricCard(
        title = "أيام التدريب المتتالية",
        value = "${gameState.streakDays} أيام",
        icon = {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(BrandEmerald),
            contentAlignment = Alignment.Center
          ) {
            IllustratedStar(sizeDp = 14.dp, isFilled = true)
          }
        },
        color = BrandEmerald,
        modifier = Modifier.weight(1f)
      )
    }

    // 3. Stage-by-Stage Breakdown
    Text(
      text = "تفاصيل الجزر والمراحل",
      fontFamily = CairoFontFamily,
      fontWeight = FontWeight.Black,
      fontSize = 18.sp,
      color = TextDark,
      modifier = Modifier
        .align(Alignment.Start)
        .padding(top = 4.dp)
    )

    StageBreakdownCard(
      level = 1,
      title = "جزيرة الأعداد الأولى",
      range = "1 - 5",
      stars = gameState.levelStars[1] ?: 0,
      isUnlocked = gameState.unlockedLevels.contains(1)
    )

    StageBreakdownCard(
      level = 2,
      title = "واحة الجمع الذكي",
      range = "1 - 10",
      stars = gameState.levelStars[2] ?: 0,
      isUnlocked = gameState.unlockedLevels.contains(2)
    )

    StageBreakdownCard(
      level = 3,
      title = "قمة أبطال الرياضيات",
      range = "1 - 20",
      stars = gameState.levelStars[3] ?: 0,
      isUnlocked = gameState.unlockedLevels.contains(3)
    )

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
private fun MetricCard(
  title: String,
  value: String,
  icon: @Composable () -> Unit,
  color: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .shadow(4.dp, RoundedCornerShape(22.dp), spotColor = color.copy(alpha = 0.3f))
      .clip(RoundedCornerShape(22.dp))
      .background(Color.White)
      .border(1.5.dp, CardBorder, RoundedCornerShape(22.dp))
      .padding(14.dp)
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(6.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        icon()
      }

      Text(
        text = value,
        fontFamily = FredokaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = TextDark
      )

      Text(
        text = title,
        fontFamily = CairoFontFamily,
        fontSize = 12.sp,
        color = TextMuted
      )
    }
  }
}

@Composable
private fun StageBreakdownCard(
  level: Int,
  title: String,
  range: String,
  stars: Int,
  isUnlocked: Boolean
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .shadow(3.dp, RoundedCornerShape(20.dp), spotColor = Color(0x10000000))
      .clip(RoundedCornerShape(20.dp))
      .background(Color.White)
      .border(1.5.dp, CardBorder, RoundedCornerShape(20.dp))
      .padding(14.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        IllustratedPlanetOrb(level = level, sizeDp = 40.dp)

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
          Text(
            text = title,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = if (isUnlocked) TextDark else TextMuted
          )
          Text(
            text = "الأعداد: $range",
            fontFamily = FredokaFontFamily,
            fontSize = 12.sp,
            color = TextMuted
          )
        }
      }

      Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        for (i in 1..3) {
          IllustratedStar(sizeDp = 18.dp, isFilled = isUnlocked && i <= stars)
        }
      }
    }
  }
}
