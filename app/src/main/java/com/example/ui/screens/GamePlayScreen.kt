package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import com.example.ui.components.KidAnswerButton
import com.example.ui.components.PlayfulCosmicBackground
import com.example.ui.components.RocketProgressBar
import com.example.ui.components.VisualMathEquation

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
      cheerScale.snapTo(0.2f)
      cheerScale.animateTo(1.2f, tween(160, easing = FastOutSlowInEasing))
      cheerScale.animateTo(1f, tween(140, easing = FastOutSlowInEasing))
    }
  }

  Box(modifier = modifier.fillMaxSize()) {
    // Dynamic animated cosmic background
    PlayfulCosmicBackground()

    // Confetti explosion on correct answer
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
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Rocket Checkpoints Progress Bar
        RocketProgressBar(
          currentStep = playState.currentIndex,
          totalSteps = playState.questions.size.coerceAtLeast(1)
        )

        // Mascot & Rocket interactive header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          CuteRocket(sizeDp = 50.dp)

          // Mascot with expressive mood
          CartoonAstronautMascot(
            sizeDp = 95.dp,
            mood = playState.mascotMood
          )

          // Level badge emoji
          Text(
            text = when (playState.currentLevel) {
              1 -> "🌕"
              2 -> "🪐"
              else -> "🌌"
            },
            fontSize = 36.sp
          )
        }

        // Brief cheerful victory popup banner
        AnimatedVisibility(
          visible = playState.correctCheer != null,
          enter = fadeIn() + scaleIn()
        ) {
          Box(
            modifier = Modifier
              .scale(cheerScale.value)
              .shadow(8.dp, RoundedCornerShape(20.dp))
              .clip(RoundedCornerShape(20.dp))
              .background(Color(0xFF22C55E))
              .border(2.5.dp, Color.White, RoundedCornerShape(20.dp))
              .padding(horizontal = 24.dp, vertical = 8.dp)
          ) {
            Text(
              text = playState.correctCheer ?: "",
              fontSize = 26.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )
          }
        }

        if (currentQuestion != null) {
          // Visual Math Equation Card (Items e.g. 🌟🌟 + 🌟🌟🌟 = ؟)
          VisualMathEquation(
            question = currentQuestion,
            showHint = playState.showHint,
            onItemTapped = onItemTapped
          )

          Spacer(modifier = Modifier.height(6.dp))

          // Big tactile answer buttons: 3 or 4 choices
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {
            currentQuestion.options.forEachIndexed { index, optionVal ->
              val buttonState = playState.buttonStates[optionVal] ?: AnswerButtonState.DEFAULT
              KidAnswerButton(
                value = optionVal,
                index = index,
                state = buttonState,
                onClick = { onAnswerSelected(optionVal) }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}
