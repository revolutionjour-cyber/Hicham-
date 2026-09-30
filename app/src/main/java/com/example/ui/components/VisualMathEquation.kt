package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MathOperator
import com.example.model.MathQuestion
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisualMathEquation(
  question: MathQuestion,
  showHint: Boolean,
  onItemTapped: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Card: Visual Items Box (e.g. ⭐⭐ + ⭐⭐⭐)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(10.dp, RoundedCornerShape(28.dp))
        .clip(RoundedCornerShape(28.dp))
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF2E1065), Color(0xFF1E1B4B))
          )
        )
        .border(3.dp, Color(0xFF818CF8).copy(alpha = 0.6f), RoundedCornerShape(28.dp))
        .padding(horizontal = 16.dp, vertical = 20.dp),
      contentAlignment = Alignment.Center
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Group A: First Count of Items
        ItemGroupCard(
          count = question.firstCount,
          emoji = question.item.emoji,
          startIndex = 1,
          showHint = showHint,
          onItemTapped = onItemTapped
        )

        // Math Operator (+ or -)
        Box(
          modifier = Modifier
            .padding(horizontal = 10.dp)
            .size(46.dp)
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFF59E0B), Color(0xFFD97706))
              )
            )
            .border(2.dp, Color.White, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = question.operator.symbol,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
          )
        }

        // Group B: Second Count of Items
        ItemGroupCard(
          count = question.secondCount,
          emoji = question.item.emoji,
          startIndex = if (question.operator == MathOperator.PLUS) question.firstCount + 1 else 1,
          showHint = showHint,
          isSubtracted = question.operator == MathOperator.MINUS,
          onItemTapped = onItemTapped
        )
      }
    }

    // Lower Card: Big Numeric Equation (e.g. 2 + 3 = ?)
    Box(
      modifier = Modifier
        .shadow(6.dp, RoundedCornerShape(20.dp))
        .clip(RoundedCornerShape(20.dp))
        .background(
          brush = Brush.horizontalGradient(
            colors = listOf(Color(0xFF4338CA), Color(0xFF6366F1))
          )
        )
        .border(2.5.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
        .padding(horizontal = 28.dp, vertical = 10.dp)
        .testTag("numeric_equation"),
      contentAlignment = Alignment.Center
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = "${question.firstCount}",
          fontSize = 38.sp,
          fontWeight = FontWeight.Black,
          color = Color(0xFFFDE047)
        )
        Text(
          text = question.operator.symbol,
          fontSize = 34.sp,
          fontWeight = FontWeight.Black,
          color = Color.White
        )
        Text(
          text = "${question.secondCount}",
          fontSize = 38.sp,
          fontWeight = FontWeight.Black,
          color = Color(0xFFFDE047)
        )
        Text(
          text = "=",
          fontSize = 36.sp,
          fontWeight = FontWeight.Black,
          color = Color.White
        )
        // Question Mark with pulse
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFEC4899))
            .border(2.dp, Color.White, RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "؟",
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ItemGroupCard(
  count: Int,
  emoji: String,
  startIndex: Int,
  showHint: Boolean,
  isSubtracted: Boolean = false,
  onItemTapped: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .background(Color(0x356366F1))
      .padding(horizontal = 8.dp, vertical = 6.dp)
  ) {
    FlowRow(
      horizontalArrangement = Arrangement.Center,
      verticalArrangement = Arrangement.Center,
      maxItemsInEachRow = 3
    ) {
      for (i in 0 until count) {
        val itemNumber = startIndex + i
        InteractiveItem(
          emoji = emoji,
          hintNumber = itemNumber,
          showHint = showHint,
          isSubtracted = isSubtracted,
          onTap = onItemTapped
        )
      }
    }
  }
}

@Composable
private fun InteractiveItem(
  emoji: String,
  hintNumber: Int,
  showHint: Boolean,
  isSubtracted: Boolean,
  onTap: () -> Unit
) {
  val scale = remember { Animatable(1f) }
  val scope = rememberCoroutineScope()

  Box(
    modifier = Modifier
      .padding(3.dp)
      .scale(scale.value)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = {
          onTap()
          scope.launch {
            scale.animateTo(1.4f, tween(100, easing = FastOutSlowInEasing))
            scale.animateTo(1f, tween(120, easing = FastOutSlowInEasing))
          }
        }
      ),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = emoji,
      fontSize = 34.sp,
      modifier = Modifier.padding(bottom = if (showHint) 10.dp else 0.dp)
    )

    // Subtraction strike visual
    if (isSubtracted) {
      Text(
        text = "❌",
        fontSize = 20.sp,
        modifier = Modifier.align(Alignment.Center)
      )
    }

    // Visual hint: numbered dot so child can count 1, 2, 3...
    AnimatedVisibility(
      visible = showHint,
      enter = fadeIn() + scaleIn(),
      modifier = Modifier.align(Alignment.BottomCenter)
    ) {
      Box(
        modifier = Modifier
          .size(20.dp)
          .clip(CircleShape)
          .background(Color(0xFF22C55E))
          .border(1.5.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "$hintNumber",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }
    }
  }
}
