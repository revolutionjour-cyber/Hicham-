package com.example.ui.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KidGameState
import com.example.ui.components.CartoonAstronautMascot
import com.example.ui.components.CartoonTopBar
import com.example.ui.components.CuteRocket
import com.example.ui.components.LevelCardButton
import com.example.ui.components.MascotMood
import com.example.ui.components.PlayfulCosmicBackground

@Composable
fun HomeScreen(
  gameState: KidGameState,
  isSoundEnabled: Boolean,
  onToggleSound: () -> Unit,
  onSelectLevel: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize()) {
    // Dynamic animated cosmic background
    PlayfulCosmicBackground()

    Scaffold(
      containerColor = Color.Transparent,
      topBar = {
        CartoonTopBar(
          totalStars = gameState.totalStars,
          totalCoins = gameState.totalCoins,
          isSoundEnabled = isSoundEnabled,
          onToggleSound = onToggleSound,
          onBackClick = null
        )
      }
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Welcoming Cartoon Mascot & Animated Rocket
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          CuteRocket(sizeDp = 52.dp)
          CartoonAstronautMascot(
            sizeDp = 110.dp,
            mood = MascotMood.IDLE
          )
          Text(
            text = "🪐",
            fontSize = 42.sp
          )
        }

        // Minimal title, mostly visual
        Text(
          text = "🚀 اختر المستوى",
          fontSize = 24.sp,
          fontWeight = FontWeight.Black,
          color = Color(0xFFFDE047)
        )

        // 3 Giant Level Buttons in the center: 1, 2, 3
        LevelCardButton(
          level = 1,
          isUnlocked = gameState.unlockedLevels.contains(1),
          earnedStars = gameState.levelStars[1] ?: 0,
          onClick = { onSelectLevel(1) }
        )

        LevelCardButton(
          level = 2,
          isUnlocked = gameState.unlockedLevels.contains(2),
          earnedStars = gameState.levelStars[2] ?: 0,
          onClick = { onSelectLevel(2) }
        )

        LevelCardButton(
          level = 3,
          isUnlocked = gameState.unlockedLevels.contains(3),
          earnedStars = gameState.levelStars[3] ?: 0,
          onClick = { onSelectLevel(3) }
        )

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
