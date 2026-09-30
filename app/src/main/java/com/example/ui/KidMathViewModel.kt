package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.KidSoundManager
import com.example.data.KidGameRepository
import com.example.model.MathQuestion
import com.example.ui.components.AnswerButtonState
import com.example.ui.components.MascotMood
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenState {
  HOME,
  PLAYING,
  LEVEL_COMPLETE
}

data class PlayUiState(
  val currentLevel: Int = 1,
  val questions: List<MathQuestion> = emptyList(),
  val currentIndex: Int = 0,
  val mascotMood: MascotMood = MascotMood.IDLE,
  val buttonStates: Map<Int, AnswerButtonState> = emptyMap(),
  val showHint: Boolean = false,
  val confettiTrigger: Int = 0,
  val levelStarsEarned: Int = 0,
  val correctCheer: String? = null,
  val isAdvancing: Boolean = false
)

class KidMathViewModel(application: Application) : AndroidViewModel(application) {
  val soundManager = KidSoundManager(application)
  private val repository = KidGameRepository(application)

  val gameState = repository.gameState.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    repository.gameState.value
  )

  private val _screenState = MutableStateFlow(ScreenState.HOME)
  val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

  private val _playState = MutableStateFlow(PlayUiState())
  val playState: StateFlow<PlayUiState> = _playState.asStateFlow()

  fun startLevel(level: Int) {
    soundManager.playPop()
    val questions = MathQuestion.generateQuestionsForLevel(level, count = 5)
    _playState.value = PlayUiState(
      currentLevel = level,
      questions = questions,
      currentIndex = 0,
      mascotMood = MascotMood.IDLE,
      buttonStates = emptyMap(),
      showHint = false,
      confettiTrigger = 0,
      levelStarsEarned = 0,
      correctCheer = null,
      isAdvancing = false
    )
    _screenState.value = ScreenState.PLAYING
  }

  fun onAnswerSelected(selectedAnswer: Int) {
    val state = _playState.value
    if (state.isAdvancing) return
    val currentQ = state.questions.getOrNull(state.currentIndex) ?: return

    if (selectedAnswer == currentQ.correctAnswer) {
      // --- CORRECT ANSWER ---
      soundManager.playSuccess()
      soundManager.playStarCollect()
      repository.recordAnswerSuccess(earnedStars = 1, earnedCoins = 2)

      val cheers = listOf("أحسنت! ⭐", "رائع! 🚀", "ممتاز! 🪐", "بطل! 🌟")
      val randomCheer = cheers.random()

      _playState.value = state.copy(
        mascotMood = MascotMood.CELEBRATING,
        buttonStates = mapOf(selectedAnswer to AnswerButtonState.CORRECT),
        confettiTrigger = state.confettiTrigger + 1,
        levelStarsEarned = state.levelStarsEarned + 1,
        correctCheer = randomCheer,
        isAdvancing = true
      )

      viewModelScope.launch {
        delay(1400) // Brief celebration before smooth transition
        val nextIndex = state.currentIndex + 1
        if (nextIndex < state.questions.size) {
          // Advance to next question
          _playState.value = _playState.value.copy(
            currentIndex = nextIndex,
            mascotMood = MascotMood.IDLE,
            buttonStates = emptyMap(),
            showHint = false,
            correctCheer = null,
            isAdvancing = false
          )
        } else {
          // Completed Level!
          val starsEarned = when {
            _playState.value.levelStarsEarned >= 5 -> 3
            _playState.value.levelStarsEarned >= 3 -> 2
            else -> 1
          }
          repository.completeLevel(state.currentLevel, starsEarned)
          soundManager.playLevelComplete()
          _screenState.value = ScreenState.LEVEL_COMPLETE
        }
      }
    } else {
      // --- WRONG ANSWER ---
      soundManager.playWrong()
      _playState.value = state.copy(
        mascotMood = MascotMood.OOPS,
        buttonStates = mapOf(selectedAnswer to AnswerButtonState.WRONG),
        showHint = true // Visual hint unlocked! (dots numbered on items)
      )

      viewModelScope.launch {
        delay(600)
        // Reset button so child can try again effortlessly
        _playState.value = _playState.value.copy(
          mascotMood = MascotMood.IDLE,
          buttonStates = emptyMap()
        )
      }
    }
  }

  fun onItemTapped() {
    soundManager.playPop()
  }

  fun replayCurrentLevel() {
    startLevel(_playState.value.currentLevel)
  }

  fun nextLevel() {
    val next = _playState.value.currentLevel + 1
    if (next <= 3) {
      startLevel(next)
    } else {
      startLevel(1)
    }
  }

  fun navigateToHome() {
    soundManager.playPop()
    _screenState.value = ScreenState.HOME
  }

  fun toggleSound() {
    soundManager.toggleSound()
  }

  override fun onCleared() {
    super.onCleared()
    soundManager.setAppActive(false)
  }
}
