package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class KidGameState(
  val totalStars: Int = 0,
  val totalCoins: Int = 0,
  val unlockedLevels: Set<Int> = setOf(1),
  val levelStars: Map<Int, Int> = mapOf(1 to 0, 2 to 0, 3 to 0)
)

class KidGameRepository(context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("kid_math_game_prefs", Context.MODE_PRIVATE)

  private val _gameState = MutableStateFlow(loadGameState())
  val gameState: StateFlow<KidGameState> = _gameState.asStateFlow()

  private fun loadGameState(): KidGameState {
    val stars = prefs.getInt("total_stars", 0)
    val coins = prefs.getInt("total_coins", 0)
    val unlocked = prefs.getStringSet("unlocked_levels", setOf("1"))
      ?.mapNotNull { it.toIntOrNull() }?.toSet() ?: setOf(1)
    val lvl1 = prefs.getInt("stars_level_1", 0)
    val lvl2 = prefs.getInt("stars_level_2", 0)
    val lvl3 = prefs.getInt("stars_level_3", 0)

    return KidGameState(
      totalStars = stars,
      totalCoins = coins,
      unlockedLevels = if (unlocked.isEmpty()) setOf(1) else unlocked,
      levelStars = mapOf(1 to lvl1, 2 to lvl2, 3 to lvl3)
    )
  }

  fun recordAnswerSuccess(earnedStars: Int = 1, earnedCoins: Int = 2) {
    val current = _gameState.value
    val newStars = current.totalStars + earnedStars
    val newCoins = current.totalCoins + earnedCoins

    prefs.edit()
      .putInt("total_stars", newStars)
      .putInt("total_coins", newCoins)
      .apply()

    _gameState.value = current.copy(
      totalStars = newStars,
      totalCoins = newCoins
    )
  }

  fun completeLevel(levelId: Int, starsEarned: Int) {
    val current = _gameState.value
    val updatedStarsMap = current.levelStars.toMutableMap()
    val prevStars = updatedStarsMap[levelId] ?: 0
    if (starsEarned > prevStars) {
      updatedStarsMap[levelId] = starsEarned
      prefs.edit().putInt("stars_level_$levelId", starsEarned).apply()
    }

    // Unlock next level
    val updatedUnlocked = current.unlockedLevels.toMutableSet()
    val nextLevel = levelId + 1
    if (nextLevel <= 3) {
      updatedUnlocked.add(nextLevel)
      prefs.edit().putStringSet("unlocked_levels", updatedUnlocked.map { it.toString() }.toSet()).apply()
    }

    _gameState.value = current.copy(
      unlockedLevels = updatedUnlocked,
      levelStars = updatedStarsMap
    )
  }
}
