package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.KidGameState
import com.example.model.MonsterMathChallenge
import com.example.ui.components.AnswerButtonState
import com.example.ui.components.CartoonTopBar
import com.example.ui.components.ClayFoodItemIcon
import com.example.ui.components.ClayMonsterBowl
import com.example.ui.components.ClayMonsterCharacter
import com.example.ui.components.ConfettiParticleExplosion
import com.example.ui.components.IllustratedCross
import com.example.ui.components.IllustratedStar
import com.example.ui.components.KidAnswerButton
import com.example.ui.components.MonsterExpression
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandRose
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.BrandSkyBlueBg
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.TextDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MonsterKitchenScreen(
  challenge: MonsterMathChallenge,
  currentIndex: Int,
  totalChallenges: Int,
  currentFedCount: Int,
  monsterMood: MonsterExpression,
  buttonStates: Map<Int, AnswerButtonState>,
  comboStreak: Int,
  correctCheer: String?,
  confettiTrigger: Int,
  gameState: KidGameState,
  isSoundEnabled: Boolean,
  onToggleSound: () -> Unit,
  onFeedItem: () -> Unit,
  onChooseAnswer: (Int) -> Unit,
  onBackToHome: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBackToHome() }

  val cheerScale = remember { Animatable(0f) }
  LaunchedEffect(correctCheer) {
    if (correctCheer != null) {
      cheerScale.snapTo(0.3f)
      cheerScale.animateTo(1.2f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
      cheerScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFFE0F2FE), // Pastel Sky
            Color(0xFFF0FDF4), // Mint Cream
            Color(0xFFFFFBEB)  // Soft Buttercup
          )
        )
      )
  ) {
    ConfettiParticleExplosion(triggerKey = confettiTrigger)

    Scaffold(
      containerColor = Color.Transparent,
      topBar = {
        CartoonTopBar(
          totalStars = gameState.totalStars,
          totalCoins = gameState.totalCoins,
          isSoundEnabled = isSoundEnabled,
          onToggleSound = onToggleSound,
          onBackClick = onBackToHome
        )
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        contentAlignment = Alignment.TopCenter
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 600.dp)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .navigationBarsPadding(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // 1. Level & Progress Pill
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color(0x2038BDF8))
              .clip(RoundedCornerShape(20.dp))
              .background(Color.White)
              .border(1.5.dp, CardBorder, RoundedCornerShape(20.dp))
              .padding(horizontal = 16.dp, vertical = 8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "وجبة ${currentIndex + 1} من $totalChallenges",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = BrandSkyBlue
              )

              if (comboStreak >= 2) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  IllustratedStar(sizeDp = 18.dp, isFilled = true)
                  Text(
                    text = "سلسلة ذكاء x$comboStreak!",
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFFD97706)
                  )
                }
              }
            }
          }

          // 2. The Hungry Cute Monster "بوبو"
          Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            ClayMonsterCharacter(
              expression = monsterMood,
              sizeDp = 135.dp
            )
          }

          // 3. Cheerful Story Goal Bubble
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = Color(0x20000000))
              .clip(RoundedCornerShape(22.dp))
              .background(Color.White)
              .border(2.dp, BrandSkyBlue.copy(alpha = 0.4f), RoundedCornerShape(22.dp))
              .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = challenge.storyPromptAr,
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = TextDark
            )
          }

          // 4. Positive Cheer Feedback
          AnimatedVisibility(
            visible = correctCheer != null,
            enter = fadeIn() + scaleIn()
          ) {
            Box(
              modifier = Modifier
                .scale(cheerScale.value)
                .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = BrandEmerald)
                .clip(RoundedCornerShape(20.dp))
                .background(BrandEmerald)
                .border(2.dp, Color.White, RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
              Text(
                text = correctCheer ?: "",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = Color.White
              )
            }
          }

          // 5. The Clay Plate with Real Food Items
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .shadow(8.dp, RoundedCornerShape(32.dp), spotColor = Color(0x2538BDF8))
              .clip(RoundedCornerShape(32.dp))
              .background(Color.White)
              .border(2.dp, CardBorder, RoundedCornerShape(32.dp))
              .padding(horizontal = 14.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Food Plate
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(24.dp))
                  .background(Color(0xFFF0FDF4))
                  .border(1.5.dp, Color(0xFFDCFCE7), RoundedCornerShape(24.dp))
                  .padding(12.dp),
                contentAlignment = Alignment.Center
              ) {
                FlowRow(
                  horizontalArrangement = Arrangement.Center,
                  verticalArrangement = Arrangement.Center,
                  maxItemsInEachRow = 5
                ) {
                  // Existing / Current food items in plate
                  val displayedCount = if (challenge.isSubtraction) {
                    challenge.initialInBowl
                  } else {
                    challenge.initialInBowl + currentFedCount
                  }

                  for (i in 0 until displayedCount) {
                    val isSubtractedCross = challenge.isSubtraction && (i >= challenge.targetTotal)
                    InteractivePlateFoodCell(
                      foodType = challenge.foodType,
                      isCrossed = isSubtractedCross,
                      onTap = onFeedItem
                    )
                  }

                  // Translucent ghost slots for what is still needed!
                  if (!challenge.isSubtraction) {
                    val remainingNeeded = (challenge.targetTotal - displayedCount).coerceAtLeast(0)
                    for (j in 0 until remainingNeeded) {
                      GhostFoodSlot(foodType = challenge.foodType)
                    }
                  }
                }
              }

              // Visual Math Formula: Left to Right
              CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                  Text(
                    text = "${challenge.initialInBowl}",
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = BrandSkyBlue
                  )
                  Text(
                    text = if (challenge.isSubtraction) "-" else "+",
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = TextDark
                  )

                  // Slot target
                  Box(
                    modifier = Modifier
                      .size(42.dp)
                      .shadow(4.dp, RoundedCornerShape(14.dp), spotColor = BrandRose)
                      .clip(RoundedCornerShape(14.dp))
                      .background(BrandRose)
                      .border(2.dp, Color.White, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = if (currentFedCount > 0) "$currentFedCount" else "؟",
                      fontFamily = FredokaFontFamily,
                      fontWeight = FontWeight.Bold,
                      fontSize = 24.sp,
                      color = Color.White
                    )
                  }

                  Text(
                    text = "=",
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = TextDark
                  )
                  Text(
                    text = "${challenge.targetTotal}",
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = BrandEmerald
                  )
                }
              }
            }
          }

          // 6. Direct Tactile Answer Selection (Buttons to feed Bobo the missing number)
          Text(
            text = "اختر كم حبة نطعمه لتكتمل الوجبة:",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF64748B)
          )

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            challenge.options.forEachIndexed { index, optionVal ->
              val buttonState = buttonStates[optionVal] ?: AnswerButtonState.DEFAULT
              KidAnswerButton(
                value = optionVal,
                index = index,
                state = buttonState,
                onClick = { onChooseAnswer(optionVal) },
                modifier = Modifier.weight(1f)
              )
            }
          }

          Spacer(modifier = Modifier.height(20.dp))
        }
      }
    }
  }
}

@Composable
private fun InteractivePlateFoodCell(
  foodType: com.example.ui.components.ClayFoodType,
  isCrossed: Boolean,
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
            scale.animateTo(1.3f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
          }
        }
      ),
    contentAlignment = Alignment.Center
  ) {
    ClayFoodItemIcon(
      foodType = foodType,
      sizeDp = 44.dp
    )

    if (isCrossed) {
      IllustratedCross(
        sizeDp = 26.dp,
        modifier = Modifier.align(Alignment.Center)
      )
    }
  }
}

@Composable
private fun GhostFoodSlot(
  foodType: com.example.ui.components.ClayFoodType
) {
  val infiniteTransition = rememberInfiniteTransition(label = "ghost_pulse")
  val alpha by infiniteTransition.animateFloat(
    initialValue = 0.25f,
    targetValue = 0.55f,
    animationSpec = infiniteRepeatable(
      animation = tween(900),
      repeatMode = RepeatMode.Reverse
    ),
    label = "ghost_alpha"
  )

  Box(
    modifier = Modifier
      .padding(4.dp)
      .size(44.dp)
      .clip(CircleShape)
      .background(Color(0xFFCBD5E1).copy(alpha = alpha))
      .border(1.5.dp, Color(0xFF94A3B8).copy(alpha = alpha), CircleShape),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "؟",
      fontFamily = FredokaFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 20.sp,
      color = Color.White
    )
  }
}
