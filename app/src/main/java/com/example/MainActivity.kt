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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.KidMathViewModel
import com.example.ui.ScreenState
import com.example.ui.components.LevelCompleteView
import com.example.ui.screens.GamePlayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: KidMathViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme(dynamicColor = false) {
        // Enforce RTL direction for kid Arabic interface
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
          Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF13092D)
          ) {
            KidMathApp(viewModel = viewModel)
          }
        }
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
  val screenState by viewModel.screenState.collectAsStateWithLifecycle()
  val playState by viewModel.playState.collectAsStateWithLifecycle()
  val gameState by viewModel.gameState.collectAsStateWithLifecycle()
  val isSoundEnabled by viewModel.soundManager.isSoundEnabled.collectAsStateWithLifecycle()

  when (screenState) {
    ScreenState.HOME -> {
      HomeScreen(
        gameState = gameState,
        isSoundEnabled = isSoundEnabled,
        onToggleSound = { viewModel.toggleSound() },
        onSelectLevel = { level -> viewModel.startLevel(level) }
      )
    }
    ScreenState.PLAYING -> {
      GamePlayScreen(
        playState = playState,
        gameState = gameState,
        isSoundEnabled = isSoundEnabled,
        onToggleSound = { viewModel.toggleSound() },
        onAnswerSelected = { answer -> viewModel.onAnswerSelected(answer) },
        onItemTapped = { viewModel.onItemTapped() },
        onBackToHome = { viewModel.navigateToHome() }
      )
    }
    ScreenState.LEVEL_COMPLETE -> {
      val nextAvailable = if (playState.currentLevel < 3) {
        { viewModel.nextLevel() }
      } else null

      LevelCompleteView(
        level = playState.currentLevel,
        starsEarned = when {
          playState.levelStarsEarned >= 5 -> 3
          playState.levelStarsEarned >= 3 -> 2
          else -> 1
        },
        totalStars = gameState.totalStars,
        totalCoins = gameState.totalCoins,
        onReplay = { viewModel.replayCurrentLevel() },
        onNextLevel = nextAvailable,
        onHome = { viewModel.navigateToHome() }
      )
    }
  }
}
