package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Difficulty
import com.example.model.DynamicMathChallenge
import com.example.model.MathOp
import com.example.model.TangibleItemType
import com.example.ui.components.AnswerButtonState
import com.example.ui.components.ConfettiParticleExplosion
import com.example.ui.components.IllustratedCross
import com.example.ui.components.IllustratedItemIcon
import com.example.ui.components.IllustratedStar
import com.example.ui.theme.BrandAmber
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandRose
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.BrandSkyBlueBg
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.FredokaFontFamily
import com.example.ui.theme.TextDark
import kotlinx.coroutines.launch

@Composable
fun DirectMathPlayScreen(
  challenge: DynamicMathChallenge,
  selectedOp: MathOp,
  selectedDifficulty: Difficulty,
  adaptiveTier: Int,
  totalStars: Int,
  comboStreak: Int,
  buttonStates: Map<Int, AnswerButtonState>,
  correctCheer: String?,
  confettiTrigger: Int,
  isSoundEnabled: Boolean,
  onToggleSound: () -> Unit,
  onSelectOp: (MathOp) -> Unit,
  onSelectDifficulty: (Difficulty) -> Unit,
  onAnswerSelected: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val cheerScale = remember { Animatable(0f) }
  LaunchedEffect(correctCheer) {
    if (correctCheer != null) {
      cheerScale.snapTo(0.3f)
      cheerScale.animateTo(1.2f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
      cheerScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFFE0F2FE), // Sky
            Color(0xFFF8FAFC), // White ice
            Color(0xFFFEF3C7)  // Warm buttercup
          )
        )
      )
  ) {
    ConfettiParticleExplosion(triggerKey = confettiTrigger)

    Scaffold(
      containerColor = Color.Transparent,
      topBar = {
        DirectPlayTopBar(
          totalStars = totalStars,
          comboStreak = comboStreak,
          adaptiveTier = adaptiveTier,
          isSoundEnabled = isSoundEnabled,
          onToggleSound = onToggleSound
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
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .navigationBarsPadding(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // 1. Operator Selector: (+, -, ×, ÷, 🔀)
          OperatorSelectorBar(
            selectedOp = selectedOp,
            onSelectOp = onSelectOp
          )

          // 2. Difficulty Selector: (سهل، متوسط، صعب)
          DifficultySelectorBar(
            selectedDifficulty = selectedDifficulty,
            onSelectDifficulty = onSelectDifficulty
          )

          // 3. Adaptive Tier Indicator
          AdaptiveProgressionPill(
            tier = adaptiveTier,
            comboStreak = comboStreak
          )

          // 4. Story / Prompt Bubble
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = Color(0x2038BDF8))
              .clip(RoundedCornerShape(22.dp))
              .background(Color.White)
              .border(2.dp, CardBorder, RoundedCornerShape(22.dp))
              .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = challenge.storyPromptAr,
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = TextDark
            )
          }

          // 5. Positive Cheer Banner
          AnimatedVisibility(
            visible = correctCheer != null,
            enter = fadeIn() + scaleIn()
          ) {
            Box(
              modifier = Modifier
                .scale(cheerScale.value)
                .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = BrandEmerald)
                .clip(RoundedCornerShape(20.dp))
                .background(BrandEmerald)
                .border(2.dp, Color.White, RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
              Text(
                text = correctCheer ?: "",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = Color.White
              )
            }
          }

          // 6. Tactile Visual Counting Tray (Shown dynamically in Easy mode)
          if (challenge.difficulty == Difficulty.EASY) {
            VisualEasyCountingTray(challenge = challenge)
          }

          // 7. High-Contrast Modern Equation Pill: LTR
          CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Box(
              modifier = Modifier
                .shadow(8.dp, RoundedCornerShape(28.dp), spotColor = Color(0x3038BDF8))
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .border(2.dp, CardBorder, RoundedCornerShape(28.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .testTag("math_equation_box"),
              contentAlignment = Alignment.Center
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
              ) {
                Text(
                  text = "${challenge.firstNum}",
                  fontFamily = FredokaFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 38.sp,
                  color = BrandSkyBlue
                )
                Text(
                  text = challenge.effectiveOp.symbol,
                  fontFamily = FredokaFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 32.sp,
                  color = TextDark
                )
                Text(
                  text = "${challenge.secondNum}",
                  fontFamily = FredokaFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 38.sp,
                  color = BrandSkyBlue
                )
                Text(
                  text = "=",
                  fontFamily = FredokaFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 32.sp,
                  color = TextDark
                )

                // Missing Target Slot
                Box(
                  modifier = Modifier
                    .size(52.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = BrandRose)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                      Brush.verticalGradient(
                        listOf(BrandRose, Color(0xFFE11D48))
                      )
                    )
                    .border(2.dp, Color.White, RoundedCornerShape(16.dp)),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "؟",
                    fontFamily = FredokaFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.White
                  )
                }
              }
            }
          }

          // 8. The 4 Big Answer Choice Buttons (2x2 Grid)
          Text(
            text = "اختر الإجابة الصحيحة:",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF64748B)
          )

          Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            val chunked = challenge.options.chunked(2)
            chunked.forEachIndexed { rowIndex, rowOptions ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                rowOptions.forEachIndexed { colIndex, optionVal ->
                  val buttonState = buttonStates[optionVal] ?: AnswerButtonState.DEFAULT
                  DirectAnswerCard(
                    value = optionVal,
                    state = buttonState,
                    onClick = { onAnswerSelected(optionVal) },
                    modifier = Modifier.weight(1f)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(20.dp))
        }
      }
    }
  }
}

/**
 * Top bar with Stars, Streak, Sound Toggle, and Rank
 */
@Composable
private fun DirectPlayTopBar(
  totalStars: Int,
  comboStreak: Int,
  adaptiveTier: Int,
  isSoundEnabled: Boolean,
  onToggleSound: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Sound Toggle Button
      Box(
        modifier = Modifier
          .size(46.dp)
          .shadow(4.dp, CircleShape, spotColor = Color(0x20000000))
          .clip(CircleShape)
          .background(Color.White)
          .border(1.5.dp, CardBorder, CircleShape)
          .clickable(onClick = onToggleSound)
          .testTag("sound_toggle_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
          contentDescription = "تبديل الصوت",
          tint = if (isSoundEnabled) BrandSkyBlue else Color(0xFF94A3B8),
          modifier = Modifier.size(24.dp)
        )
      }

      // Combo Streak & Rank
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val rankTitle = when (adaptiveTier) {
          1 -> "مبتدئ ذكي"
          2 -> "بطل الحساب"
          3 -> "عبقري الأرقام"
          else -> "أسطورة الرياضيات"
        }

        Box(
          modifier = Modifier
            .shadow(3.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(BrandSkyBlueBg)
            .border(1.5.dp, BrandSkyBlue.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text(
            text = rankTitle,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = BrandSkyBlue
          )
        }

        if (comboStreak >= 2) {
          Box(
            modifier = Modifier
              .shadow(3.dp, RoundedCornerShape(16.dp))
              .clip(RoundedCornerShape(16.dp))
              .background(Color(0xFFFEF3C7))
              .border(1.5.dp, Color(0xFFF59E0B), RoundedCornerShape(16.dp))
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = "سلسلة x$comboStreak 🔥",
              fontFamily = FredokaFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color(0xFFB45309)
            )
          }
        }
      }

      // Stars Pill
      Box(
        modifier = Modifier
          .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = BrandAmber)
          .clip(RoundedCornerShape(20.dp))
          .background(Color.White)
          .border(1.5.dp, BrandAmber.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
          .padding(horizontal = 12.dp, vertical = 6.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          IllustratedStar(sizeDp = 20.dp, isFilled = true)
          Text(
            text = "$totalStars",
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = BrandAmber
          )
        }
      }
    }
  }
}

/**
 * Segmented Buttons to switch between Operations (+, -, ×, ÷, 🔀)
 */
@Composable
private fun OperatorSelectorBar(
  selectedOp: MathOp,
  onSelectOp: (MathOp) -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .shadow(6.dp, RoundedCornerShape(24.dp), spotColor = Color(0x2038BDF8))
      .clip(RoundedCornerShape(24.dp))
      .background(Color.White)
      .border(1.5.dp, CardBorder, RoundedCornerShape(24.dp))
      .padding(6.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      MathOp.values().forEach { op ->
        val isSelected = op == selectedOp
        val interaction = remember { MutableInteractionSource() }
        val isPressed by interaction.collectIsPressedAsState()
        val scale by animateFloatAsState(
          targetValue = if (isPressed) 0.92f else 1f,
          label = "op_scale"
        )

        Box(
          modifier = Modifier
            .weight(1f)
            .scale(scale)
            .height(52.dp)
            .shadow(
              elevation = if (isSelected) 4.dp else 0.dp,
              shape = RoundedCornerShape(18.dp),
              spotColor = BrandSkyBlue
            )
            .clip(RoundedCornerShape(18.dp))
            .background(
              if (isSelected) {
                Brush.verticalGradient(
                  listOf(BrandSkyBlue, Color(0xFF0284C7))
                )
              } else {
                Brush.verticalGradient(
                  listOf(Color(0xFFF8FAFC), Color(0xFFF1F5F9))
                )
              }
            )
            .clickable(
              interactionSource = interaction,
              indication = null,
              onClick = { onSelectOp(op) }
            )
            .testTag("op_${op.name}"),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Text(
              text = op.symbol,
              fontFamily = FredokaFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 24.sp,
              color = if (isSelected) Color.White else TextDark
            )
            Text(
              text = op.titleAr,
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              color = if (isSelected) Color.White.copy(alpha = 0.9f) else Color(0xFF64748B)
            )
          }
        }
      }
    }
  }
}

/**
 * Difficulty Selector Bar: سهل | متوسط | صعب
 */
@Composable
private fun DifficultySelectorBar(
  selectedDifficulty: Difficulty,
  onSelectDifficulty: (Difficulty) -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .shadow(4.dp, RoundedCornerShape(22.dp), spotColor = Color(0x15000000))
      .clip(RoundedCornerShape(22.dp))
      .background(Color.White)
      .border(1.5.dp, CardBorder, RoundedCornerShape(22.dp))
      .padding(6.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Difficulty.values().forEach { diff ->
        val isSelected = diff == selectedDifficulty
        val color = Color(diff.colorHex)

        Box(
          modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .shadow(
              elevation = if (isSelected) 3.dp else 0.dp,
              shape = RoundedCornerShape(16.dp),
              spotColor = color
            )
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) color else Color(0xFFF8FAFC))
            .clickable { onSelectDifficulty(diff) }
            .testTag("diff_${diff.name}"),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = diff.titleAr,
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = if (isSelected) Color.White else TextDark
            )
            Text(
              text = "(${diff.rangeLabel})",
              fontFamily = FredokaFontFamily,
              fontSize = 11.sp,
              color = if (isSelected) Color.White.copy(alpha = 0.85f) else Color(0xFF94A3B8)
            )
          }
        }
      }
    }
  }
}

/**
 * Adaptive dynamic difficulty indicator
 */
@Composable
private fun AdaptiveProgressionPill(
  tier: Int,
  comboStreak: Int
) {
  val progress = ((comboStreak % 3) / 3f).coerceIn(0f, 1f)

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(Color(0xFFF1F5F9))
      .padding(horizontal = 14.dp, vertical = 8.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "المستوى التلقائي: درجة $tier",
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = Color(0xFF475569)
      )

      val remaining = (3 - (comboStreak % 3))
      Text(
        text = if (remaining == 3) "حافظ على إجاباتك الصحيحة!" else "باقي $remaining للترقية التلقائية!",
        fontFamily = CairoFontFamily,
        fontSize = 11.sp,
        color = BrandSkyBlue
      )
    }
  }
}

/**
 * Tactile Visual Tray for Easy mode
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VisualEasyCountingTray(challenge: DynamicMathChallenge) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .shadow(6.dp, RoundedCornerShape(26.dp), spotColor = Color(0x2038BDF8))
      .clip(RoundedCornerShape(26.dp))
      .background(Color.White)
      .border(1.5.dp, CardBorder, RoundedCornerShape(26.dp))
      .padding(12.dp),
    contentAlignment = Alignment.Center
  ) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
      if (challenge.effectiveOp == MathOp.PLUS) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          VisualItemsRow(count = challenge.firstNum, item = challenge.itemType)
          Text(
            text = "+",
            fontFamily = FredokaFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = BrandAmber,
            modifier = Modifier.padding(horizontal = 10.dp)
          )
          VisualItemsRow(count = challenge.secondNum, item = challenge.itemType)
        }
      } else if (challenge.effectiveOp == MathOp.MINUS) {
        val total = challenge.firstNum
        val sub = challenge.secondNum
        val rem = total - sub
        FlowRow(
          horizontalArrangement = Arrangement.Center,
          verticalArrangement = Arrangement.Center,
          maxItemsInEachRow = 6
        ) {
          for (i in 0 until rem) {
            IllustratedItemIcon(itemType = challenge.itemType, sizeDp = 38.dp, modifier = Modifier.padding(4.dp))
          }
          for (i in 0 until sub) {
            Box(modifier = Modifier.padding(4.dp), contentAlignment = Alignment.Center) {
              IllustratedItemIcon(itemType = challenge.itemType, sizeDp = 38.dp)
              IllustratedCross(sizeDp = 22.dp)
            }
          }
        }
      } else {
        // Multiply or Divide equal groups
        VisualItemsRow(count = challenge.firstNum, item = challenge.itemType)
      }
    }
  }
}

@Composable
private fun VisualItemsRow(count: Int, item: TangibleItemType) {
  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
    for (i in 0 until count.coerceAtMost(6)) {
      IllustratedItemIcon(itemType = item, sizeDp = 36.dp)
    }
  }
}

/**
 * Big tactile Answer Card (2x2 Grid Button)
 */
@Composable
private fun DirectAnswerCard(
  value: Int,
  state: AnswerButtonState,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val interaction = remember { MutableInteractionSource() }
  val isPressed by interaction.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.94f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "ans_scale"
  )

  val bgColor = when (state) {
    AnswerButtonState.CORRECT -> BrandEmerald
    AnswerButtonState.WRONG -> BrandRose
    AnswerButtonState.DEFAULT -> Color.White
  }

  val textColor = when (state) {
    AnswerButtonState.CORRECT, AnswerButtonState.WRONG -> Color.White
    AnswerButtonState.DEFAULT -> TextDark
  }

  val borderColor = when (state) {
    AnswerButtonState.CORRECT -> BrandEmerald
    AnswerButtonState.WRONG -> BrandRose
    AnswerButtonState.DEFAULT -> CardBorder
  }

  Box(
    modifier = modifier
      .scale(scale)
      .height(68.dp)
      .shadow(6.dp, RoundedCornerShape(22.dp), spotColor = Color(0x20000000))
      .clip(RoundedCornerShape(22.dp))
      .background(bgColor)
      .border(2.dp, borderColor, RoundedCornerShape(22.dp))
      .clickable(
        interactionSource = interaction,
        indication = null,
        onClick = onClick
      )
      .testTag("ans_btn_$value"),
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
