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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.Difficulty
import com.example.model.DynamicMathChallenge
import com.example.model.MathOp
import com.example.ui.components.AnswerButtonState
import com.example.ui.components.ChalkLedgeDecor
import com.example.ui.components.ChalkTargetDropSlot
import com.example.ui.components.ChalkText
import com.example.ui.components.ChalkboardSlate
import com.example.ui.components.ConfettiParticleExplosion
import com.example.ui.components.IllustratedStar
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
  modifier: Modifier = Modifier
) {
  var isTargetHovered by remember { mutableStateOf(false) }
  var showModernButtonsDialog by remember { mutableStateOf(false) }
  var showOperationBars by remember { mutableStateOf(false) }

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
          onToggleSound = onToggleSound,
          onToggleOperationBars = { showOperationBars = !showOperationBars },
          onOpenModernButtons = { showModernButtonsDialog = true }
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
            .padding(horizontal = 14.dp, vertical = 4.dp)
            .navigationBarsPadding(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // The Two Top Button Bars: Modern Calculator Operations (+, −, ×, ÷) + Difficulty Selector
          // Hidden by default so they never disturb the view, smoothly animating into view on demand!
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
              // 1. Four Modern Calculator Buttons (+, −, ×, ÷)
              ModernOperationSelectorRow(
                selectedOp = selectedOp,
                onSelectOp = { op ->
                  onSelectOp(op)
                  showOperationBars = false // Auto-hide smoothly after selection
                }
              )

              // 2. Difficulty Selector (سهل | متوسط | صعب)
              ChalkDifficultySelector(
                selectedDifficulty = selectedDifficulty,
                onSelectDifficulty = { diff ->
                  onSelectDifficulty(diff)
                  showOperationBars = false // Auto-hide smoothly after selection
                }
              )
            }
          }

          // Dynamic Calculation Result Component (updates on number/operation press)
          DynamicCalculationResultBanner(
            challenge = challenge,
            currentSlotAnswer = currentSlotAnswer,
            isSlotAnswerWrong = isSlotAnswerWrong
          )

          // 4. THE REALISTIC WOODEN CHALKBOARD (From the reference photo)
          ChalkboardSlate(
            modifier = Modifier
              .fillMaxWidth()
              .height(230.dp)
          ) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
            ) {
              Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
              ) {
                // Hand-written Arabic story prompt in Chalk
                Text(
                  text = challenge.storyPromptAr,
                  fontFamily = CairoFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = Color(0xFFF1F5F9),
                  modifier = Modifier.padding(top = 4.dp)
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
                      fontSize = 44.sp,
                      color = Color(0xFFF8FAFC)
                    )

                    Spacer(modifier = Modifier.size(14.dp))

                    ChalkText(
                      text = challenge.effectiveOp.symbol,
                      fontSize = 38.sp,
                      color = Color(0xFFFEF08A), // Soft yellow chalk for operator
                      modifier = Modifier.clickable { showOperationBars = !showOperationBars }
                    )

                    Spacer(modifier = Modifier.size(14.dp))

                    ChalkText(
                      text = "${challenge.secondNum}",
                      fontSize = 44.sp,
                      color = Color(0xFFF8FAFC)
                    )

                    Spacer(modifier = Modifier.size(14.dp))

                    ChalkText(
                      text = "=",
                      fontSize = 38.sp,
                      color = Color(0xFFFEF08A)
                    )

                    Spacer(modifier = Modifier.size(14.dp))

                    // THE TARGET DROP SLOT ON THE CHALKBOARD
                    ChalkTargetDropSlot(
                      isHovered = isTargetHovered,
                      currentValue = currentSlotAnswer,
                      isWrong = isSlotAnswerWrong,
                      modifier = Modifier.testTag("chalk_target_slot")
                    )
                  }
                }

                // Decorative Chalk Ledge at the bottom of the board
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  ChalkLedgeDecor()

                  Text(
                    text = "المس أو اسحب الرقم للسبورة 👆",
                    fontFamily = CairoFontFamily,
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1)
                  )
                }
              }
            }
          }

          // 4. Desk Surface Header with Cactus (Faithful to photo)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            PottedCactusDecor(sizeDp = 48.dp)

            // Cheer Banner
            AnimatedVisibility(
              visible = correctCheer != null,
              enter = fadeIn() + scaleIn()
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
                  fontSize = 16.sp,
                  color = Color.White
                )
              }
            }

            Text(
              text = "اختر الإجابة:",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color(0xFF57534E)
            )
          }

          // 5. DRAGGABLE NUMBER SUGGESTIONS TRAY (Sitting on the wooden table)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x3078350F))
              .clip(RoundedCornerShape(24.dp))
              // Natural Wood Desk Texture
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
              .padding(14.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              challenge.options.forEach { optionVal ->
                val buttonState = buttonStates[optionVal] ?: AnswerButtonState.DEFAULT
                DraggableChalkTile(
                  value = optionVal,
                  state = buttonState,
                  onHoverChange = { isTargetHovered = it },
                  onDropInside = {
                    onAnswerDropped(optionVal)
                  },
                  modifier = Modifier.weight(1f)
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
 * Draggable Number Tile with fluid drag physics and instant tap responsiveness.
 * Supports BOTH drag-and-drop to the board AND direct tap with zero gesture conflicts.
 */
@Composable
private fun DraggableChalkTile(
  value: Int,
  state: AnswerButtonState,
  onHoverChange: (Boolean) -> Unit,
  onDropInside: () -> Unit,
  modifier: Modifier = Modifier
) {
  val shape = RoundedCornerShape(20.dp)

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

  val elevation = when (state) {
    AnswerButtonState.CORRECT, AnswerButtonState.WRONG -> 8.dp
    AnswerButtonState.DEFAULT -> 4.dp
  }

  Surface(
    onClick = { onDropInside() },
    shape = shape,
    color = bgColor,
    shadowElevation = elevation,
    border = androidx.compose.foundation.BorderStroke(
      width = if (state == AnswerButtonState.DEFAULT) 1.5.dp else 2.5.dp,
      color = borderColor
    ),
    modifier = modifier
      .height(68.dp)
      .testTag("tile_$value")
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          if (state == AnswerButtonState.DEFAULT) {
            Brush.verticalGradient(
              listOf(Color.White, Color(0xFFF8FAFC))
            )
          } else {
            Brush.verticalGradient(
              listOf(bgColor.copy(alpha = 0.92f), bgColor)
            )
          }
        ),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "$value",
        fontFamily = FredokaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = textColor
      )
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
            .shadow(2.dp, CircleShape, spotColor = Color(0x20000000))
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, CardBorder, CircleShape)
            .clickable(onClick = onToggleSound)
            .testTag("chalk_sound_toggle"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
            contentDescription = "الصوت",
            tint = if (isSoundEnabled) BrandSkyBlue else Color(0xFF94A3B8),
            modifier = Modifier.size(20.dp)
          )
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
          color = if (showOperationBars) activeColor else Color.White.copy(alpha = 0.95f),
          shadowElevation = 3.dp,
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (showOperationBars) activeColor else Color(0xFFE2E8F0)
          ),
          modifier = Modifier.testTag("chalk_toggle_operation_bars")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
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

            Text(
              text = "${selectedOp.titleAr} • ${selectedDifficulty.titleAr}",
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              color = if (showOperationBars) Color.White else Color(0xFF1E293B)
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

      // Rank & Streak
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val rankName = when (adaptiveTier) {
          1 -> "مبتدئ ذكي"
          2 -> "بطل الحساب"
          3 -> "عبقري الصبورة"
          else -> "أسطورة الرياضيات"
        }

        Box(
          modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFD6B588), RoundedCornerShape(14.dp))
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
              .shadow(2.dp, RoundedCornerShape(14.dp))
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFFFEF3C7))
              .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(14.dp))
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Text(
              text = "سلسلة x$comboStreak 🔥",
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
          .shadow(3.dp, RoundedCornerShape(18.dp), spotColor = BrandAmber)
          .clip(RoundedCornerShape(18.dp))
          .background(Color.White)
          .border(1.dp, BrandAmber.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
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
                listOf(Color.White.copy(alpha = 0.88f), Color.White.copy(alpha = 0.72f))
              )
            )
            .border(
              width = if (isSelected) 2.2.dp else 1.2.dp,
              brush = Brush.linearGradient(
                colors = if (isSelected) {
                  listOf(Color(0xFFFEF08A), Color.White)
                } else {
                  listOf(Color(0x80FFFFFF), Color(0x25FFFFFF))
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
              text = diff.titleAr,
              fontFamily = CairoFontFamily,
              fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
              fontSize = 13.sp,
              color = if (isSelected) Color.White else Color(0xFF1E293B)
            )
            Text(
              text = diff.rangeLabel,
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
  modifier: Modifier = Modifier
) {
  val num1 = challenge.firstNum
  val num2 = challenge.secondNum
  val op = challenge.effectiveOp.symbol
  val isAnswered = currentSlotAnswer != null
  val isCorrect = isAnswered && !isSlotAnswerWrong

  val resultText = remember(challenge, currentSlotAnswer, isSlotAnswerWrong) {
    if (currentSlotAnswer == null) {
      "العملية الحالية: $num1 $op $num2 = ؟  (اختر رقماً للحل)"
    } else if (isCorrect) {
      "النتيجة: $num1 $op $num2 = $currentSlotAnswer  (إجابة صحيحة! أحسنت 🎉)"
    } else {
      "النتيجة: $num1 $op $num2 = $currentSlotAnswer  (إجابة غير صحيحة، حاول ثانية ❌)"
    }
  }

  val bannerBg = when {
    isCorrect -> Color(0xFFECFDF5)
    isSlotAnswerWrong -> Color(0xFFFFF1F2)
    else -> Color(0xFFF8FAFC)
  }

  val bannerBorder = when {
    isCorrect -> Color(0xFF10B981)
    isSlotAnswerWrong -> Color(0xFFF43F5E)
    else -> Color(0xFFE2E8F0)
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
          text = if (isCorrect) "✓ صحيح" else if (isSlotAnswerWrong) "✗ خطأ" else "⏳ في الانتظار",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          color = Color.White
        )
      }
    }
  }
}
