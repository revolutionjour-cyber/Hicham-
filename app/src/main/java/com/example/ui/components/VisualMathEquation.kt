package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MathOperator
import com.example.model.MathQuestion
import com.example.model.TangibleItemType
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandRose
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.BrandSkyBlueBg
import com.example.ui.theme.CardBorder
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisualMathEquation(
  question: MathQuestion,
  showHint: Boolean,
  onItemTapped: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. MOTIVATING STORY CHALLENGE BANNER
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color(0x2038BDF8))
        .clip(RoundedCornerShape(20.dp))
        .background(BrandSkyBlueBg)
        .border(1.5.dp, BrandSkyBlue.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
        .padding(horizontal = 16.dp, vertical = 10.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = question.promptAr,
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = BrandSkyBlue
      )
    }

    // 2. TACTILE COUNTING TRAY (Pedagogically accurate for kids)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(8.dp, RoundedCornerShape(32.dp), spotColor = Color(0x3038BDF8))
        .clip(RoundedCornerShape(32.dp))
        .background(Color.White)
        .border(2.dp, CardBorder, RoundedCornerShape(32.dp))
        .padding(horizontal = 14.dp, vertical = 18.dp),
      contentAlignment = Alignment.Center
    ) {
      CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        if (question.operator == MathOperator.PLUS) {
          // --- ADDITION: Group A + Group B ---
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            CountingTrayGroup(
              count = question.firstCount,
              itemType = question.item,
              startIndex = 1,
              showHint = showHint,
              onItemTapped = onItemTapped
            )

            // Operator Badge (+)
            Box(
              modifier = Modifier
                .padding(horizontal = 10.dp)
                .size(46.dp)
                .shadow(4.dp, CircleShape, spotColor = BrandAmber)
                .clip(CircleShape)
                .background(
                  Brush.verticalGradient(
                    listOf(BrandAmber, Color(0xFFD97706))
                  )
                )
                .border(2.dp, Color.White, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "+",
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.White
              )
            }

            CountingTrayGroup(
              count = question.secondCount,
              itemType = question.item,
              startIndex = question.firstCount + 1,
              showHint = showHint,
              onItemTapped = onItemTapped
            )
          }
        } else {
          // --- SUBTRACTION: Total items with subtracted items crossed out ---
          val totalItems = question.firstCount
          val subtractedCount = question.secondCount
          val remainingCount = totalItems - subtractedCount

          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF8FAFC))
                .border(1.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
              FlowRow(
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,
                maxItemsInEachRow = 5
              ) {
                // First: uncrossed remaining items
                for (i in 0 until remainingCount) {
                  TangibleItemCell(
                    itemType = question.item,
                    hintNumber = i + 1,
                    showHint = showHint,
                    isSubtracted = false,
                    onTap = onItemTapped
                  )
                }
                // Next: crossed-out subtracted items
                for (i in 0 until subtractedCount) {
                  TangibleItemCell(
                    itemType = question.item,
                    hintNumber = 0,
                    showHint = false,
                    isSubtracted = true,
                    onTap = onItemTapped
                  )
                }
              }
            }
          }
        }
      }
    }

    // 3. CRISP NUMERIC EQUATION (Always left-to-right math: 3 + 2 = [ ؟ ])
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
      Box(
        modifier = Modifier
          .shadow(6.dp, RoundedCornerShape(26.dp), spotColor = Color(0x25000000))
          .clip(RoundedCornerShape(26.dp))
          .background(Color.White)
          .border(2.dp, CardBorder, RoundedCornerShape(26.dp))
          .padding(horizontal = 24.dp, vertical = 8.dp)
          .testTag("numeric_equation"),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Text(
            text = "${question.firstCount}",
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 38.sp,
            color = BrandSkyBlue
          )
          Text(
            text = question.operator.symbol,
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = TextDark
          )
          Text(
            text = "${question.secondCount}",
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 38.sp,
            color = BrandSkyBlue
          )
          Text(
            text = "=",
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = TextDark
          )

          // Missing Answer Slot
          Box(
            modifier = Modifier
              .size(48.dp)
              .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = BrandRose)
              .clip(RoundedCornerShape(16.dp))
              .background(
                Brush.verticalGradient(
                  listOf(BrandRose, Color(0xFFE11D48))
                )
              )
              .border(2.dp, Color.White, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "؟",
              fontFamily = FredokaFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 30.sp,
              color = Color.White
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CountingTrayGroup(
  count: Int,
  itemType: TangibleItemType,
  startIndex: Int,
  showHint: Boolean,
  onItemTapped: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(24.dp))
      .background(Color(0xFFF8FAFC))
      .border(1.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp))
      .padding(horizontal = 8.dp, vertical = 6.dp)
  ) {
    FlowRow(
      horizontalArrangement = Arrangement.Center,
      verticalArrangement = Arrangement.Center,
      maxItemsInEachRow = 3
    ) {
      for (i in 0 until count) {
        val itemNumber = startIndex + i
        TangibleItemCell(
          itemType = itemType,
          hintNumber = itemNumber,
          showHint = showHint,
          isSubtracted = false,
          onTap = onItemTapped
        )
      }
    }
  }
}

@Composable
private fun TangibleItemCell(
  itemType: TangibleItemType,
  hintNumber: Int,
  showHint: Boolean,
  isSubtracted: Boolean,
  onTap: () -> Unit
) {
  val scale = remember { Animatable(1f) }
  val scope = rememberCoroutineScope()

  Box(
    modifier = Modifier
      .padding(4.dp)
      .scale(scale.value)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = {
          onTap()
          scope.launch {
            scale.animateTo(1.35f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
          }
        }
      ),
    contentAlignment = Alignment.Center
  ) {
    IllustratedItemIcon(
      itemType = itemType,
      sizeDp = 44.dp,
      modifier = Modifier.padding(bottom = if (showHint && hintNumber > 0) 10.dp else 0.dp)
    )

    if (isSubtracted) {
      IllustratedCross(
        sizeDp = 26.dp,
        modifier = Modifier.align(Alignment.Center)
      )
    }

    if (showHint && hintNumber > 0) {
      Box(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .size(22.dp)
          .shadow(3.dp, CircleShape)
          .clip(CircleShape)
          .background(BrandEmerald)
          .border(1.5.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$hintNumber",
          fontFamily = FredokaFontFamily,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }
    }
  }
}
