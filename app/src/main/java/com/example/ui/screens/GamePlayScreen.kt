package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KidGameState
import com.example.ui.PlayUiState
import com.example.ui.components.AnswerButtonState
import com.example.ui.components.CartoonAstronautMascot
import com.example.ui.components.CartoonTopBar
import com.example.ui.components.ConfettiParticleExplosion
import com.example.ui.components.CuteRocket
import com.example.ui.components.IllustratedPlanetOrb
import com.example.ui.components.IllustratedStar
import com.example.ui.components.KidAnswerButton
import com.example.ui.components.PlayfulCosmicBackground
import com.example.ui.components.RocketProgressBar
import com.example.ui.components.VisualMathEquation
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.FredokaFontFamily

@Composable
fun GamePlayScreen(
  playState: PlayUiState,
  gameState: KidGameState,
  isSoundEnabled: Boolean,
  onToggleSound: () -> Unit,
  onAnswerSelected: (Int) -> Unit,
  onItemTapped: () -> Unit,
  onBackToHome: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBackToHome() }

  val currentQuestion = playState.questions.getOrNull(playState.currentIndex)
  val cheerScale = remember { Animatable(0f) }

  LaunchedEffect(playState.correctCheer) {
    if (playState.correctCheer != null) {
      cheerScale.snapTo(0.3f)
      cheerScale.animateTo(1.2f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
      cheerScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }
  }

  Box(modifier = modifier.fillMaxSize()) {
    PlayfulCosmicBackground()

    ConfettiParticleExplosion(triggerKey = playState.confettiTrigger)

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
          // 1. Rocket Progress Checkpoint Bar
          RocketProgressBar(
            currentStep = playState.currentIndex,
            totalSteps = playState.questions.size.coerceAtLeast(1)
          )

          // 2. Playful Mascot, Companion Rocket, & Combo Badge
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            CuteRocket(sizeDp = 50.dp)

            CartoonAstronautMascot(
              sizeDp = 95.dp,
              mood = playState.mascotMood
            )

            // Dynamic Combo Streak Multiplier Badge (Encourages continuity & excitement)
            if (playState.comboStreak >= 2) {
              Box(
                modifier = Modifier
                  .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = BrandAmber)
                  .clip(RoundedCornerShape(16.dp))
                  .background(Color(0xFFFFFBEB))
                  .border(2.dp, BrandAmber, RoundedCornerShape(16.dp))
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  IllustratedStar(sizeDp = 18.dp, isFilled = true)
                  Text(
                    text = "سلسلة x${playState.comboStreak}",
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFFB45309)
                  )
                }
              }
            } else {
              IllustratedPlanetOrb(
                level = playState.currentLevel,
                sizeDp = 44.dp
              )
            }
          }

          // 3. Victory Feedback Banner
          AnimatedVisibility(
            visible = playState.correctCheer != null,
            enter = fadeIn() + scaleIn()
          ) {
            Box(
              modifier = Modifier
                .scale(cheerScale.value)
                .shadow(8.dp, RoundedCornerShape(22.dp), spotColor = BrandEmerald)
                .clip(RoundedCornerShape(22.dp))
                .background(BrandEmerald)
                .border(2.5.dp, Color.White, RoundedCornerShape(22.dp))
                .padding(horizontal = 26.dp, vertical = 8.dp)
            ) {
              Text(
                text = playState.correctCheer ?: "",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
                color = Color.White
              )
            }
          }

          if (currentQuestion != null) {
            // 4. Large Interactive Math Equation Card with Real Tangible Items
            VisualMathEquation(
              question = currentQuestion,
              showHint = playState.showHint,
              onItemTapped = onItemTapped
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 5. Large 3D Tactile Answer Buttons (Equally distributed)
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              currentQuestion.options.forEachIndexed { index, optionVal ->
                val buttonState = playState.buttonStates[optionVal] ?: AnswerButtonState.DEFAULT
                KidAnswerButton(
                  value = optionVal,
                  index = index,
                  state = buttonState,
                  onClick = { onAnswerSelected(optionVal) },
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}
