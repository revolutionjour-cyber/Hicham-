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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KidGameState
import com.example.model.RoyalSanctuaryChallenge
import com.example.ui.components.AnswerButtonState
import com.example.ui.components.CartoonTopBar
import com.example.ui.components.ConfettiParticleExplosion
import com.example.ui.components.IllustratedStar
import com.example.ui.components.KidAnswerButton
import com.example.ui.components.LuxuryGemIcon
import com.example.ui.components.RoyalCrownArtifact
import com.example.ui.components.RoyalGoldenBalance
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.FredokaFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoyalSanctuaryScreen(
  challenge: RoyalSanctuaryChallenge,
  currentIndex: Int,
  totalChallenges: Int,
  currentPlacedGems: Int,
  isBalanced: Boolean,
  buttonStates: Map<Int, AnswerButtonState>,
  comboStreak: Int,
  correctCheer: String?,
  confettiTrigger: Int,
  gameState: KidGameState,
  isSoundEnabled: Boolean,
  onToggleSound: () -> Unit,
  onAddGemToPan: () -> Unit,
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

  // Deep Luxury Palace Background (Midnight Sapphire & Gold Starlight)
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFF0F172A), // Midnight Slate
            Color(0xFF1E293B), // Royal Indigo
            Color(0xFF0A0F1D)  // Deep Velvet Obsidian
          )
        )
      )
  ) {
    // Subtle luxury ambient golden bokeh
    LuxuryPalaceAmbientLights()

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
          // 1. Royal Chamber Progress Pill (Frosted Glass & Gold Rim)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = Color(0x50FBBF24))
              .clip(RoundedCornerShape(22.dp))
              .background(Color(0xFF1E293B).copy(alpha = 0.85f))
              .border(1.5.dp, Color(0xFFFBBF24).copy(alpha = 0.5f), RoundedCornerShape(22.dp))
              .padding(horizontal = 18.dp, vertical = 8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "اللغز الملكي ${currentIndex + 1} من $totalChallenges",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFFFDE68A)
              )

              if (comboStreak >= 2) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  IllustratedStar(sizeDp = 18.dp, isFilled = true)
                  Text(
                    text = "سلسلة الحكمة x$comboStreak",
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFFFBBF24)
                  )
                }
              }
            }
          }

          // 2. The Majestic Floating Golden Balance
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .shadow(12.dp, RoundedCornerShape(32.dp), spotColor = Color(0x35FBBF24))
              .clip(RoundedCornerShape(32.dp))
              .background(
                Brush.radialGradient(
                  colors = listOf(Color(0x3038BDF8), Color(0x101E293B), Color(0x00000000)),
                  radius = 400f
                )
              )
              .border(1.5.dp, Color(0x35FDE047), RoundedCornerShape(32.dp))
              .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
          ) {
            val totalRightWeight = challenge.initialRightWeight + currentPlacedGems
            RoyalGoldenBalance(
              leftCount = challenge.leftTargetWeight,
              rightCount = totalRightWeight,
              leftGem = challenge.gemType,
              rightGem = challenge.gemType,
              isBalanced = isBalanced,
              onLeftPanClick = {},
              onRightPanClick = onAddGemToPan
            )
          }

          // 3. Poetic Palace Dialogue Goal
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .shadow(8.dp, RoundedCornerShape(22.dp), spotColor = Color(0x40000000))
              .clip(RoundedCornerShape(22.dp))
              .background(Color(0xFF0F172A).copy(alpha = 0.90f))
              .border(1.5.dp, Color(0xFFD97706).copy(alpha = 0.6f), RoundedCornerShape(22.dp))
              .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = challenge.storyDialogueAr,
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = Color(0xFFF8FAFC)
            )
          }

          // 4. Triumph Cheer Message when Balanced
          AnimatedVisibility(
            visible = correctCheer != null,
            enter = fadeIn() + scaleIn()
          ) {
            Box(
              modifier = Modifier
                .scale(cheerScale.value)
                .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = Color(0xFFFBBF24))
                .clip(RoundedCornerShape(22.dp))
                .background(
                  Brush.horizontalGradient(
                    listOf(Color(0xFF059669), Color(0xFF10B981))
                  )
                )
                .border(2.dp, Color(0xFFFDE68A), RoundedCornerShape(22.dp))
                .padding(horizontal = 26.dp, vertical = 10.dp)
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

          // 5. The Velvet Gem Drawer (Touch to Place Gem directly onto Pan)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .shadow(10.dp, RoundedCornerShape(30.dp), spotColor = Color(0x50000000))
              .clip(RoundedCornerShape(30.dp))
              .background(Color(0xFF1E293B).copy(alpha = 0.85f))
              .border(2.dp, Color(0x50FDE047), RoundedCornerShape(30.dp))
              .padding(14.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Text(
                text = "الميزان الذهبي: انقر على الجوهرة لوضعها في الكفة",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF94A3B8)
              )

              // Interactive Gemstone Touch Drawer
              Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Interactive Jewel
                Box(
                  modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF334155))
                    .border(2.dp, Color(0xFFFBBF24), CircleShape)
                    .clickable(
                      interactionSource = remember { MutableInteractionSource() },
                      indication = null,
                      onClick = onAddGemToPan
                    )
                    .padding(8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  LuxuryGemIcon(gemType = challenge.gemType, sizeDp = 48.dp)
                }

                // Balance equation indicator: LTR
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                  ) {
                    Text(
                      text = "${challenge.leftTargetWeight}",
                      fontFamily = FredokaFontFamily,
                      fontWeight = FontWeight.Bold,
                      fontSize = 32.sp,
                      color = Color(0xFFFDE68A)
                    )
                    Text(
                      text = "=",
                      fontFamily = FredokaFontFamily,
                      fontWeight = FontWeight.Bold,
                      fontSize = 28.sp,
                      color = Color.White
                    )
                    Text(
                      text = "${challenge.initialRightWeight}",
                      fontFamily = FredokaFontFamily,
                      fontWeight = FontWeight.Bold,
                      fontSize = 32.sp,
                      color = Color(0xFF38BDF8)
                    )
                    Text(
                      text = "+",
                      fontFamily = FredokaFontFamily,
                      fontWeight = FontWeight.Bold,
                      fontSize = 26.sp,
                      color = Color.White
                    )

                    // Mystery slot
                    Box(
                      modifier = Modifier
                        .size(44.dp)
                        .shadow(4.dp, RoundedCornerShape(14.dp), spotColor = Color(0xFFF43F5E))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFE11D48))
                        .border(1.5.dp, Color.White, RoundedCornerShape(14.dp)),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = if (currentPlacedGems > 0) "$currentPlacedGems" else "؟",
                        fontFamily = FredokaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                      )
                    }
                  }
                }
              }
            }
          }

          // 6. Direct Royal Number Tokens (Choose how many gems to place)
          Text(
            text = "أو اختر عدد الجواهر مباشرة لوزن الكفة:",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFFCBD5E1)
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
private fun LuxuryPalaceAmbientLights() {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height

    // Soft warm golden bokeh orbs
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0x20FDE047), Color(0x00FDE047)),
        center = Offset(w * 0.15f, h * 0.25f),
        radius = w * 0.40f
      ),
      radius = w * 0.40f,
      center = Offset(w * 0.15f, h * 0.25f)
    )

    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(Color(0x1838BDF8), Color(0x0038BDF8)),
        center = Offset(w * 0.85f, h * 0.70f),
        radius = w * 0.45f
      ),
      radius = w * 0.45f,
      center = Offset(w * 0.85f, h * 0.70f)
    )

    // Soft ambient starlight specs
    val starCoords = listOf(
      Offset(w * 0.10f, h * 0.10f),
      Offset(w * 0.85f, h * 0.15f),
      Offset(w * 0.90f, h * 0.40f),
      Offset(w * 0.08f, h * 0.55f),
      Offset(w * 0.82f, h * 0.85f)
    )
    starCoords.forEach { pt ->
      drawCircle(Color(0x40FDE68A), radius = 2.5f, center = pt)
    }
  }
}
