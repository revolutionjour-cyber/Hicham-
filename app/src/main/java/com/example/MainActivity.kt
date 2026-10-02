package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.KidMathViewModel
import com.example.ui.screens.ChalkboardPlayScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: KidMathViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme(dynamicColor = false) {
        KidMathApp(viewModel = viewModel)
      }
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.soundManager.setAppActive(true)
  }

  override fun onPause() {
    super.onPause()
    viewModel.soundManager.setAppActive(false)
  }
}

@Composable
fun KidMathApp(viewModel: KidMathViewModel) {
  val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
  val challenge by viewModel.currentChallenge.collectAsStateWithLifecycle()
  val selectedOp by viewModel.selectedOp.collectAsStateWithLifecycle()
  val selectedDifficulty by viewModel.selectedDifficulty.collectAsStateWithLifecycle()
  val adaptiveTier by viewModel.adaptiveTier.collectAsStateWithLifecycle()
  val totalStars by viewModel.directStars.collectAsStateWithLifecycle()
  val comboStreak by viewModel.comboStreak.collectAsStateWithLifecycle()
  val buttonStates by viewModel.directButtonStates.collectAsStateWithLifecycle()
  val correctCheer by viewModel.correctCheer.collectAsStateWithLifecycle()
  val confettiTrigger by viewModel.confettiTrigger.collectAsStateWithLifecycle()
  val currentSlotAnswer by viewModel.currentSlotAnswer.collectAsStateWithLifecycle()
  val isSlotAnswerWrong by viewModel.isSlotAnswerWrong.collectAsStateWithLifecycle()
  val isSoundEnabled by viewModel.soundManager.isSoundEnabled.collectAsStateWithLifecycle()

  // Dynamic layout direction based on chosen language (RTL for Moroccan Arabic, LTR for French & English)
  CompositionLocalProvider(
    LocalLayoutDirection provides if (currentLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
  ) {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) {
      ChalkboardPlayScreen(
        challenge = challenge,
        selectedOp = selectedOp,
        selectedDifficulty = selectedDifficulty,
        adaptiveTier = adaptiveTier,
        totalStars = totalStars,
        comboStreak = comboStreak,
        buttonStates = buttonStates,
        currentSlotAnswer = currentSlotAnswer,
        isSlotAnswerWrong = isSlotAnswerWrong,
        correctCheer = correctCheer,
        confettiTrigger = confettiTrigger,
        isSoundEnabled = isSoundEnabled,
        currentLanguage = currentLanguage,
        onSelectLanguage = { lang -> viewModel.setLanguage(lang) },
        onToggleSound = { viewModel.toggleSound() },
        onSelectOp = { op -> viewModel.onSelectOp(op) },
        onSelectDifficulty = { diff -> viewModel.onSelectDifficulty(diff) },
        onAnswerDropped = { answer -> viewModel.onDirectAnswerSelected(answer) }
      )
    }
  }
}
