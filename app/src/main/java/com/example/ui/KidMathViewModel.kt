package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.KidSoundManager
import com.example.data.KidGameRepository
import com.example.data.KidGameState
import com.example.model.AdaptiveQuestionGenerator
import com.example.model.Difficulty
import com.example.model.DynamicMathChallenge
import com.example.model.MathOp
import com.example.model.MathQuestion
import com.example.model.MonsterMathChallenge
import com.example.model.RoyalSanctuaryChallenge
import com.example.ui.components.AnswerButtonState
import com.example.ui.components.MascotMood
import com.example.ui.components.MonsterExpression
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ScreenState {
  HOME,
  PLAYING,
  LEVEL_COMPLETE
}

enum class MainNavTab {
  MAP,
  REWARDS,
  PROGRESS,
  SETTINGS
}

data class PlayUiState(
  val currentLevel: Int = 1,
  val challenges: List<MonsterMathChallenge> = emptyList(),
  val royalChallenges: List<RoyalSanctuaryChallenge> = emptyList(),
  val questions: List<MathQuestion> = emptyList(),
  val currentIndex: Int = 0,
  val currentFedCount: Int = 0,
  val currentPlacedGems: Int = 0,
  val isBalanced: Boolean = false,
  val monsterMood: MonsterExpression = MonsterExpression.HUNGRY,
  val mascotMood: MascotMood = MascotMood.IDLE,
  val buttonStates: Map<Int, AnswerButtonState> = emptyMap(),
  val showHint: Boolean = false,
  val confettiTrigger: Int = 0,
  val levelStarsEarned: Int = 0,
  val comboStreak: Int = 0,
  val correctCheer: String? = null,
  val isAdvancing: Boolean = false
)

class KidMathViewModel(application: Application) : AndroidViewModel(application) {
  val soundManager = KidSoundManager(application)
  private val repository = KidGameRepository(application)

  val gameState: StateFlow<KidGameState> = repository.gameState

  // Direct Playing State
  private val _selectedOp = MutableStateFlow(MathOp.PLUS)
  val selectedOp: StateFlow<MathOp> = _selectedOp.asStateFlow()

  private val _selectedDifficulty = MutableStateFlow(Difficulty.EASY)
  val selectedDifficulty: StateFlow<Difficulty> = _selectedDifficulty.asStateFlow()

  private val _adaptiveTier = MutableStateFlow(1)
  val adaptiveTier: StateFlow<Int> = _adaptiveTier.asStateFlow()

  private val _currentChallenge = MutableStateFlow(
    AdaptiveQuestionGenerator.generateQuestion(
      chosenOp = MathOp.PLUS,
      difficulty = Difficulty.EASY,
      adaptiveTier = 1
    )
  )
  val currentChallenge: StateFlow<DynamicMathChallenge> = _currentChallenge.asStateFlow()

  private val _directButtonStates = MutableStateFlow<Map<Int, AnswerButtonState>>(emptyMap())
  val directButtonStates: StateFlow<Map<Int, AnswerButtonState>> = _directButtonStates.asStateFlow()

  private val _comboStreak = MutableStateFlow(0)
  val comboStreak: StateFlow<Int> = _comboStreak.asStateFlow()

  private val _directStars = MutableStateFlow(0)
  val directStars: StateFlow<Int> = _directStars.asStateFlow()

  private val _correctCheer = MutableStateFlow<String?>(null)
  val correctCheer: StateFlow<String?> = _correctCheer.asStateFlow()

  private val _confettiTrigger = MutableStateFlow(0)
  val confettiTrigger: StateFlow<Int> = _confettiTrigger.asStateFlow()

  private val _currentSlotAnswer = MutableStateFlow<Int?>(null)
  val currentSlotAnswer: StateFlow<Int?> = _currentSlotAnswer.asStateFlow()

  private val _isSlotAnswerWrong = MutableStateFlow(false)
  val isSlotAnswerWrong: StateFlow<Boolean> = _isSlotAnswerWrong.asStateFlow()

  private var isAdvancingQuestion = false

  // Legacy state for backward compatibility
  private val _screenState = MutableStateFlow(ScreenState.PLAYING)
  val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

  private val _activeTab = MutableStateFlow(MainNavTab.MAP)
  val activeTab: StateFlow<MainNavTab> = _activeTab.asStateFlow()

  private val _playState = MutableStateFlow(PlayUiState())
  val playState: StateFlow<PlayUiState> = _playState.asStateFlow()

  fun onSelectOp(op: MathOp) {
    isAdvancingQuestion = false
    soundManager.playTap()
    _selectedOp.value = op
    _directButtonStates.value = emptyMap()
    _correctCheer.value = null
    refreshNewQuestion()
  }

  fun onSelectDifficulty(diff: Difficulty) {
    isAdvancingQuestion = false
    soundManager.playTap()
    _selectedDifficulty.value = diff
    _adaptiveTier.value = 1
    _comboStreak.value = 0
    _directButtonStates.value = emptyMap()
    _correctCheer.value = null
    refreshNewQuestion()
  }

  fun onDirectAnswerSelected(selectedAnswer: Int) {
    if (isAdvancingQuestion) return
    val challenge = _currentChallenge.value
    _currentSlotAnswer.value = selectedAnswer
    soundManager.playChalkSnap()

    if (selectedAnswer == challenge.answer) {
      _isSlotAnswerWrong.value = false
      soundManager.playSuccess()
      val newStreak = _comboStreak.value + 1
      _comboStreak.value = newStreak
      _directStars.value = _directStars.value + 1
      _confettiTrigger.value = _confettiTrigger.value + 1
      repository.recordAnswerSuccess(earnedStars = 1, earnedCoins = 2)

      // Automatic Adaptive Difficulty scaling every 3 consecutive correct answers!
      if (newStreak % 3 == 0) {
        val nextTier = (_adaptiveTier.value + 1).coerceAtMost(5)
        _adaptiveTier.value = nextTier
        soundManager.playLevelUp()
      }

      val cheers = if (newStreak >= 2) {
        listOf(
          "إجابة عبقرية متتالية x$newStreak! 🔥",
          "رائع جداً! مستوى ذكاء متصاعد x$newStreak! 🌟",
          "أنت بطل الرياضيات بلا منازع! 🚀"
        )
      } else {
        listOf(
          "أحسنت يا بطل! 👏",
          "إجابة صحيحة وممتازة! 🌟",
          "رائع جداً! ✨",
          "ممتاز يا عبقري! 🎯"
        )
      }

      _directButtonStates.value = mapOf(selectedAnswer to AnswerButtonState.CORRECT)
      _correctCheer.value = cheers.random()
      isAdvancingQuestion = true

      viewModelScope.launch {
        delay(950)
        _correctCheer.value = null
        _directButtonStates.value = emptyMap()
        refreshNewQuestion()
        _currentSlotAnswer.value = null
        isAdvancingQuestion = false
      }
    } else {
      // Gentle encouraging incorrect feedback
      _isSlotAnswerWrong.value = true
      soundManager.playWrong()
      _directButtonStates.value = mapOf(selectedAnswer to AnswerButtonState.WRONG)
      _comboStreak.value = 0

      val encouragingPhrases = listOf(
        "لا بأس، حاول ثانية! 💪",
        "فكر مرة أخرى، أنت تستطيع! ✨",
        "قريب جداً، أعد المحاولة! 🎯"
      )
      _correctCheer.value = encouragingPhrases.random()

      viewModelScope.launch {
        delay(800)
        _correctCheer.value = null
        _directButtonStates.value = emptyMap()
        _currentSlotAnswer.value = null
        _isSlotAnswerWrong.value = false
      }
    }
  }

  private fun refreshNewQuestion() {
    _currentSlotAnswer.value = null
    _isSlotAnswerWrong.value = false
    _currentChallenge.value = AdaptiveQuestionGenerator.generateQuestion(
      chosenOp = _selectedOp.value,
      difficulty = _selectedDifficulty.value,
      adaptiveTier = _adaptiveTier.value
    )
  }

  fun toggleSound() {
    soundManager.toggleSound()
  }

  // --- Legacy Compatibility ---
  fun setActiveTab(tab: MainNavTab) {
    soundManager.playTap()
    _activeTab.value = tab
  }

  fun startLevel(level: Int) {
    val diff = when (level) {
      1 -> Difficulty.EASY
      2 -> Difficulty.MEDIUM
      else -> Difficulty.HARD
    }
    onSelectDifficulty(diff)
  }

  fun onFeedItem() {}
  fun onChooseAnswer(selectedAnswer: Int) {
    onDirectAnswerSelected(selectedAnswer)
  }
  fun onAddGemToPan() {}
  fun onAnswerSelected(selectedAnswer: Int) {
    onDirectAnswerSelected(selectedAnswer)
  }
  fun onItemTapped() {}
  fun replayCurrentLevel() {
    refreshNewQuestion()
  }
  fun nextLevel() {
    val current = _selectedDifficulty.value
    val next = when (current) {
      Difficulty.EASY -> Difficulty.MEDIUM
      Difficulty.MEDIUM -> Difficulty.HARD
      Difficulty.HARD -> Difficulty.EASY
    }
    onSelectDifficulty(next)
  }
  fun navigateToHome() {}
  fun updateProfile(name: String, avatarIndex: Int) {
    repository.updatePlayerProfile(name, avatarIndex)
  }
  fun resetProgress() {
    soundManager.playTap()
    repository.resetAllProgress()
    _directStars.value = 0
    _comboStreak.value = 0
    _adaptiveTier.value = 1
    refreshNewQuestion()
  }
}
