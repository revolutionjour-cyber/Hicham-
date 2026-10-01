package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ModernKidsColorScheme = lightColorScheme(
  primary = BrandSkyBlue,
  onPrimary = TextWhite,
  primaryContainer = BrandSkyBlueBg,
  onPrimaryContainer = BrandSkyBlueDark,
  secondary = BrandAmber,
  onSecondary = TextDark,
  secondaryContainer = BrandAmberBg,
  onSecondaryContainer = BrandAmberDark,
  tertiary = BrandEmerald,
  onTertiary = TextWhite,
  background = CanvasBackground,
  onBackground = TextDark,
  surface = CanvasSurface,
  onSurface = TextDark,
  surfaceVariant = CanvasSurfaceSubtle,
  onSurfaceVariant = TextMedium,
  outline = CardBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = ModernKidsColorScheme,
    typography = Typography,
    content = content
  )
}
