package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.ModernMathButtons2x2Grid
import com.example.ui.components.ModernOperationSelectorRow
import com.example.ui.components.SunnyGardenBackground
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.AppLanguage
import com.example.model.Difficulty
import com.example.model.DynamicMathChallenge
import com.example.model.LanguageStrings
import com.example.model.MathOp
import com.example.model.TangibleItemType
import com.example.ui.components.AnswerButtonState
import com.example.ui.components.ChalkLedgeDecor
import com.example.ui.components.ChalkTargetDropSlot
import com.example.ui.components.ChalkText
import com.example.ui.components.ChalkboardSlate
import com.example.ui.components.ConfettiParticleExplosion
import com.example.ui.components.IllustratedItemIcon
import com.example.ui.components.IllustratedStar
import com.example.ui.components.LanguageSelectionDialog
import com.example.ui.components.PottedCactusDecor
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandRose
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.TextDark
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ChalkboardPlayScreen(
  challenge: DynamicMathChallenge,
  selectedOp: MathOp,
  selectedDifficulty: Difficulty,
  adaptiveTier: Int,
  totalStars: Int,
  comboStreak: Int,
  buttonStates: Map<Int, AnswerButtonState>,
  currentSlotAnswer: Int?,
  isSlotAnswerWrong: Boolean,
  correctCheer: String?,
  confettiTrigger: Int,
  isSoundEnabled: Boolean,
  onToggleSound: () -> Unit,
  onSelectOp: (MathOp) -> Unit,
  onSelectDifficulty: (Difficulty) -> Unit,
  onAnswerDropped: (Int) -> Unit,
  currentLanguage: AppLanguage = AppLanguage.MOROCCAN_ARABIC,
  onSelectLanguage: (AppLanguage) -> Unit = {},
  modifier: Modifier = Modifier
) {
  var isTargetHovered by remember { mutableStateOf(false) }
  var showModernButtonsDialog by remember { mutableStateOf(false) }
  var showOperationBars by remember { mutableStateOf(false) }
  var showLanguageDialog by remember { mutableStateOf(false) }
  var isVisualHelperEnabled by remember { mutableStateOf(true) }

  val cheerScale = remember { Animatable(0f) }
  LaunchedEffect(correctCheer) {
    if (correctCheer != null) {
      cheerScale.snapTo(0.3f)
      cheerScale.animateTo(1.2f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
      cheerScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }
  }

  // Sunny Garden Nature Background (Child reading book under green tree)
  SunnyGardenBackground(
    modifier = modifier.fillMaxSize()
  ) {
    ConfettiParticleExplosion(triggerKey = confettiTrigger)

    // Language Selection Dialog
    if (showLanguageDialog) {
      LanguageSelectionDialog(
        currentLanguage = currentLanguage,
        onSelectLanguage = { lang ->
          onSelectLanguage(lang)
          showLanguageDialog = false
        },
        onDismiss = { showLanguageDialog = false }
      )
    }

    // Modern 2x2 Calculator Buttons Showcase Dialog matching the reference image style
    if (showModernButtonsDialog) {
      Dialog(onDismissRequest = { showModernButtonsDialog = false }) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .shadow(28.dp, RoundedCornerShape(32.dp), spotColor = Color(0x407C3AED))
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .border(2.dp, Color(0xFFE2E8F0), RoundedCornerShape(32.dp))
            .padding(22.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "أزرار العمليات الحسابية (2×2)",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color(0xFF1E293B)
              )

              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFF1F5F9))
                  .clickable { showModernButtonsDialog = false },
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "إغلاق",
                  tint = Color(0xFF64748B),
                  modifier = Modifier.size(18.dp)
                )
              }
            }

            val currentSymbol = when (selectedOp) {
              MathOp.PLUS -> "+"
              MathOp.MINUS -> "−"
              MathOp.MULTIPLY -> "×"
              MathOp.DIVIDE -> "÷"
              MathOp.MIXED -> "🔀"
            }

            // 5-Button Modern Calculator Dialog Grid
            ModernMathButtons2x2Grid(
              buttonSize = 88.dp,
              selectedSymbol = currentSymbol,
              onButtonClick = { symbol ->
                when (symbol) {
                  "+" -> onSelectOp(MathOp.PLUS)
                  "−" -> onSelectOp(MathOp.MINUS)
                  "×" -> onSelectOp(MathOp.MULTIPLY)
                  "÷" -> onSelectOp(MathOp.DIVIDE)
                  "🔀" -> onSelectOp(MathOp.MIXED)
                }
                showModernButtonsDialog = false
              }
            )

            Text(
              text = "المس أي زر لاختيار العملية الحسابية 🎯",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 13.sp,
              color = Color(0xFF64748B)
            )
          }
        }
      }
    }

    Scaffold(
      containerColor = Color.Transparent,
      topBar = {
        ChalkTopBar(
          totalStars = totalStars,
          comboStreak = comboStreak,
          adaptiveTier = adaptiveTier,
          isSoundEnabled = isSoundEnabled,
          showOperationBars = showOperationBars,
          selectedOp = selectedOp,
          selectedDifficulty = selectedDifficulty,
          currentLanguage = currentLanguage,
          onOpenLanguageDialog = { showLanguageDialog = true },
          onToggleSound = onToggleSound,
          onToggleOperationBars = { showOperationBars = !showOperationBars },
          onOpenModernButtons = { showModernButtonsDialog = true }
        )
      }
    ) { innerPadding ->
      BoxWithConstraints(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        contentAlignment = Alignment.TopCenter
      ) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
          // ════════════════════════════════════════════════════════════════════════════
          // LANDSCAPE CINEMATIC LAYOUT: Panoramic Blackboard & Ergonomic Bottom Desk
          // Chalkboard is in the center; Answer buttons are at the bottom within natural thumb reach.
          // ════════════════════════════════════════════════════════════════════════════
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .widthIn(max = 680.dp)
              .verticalScroll(rememberScrollState())
              .padding(horizontal = 16.dp, vertical = 2.dp)
              .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            // Expandable Operations & Difficulty Bar (Transparent background)
            AnimatedVisibility(
              visible = showOperationBars,
              enter = fadeIn() + expandVertically(),
              exit = fadeOut() + shrinkVertically()
            ) {
              Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                ModernOperationSelectorRow(
                  selectedOp = selectedOp,
                  currentLanguage = currentLanguage,
                  onSelectOp = { op ->
                    onSelectOp(op)
                    showOperationBars = false
                  }
                )

                ChalkDifficultySelector(
                  selectedDifficulty = selectedDifficulty,
                  currentLanguage = currentLanguage,
                  onSelectDifficulty = { diff ->
                    onSelectDifficulty(diff)
                    showOperationBars = false
                  }
                )
              }
            }

            // Dynamic Calculation Result Component (Compact ribbon)
            DynamicCalculationResultBanner(
              challenge = challenge,
              currentSlotAnswer = currentSlotAnswer,
              isSlotAnswerWrong = isSlotAnswerWrong,
              currentLanguage = currentLanguage
            )

            // Panoramic Centered Chalkboard Slate
            ChalkboardSlate(
              modifier = Modifier
                .fillMaxWidth()
                .height(155.dp)
            ) {
              ChalkboardCardContent(
                challenge = challenge,
                isTargetHovered = isTargetHovered,
                currentSlotAnswer = currentSlotAnswer,
                isSlotAnswerWrong = isSlotAnswerWrong,
                isVisualHelperEnabled = isVisualHelperEnabled,
                currentLanguage = currentLanguage,
                onToggleVisualHelper = { isVisualHelperEnabled = !isVisualHelperEnabled },
                onToggleOperationBars = { showOperationBars = !showOperationBars }
              )
            }

            // Desk Surface Header with Cactus & Cheer (Distortion-free layout)
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .padding(horizontal = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              // Cactus decor anchored on the start edge
              Box(
                modifier = Modifier.align(Alignment.CenterStart)
              ) {
                PottedCactusDecor(sizeDp = 28.dp)
              }

              // Normal instruction prompt when not cheering (never squished, maxLines = 1)
              androidx.compose.animation.AnimatedVisibility(
                visible = correctCheer == null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.CenterEnd)
              ) {
                val chooseText = when (currentLanguage) {
                  AppLanguage.MOROCCAN_ARABIC -> "اختر الجواب ديالك 👇"
                  AppLanguage.FRENCH -> "Choisis ta réponse 👇"
                  AppLanguage.ENGLISH -> "Choose your answer 👇"
                }
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x25FFFFFF))
                    .border(1.dp, Color(0x35BA9E80), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                  Text(
                    text = chooseText,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 1,
                    color = Color(0xFF44403C)
                  )
                }
              }

              // Majestic Floating Cheer Banner in Center (Full width freedom, zero distortion)
              androidx.compose.animation.AnimatedVisibility(
                visible = correctCheer != null,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
              ) {
                val bannerColor = if (isSlotAnswerWrong) BrandRose else BrandEmerald
                Box(
                  modifier = Modifier
                    .scale(cheerScale.value)
                    .shadow(4.dp, RoundedCornerShape(14.dp), spotColor = bannerColor)
                    .clip(RoundedCornerShape(14.dp))
                    .background(bannerColor)
                    .border(1.5.dp, Color.White, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 4.dp)
                ) {
                  Text(
                    text = correctCheer ?: "",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    maxLines = 1,
                    color = Color.White
                  )
                }
              }
            }

            // Answer Cards Tray: Sitting comfortably at the bottom within thumb reach of both hands!
            AnswerCardsTray(
              challenge = challenge,
              buttonStates = buttonStates,
              isVisualHelperEnabled = isVisualHelperEnabled,
              onHoverChange = { isTargetHovered = it },
              onAnswerDropped = onAnswerDropped
            )

            Spacer(modifier = Modifier.height(6.dp))
          }
        } else {
          // ════════════════════════════════════════════════════════════════════════════
          // PORTRAIT ADAPTIVE LAYOUT: Responsive Column with Scroll Safety
          // Height-aware Chalkboard + Scroll guarantees buttons are never cut off
          // ════════════════════════════════════════════════════════════════════════════
          val boardHeight = if (maxHeight < 680.dp) 185.dp else 225.dp

          Column(
            modifier = Modifier
              .fillMaxWidth()
              .widthIn(max = 600.dp)
              .verticalScroll(rememberScrollState())
              .padding(horizontal = 14.dp, vertical = 4.dp)
              .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Expandable Operations & Difficulty Selector (Transparent)
            AnimatedVisibility(
              visible = showOperationBars,
              enter = fadeIn() + expandVertically(),
              exit = fadeOut() + shrinkVertically()
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 2.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                ModernOperationSelectorRow(
                  selectedOp = selectedOp,
                  currentLanguage = currentLanguage,
                  onSelectOp = { op ->
                    onSelectOp(op)
                    showOperationBars = false
                  }
                )

                ChalkDifficultySelector(
                  selectedDifficulty = selectedDifficulty,
                  currentLanguage = currentLanguage,
                  onSelectDifficulty = { diff ->
                    onSelectDifficulty(diff)
                    showOperationBars = false
                  }
                )
              }
            }

            // Dynamic Calculation Result Component
            DynamicCalculationResultBanner(
              challenge = challenge,
              currentSlotAnswer = currentSlotAnswer,
              isSlotAnswerWrong = isSlotAnswerWrong,
              currentLanguage = currentLanguage
            )

            // Realistic Wooden Chalkboard with Adaptive Height
            ChalkboardSlate(
              modifier = Modifier
                .fillMaxWidth()
                .height(boardHeight)
            ) {
              ChalkboardCardContent(
                challenge = challenge,
                isTargetHovered = isTargetHovered,
                currentSlotAnswer = currentSlotAnswer,
                isSlotAnswerWrong = isSlotAnswerWrong,
                isVisualHelperEnabled = isVisualHelperEnabled,
                currentLanguage = currentLanguage,
                onToggleVisualHelper = { isVisualHelperEnabled = !isVisualHelperEnabled },
                onToggleOperationBars = { showOperationBars = !showOperationBars }
              )
            }

            // Desk Surface Header with Cactus & Cheer (Distortion-free layout)
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .padding(horizontal = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              // Cactus decor anchored on the start edge
              Box(
                modifier = Modifier.align(Alignment.CenterStart)
              ) {
                PottedCactusDecor(sizeDp = 44.dp)
              }

              // Normal instruction prompt when not cheering (never squished, maxLines = 1)
              androidx.compose.animation.AnimatedVisibility(
                visible = correctCheer == null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.CenterEnd)
              ) {
                val chooseText = when (currentLanguage) {
                  AppLanguage.MOROCCAN_ARABIC -> "اختر الجواب ديالك 👇"
                  AppLanguage.FRENCH -> "Choisis ta réponse 👇"
                  AppLanguage.ENGLISH -> "Choose your answer 👇"
                }
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x25FFFFFF))
                    .border(1.dp, Color(0x35BA9E80), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                  Text(
                    text = chooseText,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    color = Color(0xFF44403C)
                  )
                }
              }

              // Majestic Floating Cheer Banner in Center (Full width freedom, zero distortion)
              androidx.compose.animation.AnimatedVisibility(
                visible = correctCheer != null,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
              ) {
                val bannerColor = if (isSlotAnswerWrong) BrandRose else BrandEmerald
                Box(
                  modifier = Modifier
                    .scale(cheerScale.value)
                    .shadow(6.dp, RoundedCornerShape(18.dp), spotColor = bannerColor)
                    .clip(RoundedCornerShape(18.dp))
                    .background(bannerColor)
                    .border(2.dp, Color.White, RoundedCornerShape(18.dp))
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = correctCheer ?: "",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    maxLines = 1,
                    color = Color.White
                  )
                }
              }
            }

            // DRAGGABLE NUMBER SUGGESTIONS TRAY (Sitting on wooden table)
            AnswerCardsTray(
              challenge = challenge,
              buttonStates = buttonStates,
              isVisualHelperEnabled = isVisualHelperEnabled,
              onHoverChange = { isTargetHovered = it },
              onAnswerDropped = onAnswerDropped
            )

            Spacer(modifier = Modifier.height(16.dp))
          }
        }
      }
    }
  }
}

/**
 * Chalkboard Card Content: Arabic story prompt, hand-written chalk equation, target slot, visual counters, and ledge
 */
@Composable
private fun ChalkboardCardContent(
  challenge: DynamicMathChallenge,
  isTargetHovered: Boolean,
  currentSlotAnswer: Int?,
  isSlotAnswerWrong: Boolean,
  isVisualHelperEnabled: Boolean,
  currentLanguage: AppLanguage,
  onToggleVisualHelper: () -> Unit,
  onToggleOperationBars: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 10.dp, vertical = 6.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Hand-written Arabic / French / English story prompt in Chalk
      Text(
        text = challenge.storyPromptAr,
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color(0xFFF1F5F9),
        modifier = Modifier.padding(top = 2.dp)
      )

      // Hand-written Chalk Math Equation: Left to Right
      CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          ChalkText(
            text = "${challenge.firstNum}",
            fontSize = 38.sp,
            color = Color(0xFFF8FAFC)
          )

          Spacer(modifier = Modifier.size(10.dp))

          ChalkText(
            text = challenge.effectiveOp.symbol,
            fontSize = 32.sp,
            color = Color(0xFFFEF08A), // Soft yellow chalk for operator
            modifier = Modifier.clickable { onToggleOperationBars() }
          )

          Spacer(modifier = Modifier.size(10.dp))

          ChalkText(
            text = "${challenge.secondNum}",
            fontSize = 38.sp,
            color = Color(0xFFF8FAFC)
          )

          Spacer(modifier = Modifier.size(10.dp))

          ChalkText(
            text = "=",
            fontSize = 32.sp,
            color = Color(0xFFFEF08A)
          )

          Spacer(modifier = Modifier.size(10.dp))

          // THE TARGET DROP SLOT ON THE CHALKBOARD
          ChalkTargetDropSlot(
            isHovered = isTargetHovered,
            currentValue = currentSlotAnswer,
            isWrong = isSlotAnswerWrong,
            modifier = Modifier.testTag("chalk_target_slot")
          )
        }
      }

      val isEasyLevel = challenge.difficulty == Difficulty.EASY

      // Interactive Visual Counters Assistant (المساعد البصري التفاعلي للأرقام - مخصص للمستوى السهل فقط)
      if (isEasyLevel) {
        InteractiveVisualCounterSection(
          challenge = challenge,
          isHelperEnabled = isVisualHelperEnabled
        )
      }

      // Decorative Chalk Ledge at the bottom of the board
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        ChalkLedgeDecor()

        if (isEasyLevel) {
          // Toggleable Visual Helper Button on the Chalk Ledge (Easy level only)
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isVisualHelperEnabled) Color(0x35FEF08A) else Color(0x18FFFFFF))
              .border(
                1.dp,
                if (isVisualHelperEnabled) Color(0x80FEF08A) else Color(0x25FFFFFF),
                RoundedCornerShape(12.dp)
              )
              .clickable { onToggleVisualHelper() }
              .padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
          ) {
            IllustratedItemIcon(
              itemType = challenge.itemType,
              sizeDp = 16.dp
            )
            Text(
              text = if (isVisualHelperEnabled) {
                LanguageStrings.getVisualHelperActive(currentLanguage)
              } else {
                LanguageStrings.getVisualHelperShow(currentLanguage)
              },
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp,
              color = if (isVisualHelperEnabled) Color(0xFFFEF08A) else Color(0xFFCBD5E1)
            )
          }
        }

        Text(
          text = LanguageStrings.getLedgeInstruction(currentLanguage),
          fontFamily = CairoFontFamily,
          fontSize = 11.sp,
          color = Color(0xFFCBD5E1)
        )
      }
    }
  }
}

/**
 * Interactive Visual Counter Assistant: Displays tactile, countable representations
 * of the math operation (e.g. 3 apples + 2 apples). Children can tap any item
 * to bounce it and hear / see the count!
 */
@Composable
private fun InteractiveVisualCounterSection(
  challenge: DynamicMathChallenge,
  isHelperEnabled: Boolean,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = isHelperEnabled,
    enter = expandVertically() + fadeIn(),
    exit = shrinkVertically() + fadeOut(),
    modifier = modifier.fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(Color(0x22000000))
        .border(1.dp, Color(0x28FFFFFF), RoundedCornerShape(12.dp))
        .padding(horizontal = 8.dp, vertical = 4.dp),
      contentAlignment = Alignment.Center
    ) {
      val firstCount = challenge.firstNum.coerceIn(1, 8)
      val secondCount = challenge.secondNum.coerceIn(1, 8)

      when (challenge.effectiveOp) {
        MathOp.PLUS -> {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
          ) {
            CountableItemGroup(
              count = firstCount,
              itemType = challenge.itemType,
              startIdx = 1
            )

            Text(
              text = "+",
              fontFamily = FredokaFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = Color(0xFFFEF08A),
              modifier = Modifier.padding(horizontal = 6.dp)
            )

            CountableItemGroup(
              count = secondCount,
              itemType = challenge.itemType,
              startIdx = firstCount + 1
            )
          }
        }

        MathOp.MINUS -> {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
          ) {
            CountableSubtractedGroup(
              totalCount = firstCount,
              subtractedCount = secondCount,
              itemType = challenge.itemType
            )
          }
        }

        MathOp.MULTIPLY -> {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
          ) {
            val groups = firstCount.coerceAtMost(3)
            val perGroup = secondCount.coerceAtMost(4)
            for (g in 0 until groups) {
              if (g > 0) {
                Text(
                  text = "+",
                  fontFamily = FredokaFontFamily,
                  fontSize = 14.sp,
                  color = Color(0xFFFEF08A),
                  modifier = Modifier.padding(horizontal = 3.dp)
                )
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0x25FFFFFF))
                  .padding(3.dp)
              ) {
                CountableItemGroup(
                  count = perGroup,
                  itemType = challenge.itemType,
                  startIdx = g * perGroup + 1,
                  sizeDp = 22.dp
                )
              }
            }
          }
        }

        MathOp.DIVIDE -> {
          val total = challenge.firstNum.coerceAtMost(10)
          val groups = challenge.secondNum.coerceIn(1, 3)
          val perGroup = (total / groups).coerceAtLeast(1)
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
          ) {
            for (g in 0 until groups) {
              Box(
                modifier = Modifier
                  .padding(horizontal = 3.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0x25FFFFFF))
                  .border(1.dp, Color(0x40FEF08A), RoundedCornerShape(8.dp))
                  .padding(3.dp)
              ) {
                CountableItemGroup(
                  count = perGroup,
                  itemType = challenge.itemType,
                  startIdx = g * perGroup + 1,
                  sizeDp = 22.dp
                )
              }
            }
          }
        }

        MathOp.MIXED -> {
          CountableItemGroup(
            count = firstCount,
            itemType = challenge.itemType,
            startIdx = 1
          )
        }
      }
    }
  }
}

/**
 * A row of interactive items that can be tapped to bounce and show their ordinal number
 */
@Composable
private fun CountableItemGroup(
  count: Int,
  itemType: TangibleItemType,
  startIdx: Int = 1,
  sizeDp: Dp = 26.dp,
  modifier: Modifier = Modifier
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    modifier = modifier
  ) {
    for (i in 0 until count) {
      val itemNumber = startIdx + i
      SingleInteractiveItem(
        number = itemNumber,
        itemType = itemType,
        sizeDp = sizeDp
      )
    }
  }
}

/**
 * Individual interactive item: Tapping pops the scale and reveals a badge!
 */
@Composable
private fun SingleInteractiveItem(
  number: Int,
  itemType: TangibleItemType,
  sizeDp: Dp = 26.dp,
  isCrossedOut: Boolean = false
) {
  var isTapped by remember { mutableStateOf(false) }
  val scale = remember { Animatable(1f) }
  val scope = rememberCoroutineScope()

  Box(
    modifier = Modifier
      .scale(scale.value)
      .clickable(
        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
        indication = null
      ) {
        isTapped = !isTapped
        scope.launch {
          scale.animateTo(1.35f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
          scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
        }
      },
    contentAlignment = Alignment.Center
  ) {
    IllustratedItemIcon(
      itemType = itemType,
      sizeDp = sizeDp,
      modifier = Modifier.alpha(if (isCrossedOut) 0.35f else 1f)
    )

    if (isCrossedOut) {
      Text(
        text = "✕",
        fontFamily = FredokaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color(0xFFF43F5E)
      )
    } else if (isTapped) {
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .offset(x = 4.dp, y = (-4).dp)
          .size(16.dp)
          .clip(CircleShape)
          .background(Color(0xFFFEF08A)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$number",
          fontFamily = FredokaFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 10.sp,
          color = Color(0xFF78350F)
        )
      }
    }
  }
}

/**
 * Subtraction group showing remaining vs crossed out items
 */
@Composable
private fun CountableSubtractedGroup(
  totalCount: Int,
  subtractedCount: Int,
  itemType: TangibleItemType,
  sizeDp: Dp = 26.dp
) {
  val remaining = (totalCount - subtractedCount).coerceAtLeast(0)
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    for (i in 1..remaining) {
      SingleInteractiveItem(
        number = i,
        itemType = itemType,
        sizeDp = sizeDp,
        isCrossedOut = false
      )
    }

    if (subtractedCount > 0 && remaining > 0) {
      Text(
        text = "|",
        fontFamily = FredokaFontFamily,
        fontSize = 16.sp,
        color = Color(0x60FFFFFF),
        modifier = Modifier.padding(horizontal = 2.dp)
      )
    }

    for (i in 1..subtractedCount.coerceAtMost(totalCount)) {
      SingleInteractiveItem(
        number = remaining + i,
        itemType = itemType,
        sizeDp = sizeDp,
        isCrossedOut = true
      )
    }
  }
}

/**
 * Answer Cards Tray: The 4 draggable/clickable tactile answer cards
 */
@Composable
private fun AnswerCardsTray(
  challenge: DynamicMathChallenge,
  buttonStates: Map<Int, AnswerButtonState>,
  isVisualHelperEnabled: Boolean,
  onHoverChange: (Boolean) -> Unit,
  onAnswerDropped: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val isEasyLevel = challenge.difficulty == Difficulty.EASY
  val itemType = challenge.itemType
  val correctAnswer = challenge.answer

  Box(
    modifier = modifier
      .fillMaxWidth()
      .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x3078350F))
      .clip(RoundedCornerShape(24.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFFEADBCE), // Polished light oak
            Color(0xFFDCC8B4),
            Color(0xFFCBB29B)
          )
        )
      )
      .border(2.dp, Color(0xFFBA9E80), RoundedCornerShape(24.dp))
      .padding(12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      challenge.options.forEach { optionVal ->
        val buttonState = buttonStates[optionVal] ?: AnswerButtonState.DEFAULT
        val isCorrectOption = (optionVal == correctAnswer)
        DraggableChalkTile(
          value = optionVal,
          state = buttonState,
          isEasyLevel = isEasyLevel,
          isVisualHelperEnabled = isVisualHelperEnabled,
          itemType = itemType,
          isCorrectOption = isCorrectOption,
          onHoverChange = onHoverChange,
          onDropInside = {
            onAnswerDropped(optionVal)
          },
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

/**
 * Draggable Number Tile with fluid drag physics and instant tap responsiveness.
 * Supports BOTH drag-and-drop to the board AND direct tap with tactile spring bounce.
 */
@Composable
private fun DraggableChalkTile(
  value: Int,
  state: AnswerButtonState,
  onHoverChange: (Boolean) -> Unit,
  onDropInside: () -> Unit,
  isEasyLevel: Boolean = false,
  isVisualHelperEnabled: Boolean = false,
  itemType: TangibleItemType? = null,
  isCorrectOption: Boolean = false,
  modifier: Modifier = Modifier
) {
  val shape = RoundedCornerShape(20.dp)
  val scope = rememberCoroutineScope()

  val offsetX = remember { Animatable(0f) }
  val offsetY = remember { Animatable(0f) }
  var isDragging by remember { mutableStateOf(false) }
  val pressScale = remember { Animatable(1f) }
  val shakeOffset = remember { Animatable(0f) }

  // Playful shake animation on wrong answer
  LaunchedEffect(state) {
    if (state == AnswerButtonState.WRONG) {
      shakeOffset.snapTo(0f)
      repeat(3) {
        shakeOffset.animateTo(-10f, spring(stiffness = Spring.StiffnessHigh))
        shakeOffset.animateTo(10f, spring(stiffness = Spring.StiffnessHigh))
      }
      shakeOffset.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
    }
  }

  val bgColor = when (state) {
    AnswerButtonState.CORRECT -> Color(0xFF10B981) // Emerald
    AnswerButtonState.WRONG -> Color(0xFFF43F5E)   // Coral Rose
    AnswerButtonState.DEFAULT -> Color.White
  }

  val borderColor = when (state) {
    AnswerButtonState.CORRECT -> Color(0xFF6EE7B7) // Mint glow
    AnswerButtonState.WRONG -> Color(0xFFFECDD3)   // Pink glow
    AnswerButtonState.DEFAULT -> Color(0xFFE2E8F0)
  }

  val textColor = when (state) {
    AnswerButtonState.CORRECT, AnswerButtonState.WRONG -> Color.White
    AnswerButtonState.DEFAULT -> Color(0xFF1E293B)
  }

  val elevation = when {
    isDragging -> 16.dp
    state != AnswerButtonState.DEFAULT -> 8.dp
    else -> 4.dp
  }

  val currentScale = if (isDragging) 1.14f else pressScale.value

  Box(
    modifier = modifier
      .zIndex(if (isDragging) 15f else 1f)
      .offset {
        IntOffset(
          (offsetX.value + shakeOffset.value).roundToInt(),
          offsetY.value.roundToInt()
        )
      }
      .scale(currentScale)
      .pointerInput(value) {
        awaitEachGesture {
          val down = awaitFirstDown(requireUnconsumed = false)
          var isDrag = false
          var totalDragX = 0f
          var totalDragY = 0f
          val touchSlop = viewConfiguration.touchSlop

          // Start tactile press animation
          scope.launch {
            pressScale.animateTo(0.92f, spring(stiffness = Spring.StiffnessHigh))
          }

          while (true) {
            val event = awaitPointerEvent()
            val change = event.changes.firstOrNull { it.id == down.id } ?: break

            if (!change.pressed) {
              // Pointer released!
              if (!isDrag) {
                // IT'S A DIRECT TAP! Instant response!
                scope.launch {
                  pressScale.animateTo(1.08f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                  pressScale.animateTo(1f)
                }
                onDropInside()
              } else {
                // DRAG RELEASED
                val wasHovered = offsetY.value < -70f
                onHoverChange(false)
                isDragging = false
                scope.launch {
                  if (wasHovered) {
                    onDropInside()
                  }
                  launch { offsetX.animateTo(0f, spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow)) }
                  launch { offsetY.animateTo(0f, spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow)) }
                  launch { pressScale.animateTo(1f) }
                }
              }
              break
            } else {
              val drag = change.positionChange()
              totalDragX += drag.x
              totalDragY += drag.y
              val dist = kotlin.math.sqrt(totalDragX * totalDragX + totalDragY * totalDragY)

              if (!isDrag && dist > touchSlop) {
                isDrag = true
                isDragging = true
                scope.launch {
                  pressScale.animateTo(1.14f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                }
              }

              if (isDrag) {
                change.consume()
                scope.launch {
                  offsetX.snapTo(offsetX.value + drag.x)
                  offsetY.snapTo(offsetY.value + drag.y)
                  onHoverChange(offsetY.value < -70f)
                }
              }
            }
          }
        }
      }
      .testTag("tile_$value")
  ) {
    val showVisualHelperFruit = isEasyLevel && isVisualHelperEnabled && itemType != null
    val isHighlightedOption = showVisualHelperFruit && isCorrectOption && state == AnswerButtonState.DEFAULT

    Surface(
      shape = shape,
      color = bgColor,
      shadowElevation = if (isHighlightedOption) 6.dp else elevation,
      border = androidx.compose.foundation.BorderStroke(
        width = if (isHighlightedOption) 2.2.dp else if (state == AnswerButtonState.DEFAULT) 1.5.dp else 2.5.dp,
        color = if (isHighlightedOption) Color(0xFFF59E0B) else borderColor
      ),
      modifier = Modifier
        .fillMaxWidth()
        .height(if (showVisualHelperFruit) 74.dp else 68.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            if (state == AnswerButtonState.DEFAULT) {
              if (isHighlightedOption) {
                Brush.verticalGradient(
                  listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7))
                )
              } else {
                Brush.verticalGradient(
                  listOf(Color.White, Color(0xFFF8FAFC))
                )
              }
            } else {
              Brush.verticalGradient(
                listOf(bgColor.copy(alpha = 0.92f), bgColor)
              )
            }
          ),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(
            text = "$value",
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = if (showVisualHelperFruit && value in 1..8) 26.sp else 32.sp,
            color = if (isHighlightedOption) Color(0xFFB45309) else textColor
          )

          // Mini fruit counters for visual arithmetic comprehension
          if (showVisualHelperFruit && value in 1..8) {
            Row(
              horizontalArrangement = Arrangement.spacedBy(2.dp),
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(top = 1.dp)
            ) {
              repeat(value.coerceAtMost(5)) {
                IllustratedItemIcon(
                  itemType = itemType,
                  sizeDp = 12.dp
                )
              }
            }
          }
        }

        // إشارة المساعد البصري على زر الإقتراح (Visual helper cue badge on suggestion button)
        if (showVisualHelperFruit && isCorrectOption) {
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .offset(x = 3.dp, y = (-3).dp)
              .size(22.dp)
              .shadow(2.dp, CircleShape)
              .clip(CircleShape)
              .background(Color(0xFFFEF08A))
              .border(1.2.dp, Color(0xFFF59E0B), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            IllustratedItemIcon(
              itemType = itemType,
              sizeDp = 14.dp
            )
          }
        }
      }
    }
  }
}

/**
 * Top bar with Stars, Streak, Sound Toggle, and Expandable Operations Pill
 */
@Composable
private fun ChalkTopBar(
  totalStars: Int,
  comboStreak: Int,
  adaptiveTier: Int,
  isSoundEnabled: Boolean,
  showOperationBars: Boolean,
  selectedOp: MathOp,
  selectedDifficulty: Difficulty,
  currentLanguage: AppLanguage = AppLanguage.MOROCCAN_ARABIC,
  onOpenLanguageDialog: () -> Unit = {},
  onToggleSound: () -> Unit,
  onToggleOperationBars: () -> Unit,
  onOpenModernButtons: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 4.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Sound Toggle Button
        Box(
          modifier = Modifier
            .size(38.dp)
            .shadow(1.dp, CircleShape, spotColor = Color(0x10000000))
            .clip(CircleShape)
            .background(
              Brush.linearGradient(
                listOf(Color.White.copy(alpha = 0.38f), Color.White.copy(alpha = 0.18f))
              )
            )
            .border(
              1.2.dp,
              Brush.linearGradient(listOf(Color(0x90FFFFFF), Color(0x35FFFFFF))),
              CircleShape
            )
            .clickable(onClick = onToggleSound)
            .testTag("chalk_sound_toggle"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
            contentDescription = "الصوت",
            tint = if (isSoundEnabled) Color(0xFF0284C7) else Color(0xFF64748B),
            modifier = Modifier.size(20.dp)
          )
        }

        // Language Selection Button
        Box(
          modifier = Modifier
            .height(38.dp)
            .shadow(1.dp, RoundedCornerShape(19.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(19.dp))
            .background(
              Brush.linearGradient(
                listOf(Color.White.copy(alpha = 0.38f), Color.White.copy(alpha = 0.18f))
              )
            )
            .border(
              1.2.dp,
              Brush.linearGradient(listOf(Color(0x90FFFFFF), Color(0x35FFFFFF))),
              RoundedCornerShape(19.dp)
            )
            .clickable(onClick = onOpenLanguageDialog)
            .padding(horizontal = 10.dp)
            .testTag("chalk_language_toggle"),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
          ) {
            Text(
              text = currentLanguage.flag,
              fontSize = 15.sp
            )
            Text(
              text = when (currentLanguage) {
                AppLanguage.MOROCCAN_ARABIC -> "المغرب"
                AppLanguage.FRENCH -> "FR"
                AppLanguage.ENGLISH -> "EN"
              },
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color(0xFF0F172A)
            )
          }
        }

        val activeColor = when (selectedOp) {
          MathOp.PLUS -> Color(0xFF2563EB)
          MathOp.MINUS -> Color(0xFFE11D48)
          MathOp.MULTIPLY -> Color(0xFF7C3AED)
          MathOp.DIVIDE -> Color(0xFF0D9488)
          MathOp.MIXED -> Color(0xFFD97706)
        }

        // Sleek Floating Toggle Pill to gracefully reveal or hide the two button bars
        Surface(
          onClick = onToggleOperationBars,
          shape = RoundedCornerShape(20.dp),
          color = Color.Transparent,
          shadowElevation = 1.dp,
          modifier = Modifier.testTag("chalk_toggle_operation_bars")
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(
                if (showOperationBars) {
                  Brush.linearGradient(
                    listOf(activeColor.copy(alpha = 0.90f), activeColor.copy(alpha = 0.80f))
                  )
                } else {
                  Brush.linearGradient(
                    listOf(Color.White.copy(alpha = 0.42f), Color.White.copy(alpha = 0.22f))
                  )
                }
              )
              .border(
                1.2.dp,
                if (showOperationBars) {
                  Brush.linearGradient(
                    listOf(Color.White.copy(alpha = 0.90f), Color.White.copy(alpha = 0.75f))
                  )
                } else {
                  Brush.linearGradient(
                    listOf(Color(0x99FFFFFF), Color(0x40FFFFFF))
                  )
                },
                RoundedCornerShape(20.dp)
              )
              .padding(horizontal = 11.dp, vertical = 6.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(
                text = selectedOp.symbol,
                fontFamily = FredokaFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (showOperationBars) Color.White else activeColor
              )

              val opTitle = LanguageStrings.getOpTitle(selectedOp, currentLanguage)
              val diffTitle = LanguageStrings.getDifficultyTitle(selectedDifficulty, currentLanguage)
              Text(
                text = "$opTitle • $diffTitle",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = if (showOperationBars) Color.White else Color(0xFF0F172A)
              )

              Icon(
                imageVector = if (showOperationBars) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (showOperationBars) "إخفاء الأزرار" else "إظهار أزرار العمليات",
                tint = if (showOperationBars) Color.White else activeColor,
                modifier = Modifier.size(15.dp)
              )
            }
          }
        }
      }

      // Rank & Streak
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val rankName = when (currentLanguage) {
          AppLanguage.MOROCCAN_ARABIC -> when (adaptiveTier) {
            1 -> "مبتدئ ذكي"
            2 -> "بطل الحساب"
            3 -> "عبقري الصبورة"
            else -> "أسطورة الرياضيات"
          }
          AppLanguage.FRENCH -> when (adaptiveTier) {
            1 -> "Débutant malin"
            2 -> "Champion calcul"
            3 -> "Génie tableau"
            else -> "Légende maths"
          }
          AppLanguage.ENGLISH -> when (adaptiveTier) {
            1 -> "Smart Beginner"
            2 -> "Math Champion"
            3 -> "Board Genius"
            else -> "Math Legend"
          }
        }

        Box(
          modifier = Modifier
            .shadow(1.dp, RoundedCornerShape(16.dp), spotColor = Color(0x10000000))
            .clip(RoundedCornerShape(16.dp))
            .background(
              Brush.linearGradient(
                listOf(Color.White.copy(alpha = 0.38f), Color.White.copy(alpha = 0.18f))
              )
            )
            .border(
              1.2.dp,
              Brush.linearGradient(listOf(Color(0x80D6B588), Color(0x35D6B588))),
              RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Text(
            text = rankName,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color(0xFF78350F)
          )
        }

        if (comboStreak >= 2) {
          Box(
            modifier = Modifier
              .shadow(1.dp, RoundedCornerShape(16.dp))
              .clip(RoundedCornerShape(16.dp))
              .background(
                Brush.linearGradient(
                  listOf(Color(0x45FEF3C7), Color(0x20F59E0B))
                )
              )
              .border(
                1.2.dp,
                Brush.linearGradient(listOf(Color(0x90F59E0B), Color(0x40F59E0B))),
                RoundedCornerShape(16.dp)
              )
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            val comboText = when (currentLanguage) {
              AppLanguage.MOROCCAN_ARABIC -> "متتالي x$comboStreak 🔥"
              AppLanguage.FRENCH -> "Série x$comboStreak 🔥"
              AppLanguage.ENGLISH -> "Streak x$comboStreak 🔥"
            }
            Text(
              text = comboText,
              fontFamily = FredokaFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color(0xFFB45309)
            )
          }
        }
      }

      // Stars Counter
      Box(
        modifier = Modifier
          .shadow(1.dp, RoundedCornerShape(18.dp))
          .clip(RoundedCornerShape(18.dp))
          .background(
            Brush.linearGradient(
              listOf(Color.White.copy(alpha = 0.38f), Color.White.copy(alpha = 0.18f))
            )
          )
          .border(
            1.2.dp,
            Brush.linearGradient(listOf(Color(0x80F59E0B), Color(0x35F59E0B))),
            RoundedCornerShape(18.dp)
          )
          .padding(horizontal = 10.dp, vertical = 5.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          IllustratedStar(sizeDp = 18.dp, isFilled = true)
          Text(
            text = "$totalStars",
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = BrandAmber
          )
        }
      }
    }
  }
}

/**
 * Difficulty Selector Bar: سهل | متوسط | صعب
 */
@Composable
private fun ChalkDifficultySelector(
  selectedDifficulty: Difficulty,
  currentLanguage: AppLanguage = AppLanguage.MOROCCAN_ARABIC,
  onSelectDifficulty: (Difficulty) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp, vertical = 2.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Difficulty.values().forEach { diff ->
      val isSelected = diff == selectedDifficulty
      val shape = RoundedCornerShape(18.dp)

      val diffGradient = when (diff) {
        Difficulty.EASY -> Brush.linearGradient(
          colors = listOf(Color(0xFF0D9488), Color(0xFF059669), Color(0xFF10B981)),
          start = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY),
          end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, 0f)
        )
        Difficulty.MEDIUM -> Brush.linearGradient(
          colors = listOf(Color(0xFF2563EB), Color(0xFF3B82F6), Color(0xFF4F46E5)),
          start = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY),
          end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, 0f)
        )
        Difficulty.HARD -> Brush.linearGradient(
          colors = listOf(Color(0xFF6366F1), Color(0xFF7C3AED), Color(0xFF9333EA)),
          start = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY),
          end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, 0f)
        )
      }

      Surface(
        onClick = { onSelectDifficulty(diff) },
        shape = shape,
        color = Color.Transparent,
        shadowElevation = if (isSelected) 8.dp else 2.dp,
        modifier = Modifier
          .weight(1f)
          .height(50.dp)
          .scale(if (isSelected) 1.05f else 0.98f)
          .testTag("chalk_diff_${diff.name}")
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              if (isSelected) diffGradient
              else Brush.linearGradient(
                listOf(Color.White.copy(alpha = 0.45f), Color.White.copy(alpha = 0.22f))
              )
            )
            .border(
              width = if (isSelected) 2.2.dp else 1.2.dp,
              brush = Brush.linearGradient(
                colors = if (isSelected) {
                  listOf(Color(0xFFFEF08A), Color.White)
                } else {
                  listOf(Color(0x95FFFFFF), Color(0x35FFFFFF))
                },
                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
              ),
              shape = shape
            ),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Text(
              text = LanguageStrings.getDifficultyTitle(diff, currentLanguage),
              fontFamily = CairoFontFamily,
              fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
              fontSize = 13.sp,
              color = if (isSelected) Color.White else Color(0xFF1E293B)
            )
            Text(
              text = LanguageStrings.getDifficultyRange(diff, currentLanguage),
              fontFamily = FredokaFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp,
              color = if (isSelected) Color.White.copy(alpha = 0.90f) else Color(0xFF64748B)
            )
          }

          // Glowing dot indicator matching operation buttons
          if (isSelected) {
            Box(
              modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 6.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(Color(0xFFFEF08A))
            )
          }
        }
      }
    }
  }
}

/**
 * Dynamic Calculation Result Banner:
 * Displays a clear Text component showing the current calculation and dynamically
 * updates whenever operation buttons (+, −, ×, ÷) or answer number buttons are pressed.
 */
@Composable
private fun DynamicCalculationResultBanner(
  challenge: DynamicMathChallenge,
  currentSlotAnswer: Int?,
  isSlotAnswerWrong: Boolean,
  currentLanguage: AppLanguage = AppLanguage.MOROCCAN_ARABIC,
  modifier: Modifier = Modifier
) {
  val num1 = challenge.firstNum
  val num2 = challenge.secondNum
  val op = challenge.effectiveOp.symbol
  val isAnswered = currentSlotAnswer != null
  val isCorrect = isAnswered && !isSlotAnswerWrong

  val resultText = remember(challenge, currentSlotAnswer, isSlotAnswerWrong, currentLanguage) {
    if (currentSlotAnswer == null) {
      when (currentLanguage) {
        AppLanguage.MOROCCAN_ARABIC -> "العملية دابا: $num1 $op $num2 = ؟  (اختار رقم للحل)"
        AppLanguage.FRENCH -> "Opération en cours : $num1 $op $num2 = ?  (Choisis un chiffre)"
        AppLanguage.ENGLISH -> "Current operation: $num1 $op $num2 = ?  (Pick a number)"
      }
    } else if (isCorrect) {
      when (currentLanguage) {
        AppLanguage.MOROCCAN_ARABIC -> "النتيجة: $num1 $op $num2 = $currentSlotAnswer  (جواب صحيح! تبارك الله عليك 🎉)"
        AppLanguage.FRENCH -> "Résultat : $num1 $op $num2 = $currentSlotAnswer  (C'est exact ! Bravo 🎉)"
        AppLanguage.ENGLISH -> "Result: $num1 $op $num2 = $currentSlotAnswer  (Correct answer! Great job 🎉)"
      }
    } else {
      when (currentLanguage) {
        AppLanguage.MOROCCAN_ARABIC -> "النتيجة: $num1 $op $num2 = $currentSlotAnswer  (الجواب ماشي هو هذاك، عاود جرب ❌)"
        AppLanguage.FRENCH -> "Résultat : $num1 $op $num2 = $currentSlotAnswer  (Ce n'est pas ça, réessaie ❌)"
        AppLanguage.ENGLISH -> "Result: $num1 $op $num2 = $currentSlotAnswer  (Not quite right, try again ❌)"
      }
    }
  }

  val bannerBg = when {
    isCorrect -> Color(0xDCEDFDF5)
    isSlotAnswerWrong -> Color(0xDCFFF1F2)
    else -> Color(0x65FFFFFF)
  }

  val bannerBorder = when {
    isCorrect -> Color(0xFF10B981)
    isSlotAnswerWrong -> Color(0xFFF43F5E)
    else -> Color(0x95FFFFFF)
  }

  val textColor = when {
    isCorrect -> Color(0xFF065F46)
    isSlotAnswerWrong -> Color(0xFF9F1239)
    else -> Color(0xFF1E293B)
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .shadow(3.dp, RoundedCornerShape(16.dp), spotColor = Color(0x15000000))
      .clip(RoundedCornerShape(16.dp))
      .background(bannerBg)
      .border(1.5.dp, bannerBorder, RoundedCornerShape(16.dp))
      .padding(horizontal = 14.dp, vertical = 8.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Dynamic Text Component for live mathematical calculation result
      Text(
        text = resultText,
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = textColor,
        modifier = Modifier
          .weight(1f)
          .testTag("dynamic_calculation_result_text")
      )

      Spacer(modifier = Modifier.size(8.dp))

      // Status Badge
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(10.dp))
          .background(
            if (isCorrect) Color(0xFF10B981)
            else if (isSlotAnswerWrong) Color(0xFFF43F5E)
            else Color(0xFF6366F1)
          )
          .padding(horizontal = 8.dp, vertical = 3.dp)
      ) {
        Text(
          text = if (isCorrect) {
            when (currentLanguage) {
              AppLanguage.MOROCCAN_ARABIC -> "✓ صحيح"
              AppLanguage.FRENCH -> "✓ Exact"
              AppLanguage.ENGLISH -> "✓ Correct"
            }
          } else if (isSlotAnswerWrong) {
            when (currentLanguage) {
              AppLanguage.MOROCCAN_ARABIC -> "✗ خطأ"
              AppLanguage.FRENCH -> "✗ Faux"
              AppLanguage.ENGLISH -> "✗ Wrong"
            }
          } else {
            when (currentLanguage) {
              AppLanguage.MOROCCAN_ARABIC -> "⏳ كنتسناو"
              AppLanguage.FRENCH -> "⏳ En attente"
              AppLanguage.ENGLISH -> "⏳ Waiting"
            }
          },
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          color = Color.White
        )
      }
    }
  }
}
