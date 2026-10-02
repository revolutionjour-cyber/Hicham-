package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppLanguage
import com.example.model.LanguageStrings
import com.example.ui.theme.CairoFontFamily

@Composable
fun LanguageSelectionDialog(
  currentLanguage: AppLanguage,
  onSelectLanguage: (AppLanguage) -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(26.dp),
      color = Color.White,
      shadowElevation = 16.dp,
      border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE2E8F0)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .testTag("language_selection_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = LanguageStrings.getLanguageDialogTitle(currentLanguage),
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color(0xFF1E293B)
          )

          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(CircleShape)
              .background(Color(0xFFF1F5F9))
              .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Color(0xFF64748B),
              modifier = Modifier.size(18.dp)
            )
          }
        }

        // Language Options List
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          AppLanguage.values().forEach { lang ->
            val isSelected = lang == currentLanguage

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .shadow(if (isSelected) 4.dp else 1.dp, RoundedCornerShape(18.dp), spotColor = Color(0x30000000))
                .clip(RoundedCornerShape(18.dp))
                .background(
                  if (isSelected) {
                    Brush.linearGradient(
                      colors = listOf(Color(0xFFEFF6FF), Color(0xFFDBEAFE))
                    )
                  } else {
                    Brush.linearGradient(
                      colors = listOf(Color(0xFFF8FAFC), Color.White)
                    )
                  }
                )
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) Color(0xFF3B82F6) else Color(0xFFE2E8F0),
                  shape = RoundedCornerShape(18.dp)
                )
                .clickable {
                  onSelectLanguage(lang)
                  onDismiss()
                }
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .testTag("lang_option_${lang.name}")
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                  Text(
                    text = lang.flag,
                    fontSize = 24.sp
                  )
                  Column {
                    Text(
                      text = lang.displayName,
                      fontFamily = CairoFontFamily,
                      fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                      fontSize = 15.sp,
                      color = if (isSelected) Color(0xFF1D4ED8) else Color(0xFF1E293B)
                    )
                    Text(
                      text = when (lang) {
                        AppLanguage.MOROCCAN_ARABIC -> "المغرب (الدارجة)"
                        AppLanguage.FRENCH -> "Français (France / Maghreb)"
                        AppLanguage.ENGLISH -> "English (UK / US)"
                      },
                      fontFamily = CairoFontFamily,
                      fontSize = 11.sp,
                      color = Color(0xFF64748B)
                    )
                  }
                }

                if (isSelected) {
                  Box(
                    modifier = Modifier
                      .size(26.dp)
                      .clip(CircleShape)
                      .background(Color(0xFF3B82F6)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = "Selected",
                      tint = Color.White,
                      modifier = Modifier.size(15.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
