package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.KidGameState
import com.example.ui.components.CartoonAstronautMascot
import com.example.ui.components.CuteRocket
import com.example.ui.components.IllustratedGem
import com.example.ui.components.IllustratedRobot
import com.example.ui.components.MascotMood
import com.example.ui.theme.BrandEmerald
import com.example.ui.theme.BrandRose
import com.example.ui.theme.BrandSkyBlue
import com.example.ui.theme.BrandSkyBlueBg
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.CardBorder
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMedium
import com.example.ui.theme.TextMuted

@Composable
fun ProfileSettingsTab(
  gameState: KidGameState,
  isSoundEnabled: Boolean,
  onToggleSound: () -> Unit,
  onUpdateProfile: (name: String, avatarIndex: Int) -> Unit,
  onResetProgress: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showResetDialog by remember { mutableStateOf(false) }
  var isEditingName by remember { mutableStateOf(false) }
  var tempName by remember { mutableStateOf(gameState.playerName) }

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Child Profile Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(8.dp, RoundedCornerShape(32.dp), spotColor = Color(0x3038BDF8))
        .clip(RoundedCornerShape(32.dp))
        .background(Color.White)
        .border(2.dp, CardBorder, RoundedCornerShape(32.dp))
        .padding(20.dp)
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Active Avatar preview
        Box(
          modifier = Modifier
            .size(90.dp)
            .shadow(4.dp, CircleShape, spotColor = BrandSkyBlue)
            .clip(CircleShape)
            .background(BrandSkyBlueBg)
            .border(3.dp, BrandSkyBlue, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          when (gameState.selectedAvatar) {
            0 -> CartoonAstronautMascot(sizeDp = 80.dp, mood = MascotMood.IDLE)
            1 -> IllustratedRobot(sizeDp = 60.dp)
            2 -> CuteRocket(sizeDp = 60.dp)
            else -> IllustratedGem(sizeDp = 50.dp)
          }
        }

        // Name & Edit
        if (isEditingName) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = tempName,
              onValueChange = { tempName = it },
              label = { Text("اسم البطل", fontFamily = CairoFontFamily) },
              modifier = Modifier.weight(1f),
              singleLine = true
            )
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(BrandEmerald)
                .clickable {
                  if (tempName.isNotBlank()) {
                    onUpdateProfile(tempName, gameState.selectedAvatar)
                  }
                  isEditingName = false
                },
              contentAlignment = Alignment.Center
            ) {
              Icon(imageVector = Icons.Default.Check, contentDescription = "حفظ", tint = Color.White)
            }
          }
        } else {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = gameState.playerName,
              fontFamily = CairoFontFamily,
              fontWeight = FontWeight.Black,
              fontSize = 22.sp,
              color = TextDark
            )
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(BrandSkyBlueBg)
                .clickable { isEditingName = true }
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = "تعديل",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = BrandSkyBlue
              )
            }
          }
        }

        // Avatar Skin Options (4 custom illustrated skins)
        Text(
          text = "اختر شخصيتك المفضلة:",
          fontFamily = CairoFontFamily,
          fontSize = 13.sp,
          color = TextMuted
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          for (avatarIdx in 0..3) {
            val isSelected = gameState.selectedAvatar == avatarIdx
            Box(
              modifier = Modifier
                .size(56.dp)
                .shadow(if (isSelected) 6.dp else 2.dp, CircleShape)
                .clip(CircleShape)
                .background(if (isSelected) BrandSkyBlueBg else Color(0xFFF8FAFC))
                .border(
                  width = if (isSelected) 3.dp else 1.5.dp,
                  color = if (isSelected) BrandSkyBlue else CardBorder,
                  shape = CircleShape
                )
                .clickable {
                  onUpdateProfile(gameState.playerName, avatarIdx)
                },
              contentAlignment = Alignment.Center
            ) {
              when (avatarIdx) {
                0 -> CartoonAstronautMascot(sizeDp = 46.dp, mood = MascotMood.IDLE)
                1 -> IllustratedRobot(sizeDp = 34.dp)
                2 -> CuteRocket(sizeDp = 34.dp)
                else -> IllustratedGem(sizeDp = 28.dp)
              }
            }
          }
        }
      }
    }

    // 2. Settings Controls Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(6.dp, RoundedCornerShape(26.dp), spotColor = Color(0x20000000))
        .clip(RoundedCornerShape(26.dp))
        .background(Color.White)
        .border(1.5.dp, CardBorder, RoundedCornerShape(26.dp))
        .padding(18.dp)
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Text(
          text = "إعدادات اللعبة والتعلم",
          fontFamily = CairoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = TextDark
        )

        // Sound Toggle Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(
              imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
              contentDescription = "صوت",
              tint = if (isSoundEnabled) BrandEmerald else TextMuted
            )
            Column {
              Text(
                text = "المؤثرات الصوتية والموسيقى",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextDark
              )
              Text(
                text = if (isSoundEnabled) "الصوت مفعّل ويشجع الطفل" else "الصوت مكتوم",
                fontFamily = CairoFontFamily,
                fontSize = 12.sp,
                color = TextMuted
              )
            }
          }

          Switch(
            checked = isSoundEnabled,
            onCheckedChange = { onToggleSound() },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.White,
              checkedTrackColor = BrandEmerald
            ),
            modifier = Modifier.testTag("settings_sound_switch")
          )
        }
      }
    }

    // 3. Reset Progress Button (Encouraging child to start fresh if desired)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .background(Color(0xFFFFF1F2))
        .border(1.5.dp, Color(0xFFFECDD3), RoundedCornerShape(20.dp))
        .clickable { showResetDialog = true }
        .padding(horizontal = 18.dp, vertical = 14.dp),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "إعادة ضبط التقدم والبدء من جديد",
        fontFamily = CairoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = BrandRose
      )
    }

    if (showResetDialog) {
      AlertDialog(
        onDismissRequest = { showResetDialog = false },
        title = {
          Text(
            text = "تأكيد إعادة الضبط",
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold
          )
        },
        text = {
          Text(
            text = "هل أنت متأكد من رغبتك في إعادة ضبط جميع النجوم والمراحل المنجزة؟",
            fontFamily = CairoFontFamily
          )
        },
        confirmButton = {
          TextButton(onClick = {
            onResetProgress()
            showResetDialog = false
          }) {
            Text("نعم، ابدأ من جديد", fontFamily = CairoFontFamily, color = BrandRose)
          }
        },
        dismissButton = {
          TextButton(onClick = { showResetDialog = false }) {
            Text("إلغاء", fontFamily = CairoFontFamily)
          }
        }
      )
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}
