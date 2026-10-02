package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.LanguageStrings
import com.example.model.MathOp
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.FredokaFontFamily

/**
 * Clean, harmonious horizontal row of the 5 Modern Calculator Buttons:
 * (+) جمع, (−) طرح, (×) ضرب, (÷) قسمة, (🔀) شامل
 * Designed with luxury jewel-toned gradients, 3D faceted mathematical symbols,
 * and high-responsiveness.
 */
@Composable
fun ModernOperationSelectorRow(
  selectedOp: MathOp,
  onSelectOp: (MathOp) -> Unit,
  currentLanguage: AppLanguage = AppLanguage.MOROCCAN_ARABIC,
  modifier: Modifier = Modifier
) {
  val operations = listOf(
    Pair("+", MathOp.PLUS),
    Pair("−", MathOp.MINUS),
    Pair("×", MathOp.MULTIPLY),
    Pair("÷", MathOp.DIVIDE),
    Pair("🔀", MathOp.MIXED)
  )

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp, vertical = 2.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    verticalAlignment = Alignment.CenterVertically
  ) {
    operations.forEach { (sym, op) ->
      val title = LanguageStrings.getOpTitle(op, currentLanguage)
      val isSelected = (selectedOp == op)
      val activeTextColor = when (op) {
        MathOp.PLUS -> Color(0xFF2563EB)
        MathOp.MINUS -> Color(0xFFE11D48)
        MathOp.MULTIPLY -> Color(0xFF7C3AED)
        MathOp.DIVIDE -> Color(0xFF0D9488)
        MathOp.MIXED -> Color(0xFFD97706)
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clickable { onSelectOp(op) }
      ) {
        ModernCalculatorButton(
          symbol = sym,
          size = 52.dp,
          isSelected = isSelected,
          onClick = { onSelectOp(op) },
          modifier = Modifier.testTag("modern_op_$sym")
        )

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
              if (isSelected) activeTextColor.copy(alpha = 0.15f)
              else Color.White.copy(alpha = 0.70f)
            )
            .padding(horizontal = 6.dp, vertical = 1.dp)
        ) {
          Text(
            text = title,
            fontFamily = CairoFontFamily,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
            fontSize = 11.sp,
            color = if (isSelected) activeTextColor else Color(0xFF1E293B)
          )
        }
      }
    }
  }
}

/**
 * 5-Button Showcase Grid for the full dialog presentation matching the reference image style
 */
@Composable
fun ModernMathButtons2x2Grid(
  modifier: Modifier = Modifier,
  buttonSize: Dp = 100.dp,
  selectedSymbol: String? = null,
  onButtonClick: ((String) -> Unit)? = null
) {
  Box(
    modifier = modifier
      .background(Color.White, RoundedCornerShape(28.dp))
      .padding(18.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Row 1: + and −
      Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        ModernCalculatorButton(
          symbol = "+",
          size = buttonSize,
          isSelected = selectedSymbol == "+",
          onClick = { onButtonClick?.invoke("+") }
        )

        ModernCalculatorButton(
          symbol = "−",
          size = buttonSize,
          isSelected = selectedSymbol == "−",
          onClick = { onButtonClick?.invoke("−") }
        )
      }

      // Row 2: × and ÷
      Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        ModernCalculatorButton(
          symbol = "×",
          size = buttonSize,
          isSelected = selectedSymbol == "×",
          onClick = { onButtonClick?.invoke("×") }
        )

        ModernCalculatorButton(
          symbol = "÷",
          size = buttonSize,
          isSelected = selectedSymbol == "÷",
          onClick = { onButtonClick?.invoke("÷") }
        )
      }

      // Row 3: Comprehensive / Mixed Mode Button (زر شامل)
      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        ModernCalculatorButton(
          symbol = "🔀",
          size = buttonSize,
          isSelected = selectedSymbol == "🔀",
          onClick = { onButtonClick?.invoke("🔀") }
        )
      }
    }
  }
}

/**
 * Single Modern Calculator Button with luxury jewel-toned gradient,
 * specular edge highlight, and 3D geometric symbol.
 */
@Composable
fun ModernCalculatorButton(
  symbol: String,
  modifier: Modifier = Modifier,
  size: Dp = 120.dp,
  isSelected: Boolean = false,
  onClick: (() -> Unit)? = null
) {
  val cornerRadius = size * 0.28f
  val shape = RoundedCornerShape(cornerRadius)

  val alpha = if (isSelected) 1.0f else 0.88f

  // Distinct jewel-toned gradients and ambient shadows per operation
  val (gradientColors, spotColor) = when (symbol) {
    "+" -> listOf(
      Color(0xFF2563EB).copy(alpha = alpha), // Electric Blue
      Color(0xFF3B82F6).copy(alpha = alpha),
      Color(0xFF4F46E5).copy(alpha = alpha), // Royal Indigo
      Color(0xFF7C3AED).copy(alpha = alpha)  // Violet
    ) to Color(0x902563EB)
    "−" -> listOf(
      Color(0xFFE11D48).copy(alpha = alpha), // Sunset Rose
      Color(0xFFF43F5E).copy(alpha = alpha),
      Color(0xFFC026D3).copy(alpha = alpha), // Fuchsia
      Color(0xFF9333EA).copy(alpha = alpha)  // Magenta
    ) to Color(0x90E11D48)
    "×" -> listOf(
      Color(0xFF6366F1).copy(alpha = alpha), // Bright Violet
      Color(0xFF7C3AED).copy(alpha = alpha), // Royal Purple
      Color(0xFF4338CA).copy(alpha = alpha), // Indigo
      Color(0xFF312E81).copy(alpha = alpha)  // Deep Midnight Indigo
    ) to Color(0x907C3AED)
    "÷" -> listOf(
      Color(0xFF0D9488).copy(alpha = alpha), // Ocean Teal
      Color(0xFF14B8A6).copy(alpha = alpha),
      Color(0xFF059669).copy(alpha = alpha), // Emerald
      Color(0xFF10B981).copy(alpha = alpha)  // Mint Emerald
    ) to Color(0x900D9488)
    "🔀" -> listOf(
      Color(0xFFEA580C).copy(alpha = alpha), // Vivid Orange
      Color(0xFFF59E0B).copy(alpha = alpha), // Golden Sun
      Color(0xFFD97706).copy(alpha = alpha), // Warm Amber
      Color(0xFFB45309).copy(alpha = alpha)  // Honey Bronze
    ) to Color(0x90F59E0B)
    else -> listOf(
      Color(0xFF2563EB).copy(alpha = alpha),
      Color(0xFF4F46E5).copy(alpha = alpha),
      Color(0xFF7C3AED).copy(alpha = alpha),
      Color(0xFF8B5CF6).copy(alpha = alpha)
    ) to Color(0x907C3AED)
  }

  val gradientBrush = Brush.linearGradient(
    colors = gradientColors,
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
  )

  Surface(
    onClick = { onClick?.invoke() },
    enabled = (onClick != null),
    shape = shape,
    color = Color.Transparent,
    shadowElevation = if (isSelected) 14.dp else 4.dp,
    modifier = modifier
      .size(size)
      .scale(if (isSelected) 1.08f else 0.98f)
      .testTag("modern_calc_btn_$symbol")
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(gradientBrush)
        .border(
          width = if (isSelected) 2.5.dp else 1.2.dp,
          brush = Brush.linearGradient(
            colors = if (isSelected) {
              listOf(Color(0xFFFEF08A), Color.White) // Golden-white highlight for active operator
            } else {
              listOf(Color(0x80FFFFFF), Color(0x18FFFFFF))
            },
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
          ),
          shape = shape
        ),
      contentAlignment = Alignment.Center
    ) {
      Canvas(
        modifier = Modifier
          .fillMaxSize()
          .padding(size * 0.20f)
      ) {
        drawModernGeometricSymbol(symbol = symbol)
      }

      // Glowing dot indicator on the active button
      if (isSelected) {
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(top = 4.dp, end = 4.dp)
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFFFEF08A))
        )
      }
    }
  }
}

/**
 * Draws the 3D beveled white geometric mathematical symbols in the exact visual style
 * of the reference image's faceted ribbon 'X'.
 */
private fun DrawScope.drawModernGeometricSymbol(symbol: String) {
  val w = size.width
  val h = size.height
  val strokeWidth = w * 0.20f
  val primaryWhite = Color(0xFFFFFFFF)
  val shadowBevel = Color(0xFFE2E8F0)
  val deepFoldShadow = Color(0xFFCBD5E1)
  val highlightEdge = Color(0xFFFFFFFF)

  when (symbol) {
    "×" -> {
      drawFacetedRibbonX(w, h, strokeWidth, primaryWhite, shadowBevel, deepFoldShadow, highlightEdge)
    }
    "+" -> {
      drawFacetedRibbonPlus(w, h, strokeWidth, primaryWhite, shadowBevel, deepFoldShadow, highlightEdge)
    }
    "−" -> {
      drawFacetedRibbonMinus(w, h, strokeWidth, primaryWhite, shadowBevel, deepFoldShadow, highlightEdge)
    }
    "÷" -> {
      drawFacetedRibbonDivide(w, h, strokeWidth, primaryWhite, shadowBevel, deepFoldShadow, highlightEdge)
    }
    "🔀" -> {
      drawFacetedShuffle(w, h, strokeWidth, primaryWhite, shadowBevel, deepFoldShadow, highlightEdge)
    }
  }
}

/**
 * Exact replica of the reference photo's X:
 * Two crossing thick geometric ribbons with center interlocking fold and inner beveled facet
 */
private fun DrawScope.drawFacetedRibbonX(
  w: Float,
  h: Float,
  thickness: Float,
  white: Color,
  bevel: Color,
  deepShadow: Color,
  highlight: Color
) {
  val pad = w * 0.05f
  val left = pad
  val right = w - pad
  val top = pad
  val bottom = h - pad
  val cx = w * 0.5f
  val cy = h * 0.5f
  val halfThick = thickness * 0.5f

  // 1. Under-ribbon (Top-Left to Bottom-Right)
  val strokePath = Path().apply {
    moveTo(left, top)
    lineTo(right, bottom)
  }
  drawPath(
    path = strokePath,
    color = bevel,
    style = Stroke(width = thickness, cap = StrokeCap.Square, join = StrokeJoin.Miter)
  )
  drawPath(
    path = strokePath,
    color = white,
    style = Stroke(width = thickness * 0.82f, cap = StrokeCap.Square, join = StrokeJoin.Miter)
  )

  // 2. Interlocking overlap fold (Over-ribbon: Bottom-Left to Top-Right)
  val overPath = Path().apply {
    moveTo(left, bottom)
    lineTo(right, top)
  }
  drawCircle(
    color = deepShadow.copy(alpha = 0.45f),
    radius = thickness * 0.70f,
    center = Offset(cx, cy)
  )
  drawPath(
    path = overPath,
    color = bevel,
    style = Stroke(width = thickness, cap = StrokeCap.Square, join = StrokeJoin.Miter)
  )
  drawPath(
    path = overPath,
    color = white,
    style = Stroke(width = thickness * 0.82f, cap = StrokeCap.Square, join = StrokeJoin.Miter)
  )

  // 3. Faceted beveled edge highlight lines
  val highlightPath = Path().apply {
    moveTo(left, top - halfThick * 0.4f)
    lineTo(cx - halfThick * 0.3f, cy)
    moveTo(cx + halfThick * 0.3f, cy)
    lineTo(right, bottom - halfThick * 0.4f)
  }
  drawPath(
    path = highlightPath,
    color = highlight,
    style = Stroke(width = thickness * 0.16f, cap = StrokeCap.Round)
  )
}

/**
 * Faceted '+' with crossed vertical and horizontal bars and center beveled relief
 */
private fun DrawScope.drawFacetedRibbonPlus(
  w: Float,
  h: Float,
  thickness: Float,
  white: Color,
  bevel: Color,
  deepShadow: Color,
  highlight: Color
) {
  val pad = w * 0.05f
  val left = pad
  val right = w - pad
  val top = pad
  val bottom = h - pad
  val cx = w * 0.5f
  val cy = h * 0.5f
  val halfThick = thickness * 0.5f

  // 1. Horizontal Bar (Under)
  val hPath = Path().apply {
    moveTo(left, cy)
    lineTo(right, cy)
  }
  drawPath(hPath, color = bevel, style = Stroke(width = thickness, cap = StrokeCap.Square))
  drawPath(hPath, color = white, style = Stroke(width = thickness * 0.82f, cap = StrokeCap.Square))

  // Center drop shadow
  drawCircle(
    color = deepShadow.copy(alpha = 0.40f),
    radius = thickness * 0.65f,
    center = Offset(cx, cy)
  )

  // 2. Vertical Bar (Over)
  val vPath = Path().apply {
    moveTo(cx, top)
    lineTo(cx, bottom)
  }
  drawPath(vPath, color = bevel, style = Stroke(width = thickness, cap = StrokeCap.Square))
  drawPath(vPath, color = white, style = Stroke(width = thickness * 0.82f, cap = StrokeCap.Square))

  // Faceted highlight along top-left edge
  drawLine(
    color = highlight,
    start = Offset(cx - halfThick * 0.42f, top + thickness * 0.1f),
    end = Offset(cx - halfThick * 0.42f, bottom - thickness * 0.1f),
    strokeWidth = thickness * 0.16f,
    cap = StrokeCap.Round
  )
}

/**
 * Faceted '−' with thick horizontal bar and chamfered highlight
 */
private fun DrawScope.drawFacetedRibbonMinus(
  w: Float,
  h: Float,
  thickness: Float,
  white: Color,
  bevel: Color,
  deepShadow: Color,
  highlight: Color
) {
  val pad = w * 0.05f
  val left = pad
  val right = w - pad
  val cy = h * 0.5f
  val halfThick = thickness * 0.5f

  val hPath = Path().apply {
    moveTo(left, cy)
    lineTo(right, cy)
  }
  drawPath(hPath, color = bevel, style = Stroke(width = thickness, cap = StrokeCap.Square))
  drawPath(hPath, color = white, style = Stroke(width = thickness * 0.82f, cap = StrokeCap.Square))

  drawLine(
    color = highlight,
    start = Offset(left + thickness * 0.1f, cy - halfThick * 0.42f),
    end = Offset(right - thickness * 0.1f, cy - halfThick * 0.42f),
    strokeWidth = thickness * 0.16f,
    cap = StrokeCap.Round
  )
}

/**
 * Faceted '÷' with horizontal bar and two faceted circular dots
 */
private fun DrawScope.drawFacetedRibbonDivide(
  w: Float,
  h: Float,
  thickness: Float,
  white: Color,
  bevel: Color,
  deepShadow: Color,
  highlight: Color
) {
  val pad = w * 0.05f
  val left = pad
  val right = w - pad
  val cx = w * 0.5f
  val cy = h * 0.5f
  val halfThick = thickness * 0.5f

  // Center bar
  val hPath = Path().apply {
    moveTo(left, cy)
    lineTo(right, cy)
  }
  drawPath(hPath, color = bevel, style = Stroke(width = thickness, cap = StrokeCap.Square))
  drawPath(hPath, color = white, style = Stroke(width = thickness * 0.82f, cap = StrokeCap.Square))

  drawLine(
    color = highlight,
    start = Offset(left + thickness * 0.1f, cy - halfThick * 0.42f),
    end = Offset(right - thickness * 0.1f, cy - halfThick * 0.42f),
    strokeWidth = thickness * 0.16f,
    cap = StrokeCap.Round
  )

  // Top and Bottom Dots with beveled gradient
  val dotRadius = thickness * 0.55f
  val dotDistance = h * 0.32f

  // Top dot
  drawCircle(
    color = bevel,
    radius = dotRadius,
    center = Offset(cx, cy - dotDistance)
  )
  drawCircle(
    color = white,
    radius = dotRadius * 0.82f,
    center = Offset(cx, cy - dotDistance)
  )

  // Bottom dot
  drawCircle(
    color = bevel,
    radius = dotRadius,
    center = Offset(cx, cy + dotDistance)
  )
  drawCircle(
    color = white,
    radius = dotRadius * 0.82f,
    center = Offset(cx, cy + dotDistance)
  )
}

/**
 * Faceted '🔀' (Comprehensive / Mixed Mode) with 3D intertwined crossing ribbon arrows
 */
private fun DrawScope.drawFacetedShuffle(
  w: Float,
  h: Float,
  thickness: Float,
  white: Color,
  bevel: Color,
  deepShadow: Color,
  highlight: Color
) {
  val stroke = thickness * 0.70f
  val padX = w * 0.12f
  val padY = h * 0.22f
  val cx = w * 0.5f
  val cy = h * 0.5f
  val arrowHead = stroke * 1.4f

  // Path 1: Top-Left to Bottom-Right
  val path1 = Path().apply {
    moveTo(padX, padY)
    lineTo(cx - padX * 0.4f, padY)
    cubicTo(cx, padY, cx, h - padY, cx + padX * 0.4f, h - padY)
    lineTo(w - padX - arrowHead * 0.3f, h - padY)
  }

  // Path 2: Bottom-Left to Top-Right
  val path2 = Path().apply {
    moveTo(padX, h - padY)
    lineTo(cx - padX * 0.4f, h - padY)
    cubicTo(cx, h - padY, cx, padY, cx + padX * 0.4f, padY)
    lineTo(w - padX - arrowHead * 0.3f, padY)
  }

  // Draw Path 1 (Under)
  drawPath(path1, bevel, style = Stroke(width = stroke * 1.25f, cap = StrokeCap.Round, join = StrokeJoin.Round))
  drawPath(path1, white, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))

  // Crossing shadow
  drawCircle(deepShadow.copy(alpha = 0.40f), radius = stroke * 0.85f, center = Offset(cx, cy))

  // Draw Path 2 (Over)
  drawPath(path2, bevel, style = Stroke(width = stroke * 1.25f, cap = StrokeCap.Round, join = StrokeJoin.Round))
  drawPath(path2, white, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))

  // Arrowhead 1 (Top-Right pointing right)
  val arr1 = Path().apply {
    moveTo(w - padX - arrowHead * 0.8f, padY - arrowHead * 0.6f)
    lineTo(w - padX + arrowHead * 0.3f, padY)
    lineTo(w - padX - arrowHead * 0.8f, padY + arrowHead * 0.6f)
    close()
  }
  drawPath(arr1, bevel)
  drawPath(arr1, white)

  // Arrowhead 2 (Bottom-Right pointing right)
  val arr2 = Path().apply {
    moveTo(w - padX - arrowHead * 0.8f, h - padY - arrowHead * 0.6f)
    lineTo(w - padX + arrowHead * 0.3f, h - padY)
    lineTo(w - padX - arrowHead * 0.8f, h - padY + arrowHead * 0.6f)
    close()
  }
  drawPath(arr2, bevel)
  drawPath(arr2, white)
}
