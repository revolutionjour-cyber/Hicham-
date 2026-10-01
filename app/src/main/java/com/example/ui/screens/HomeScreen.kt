package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KidGameState
import com.example.ui.MainNavTab
import com.example.ui.components.CartoonTopBar
import com.example.ui.components.PlayfulCosmicBackground
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.BrandSkyBlueBg
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.CardBorder
import com.example.ui.theme.TextMuted

@Composable
fun HomeScreen(
  gameState: KidGameState,
  activeTab: MainNavTab,
  onTabSelected: (MainNavTab) -> Unit,
  isSoundEnabled: Boolean,
  onToggleSound: () -> Unit,
  onSelectLevel: (Int) -> Unit,
  onUpdateProfile: (String, Int) -> Unit,
  onResetProgress: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize()) {
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
      },
      bottomBar = {
        ModernKidsBottomBar(
          activeTab = activeTab,
          onTabSelected = onTabSelected
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
            .padding(horizontal = 16.dp, vertical = 6.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          AnimatedContent(
            targetState = activeTab,
            transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(200)) },
            label = "tab_content"
          ) { tab ->
            when (tab) {
              MainNavTab.MAP -> {
                MonsterGardenHome(
                  gameState = gameState,
                  onStartLevel = onSelectLevel
                )
              }
              MainNavTab.REWARDS -> {
                RewardsCabinetTab(gameState = gameState)
              }
              MainNavTab.PROGRESS -> {
                ProgressStatsTab(gameState = gameState)
              }
              MainNavTab.SETTINGS -> {
                ProfileSettingsTab(
                  gameState = gameState,
                  isSoundEnabled = isSoundEnabled,
                  onToggleSound = onToggleSound,
                  onUpdateProfile = onUpdateProfile,
                  onResetProgress = onResetProgress
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}

/**
 * Modern floating pill navigation bar for kids with clear vector icons (ZERO Unicode emojis).
 */
@Composable
private fun ModernKidsBottomBar(
  activeTab: MainNavTab,
  onTabSelected: (MainNavTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .padding(horizontal = 20.dp, vertical = 10.dp),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .shadow(12.dp, RoundedCornerShape(28.dp), spotColor = Color(0x3038BDF8))
        .clip(RoundedCornerShape(28.dp))
        .background(Color.White)
        .border(2.dp, CardBorder, RoundedCornerShape(28.dp))
        .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        NavTabItem(
          title = "الخريطة",
          icon = Icons.Default.Map,
          isSelected = activeTab == MainNavTab.MAP,
          onClick = { onTabSelected(MainNavTab.MAP) },
          testTag = "nav_tab_map"
        )

        NavTabItem(
          title = "الجوائز",
          icon = Icons.Default.EmojiEvents,
          isSelected = activeTab == MainNavTab.REWARDS,
          onClick = { onTabSelected(MainNavTab.REWARDS) },
          testTag = "nav_tab_rewards"
        )

        NavTabItem(
          title = "التقدم",
          icon = Icons.Default.BarChart,
          isSelected = activeTab == MainNavTab.PROGRESS,
          onClick = { onTabSelected(MainNavTab.PROGRESS) },
          testTag = "nav_tab_progress"
        )

        NavTabItem(
          title = "الإعدادات",
          icon = Icons.Default.Settings,
          isSelected = activeTab == MainNavTab.SETTINGS,
          onClick = { onTabSelected(MainNavTab.SETTINGS) },
          testTag = "nav_tab_settings"
        )
      }
    }
  }
}

@Composable
private fun NavTabItem(
  title: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  testTag: String
) {
  val interactionSource = remember { MutableInteractionSource() }

  Column(
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .background(if (isSelected) BrandSkyBlueBg else Color.Transparent)
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 12.dp, vertical = 6.dp)
      .testTag(testTag),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(2.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = if (isSelected) BrandSkyBlue else TextMuted,
      modifier = Modifier.size(24.dp)
    )

    Text(
      text = title,
      fontFamily = CairoFontFamily,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      fontSize = 11.sp,
      color = if (isSelected) BrandSkyBlue else TextMuted
    )
  }
}
