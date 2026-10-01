package com.example.ui.components

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File

/**
 * SunnyGardenBackground:
 * Displays the boy reading under the tree in the sunny meadow.
 * 1. Checks for user-provided image (kid_reading_bg.jpg / jpeg / png) in assets, drawable, or files.
 * 2. If present, renders it with high quality and a soft legibility scrim.
 * 3. Gracefully provides a rich vector landscape (sunny blue sky, fluffy clouds, green hills, tree)
 *    matching the exact visual palette of the uploaded reference photo.
 */
@Composable
fun SunnyGardenBackground(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  val context = LocalContext.current
  val imageSource = remember(context) { findBackgroundImageSource(context) }

  Box(modifier = modifier.fillMaxSize()) {
    if (imageSource != null) {
      // 1. Display user-provided image
      AsyncImage(
        model = ImageRequest.Builder(context)
          .data(imageSource)
          .crossfade(true)
          .build(),
        contentDescription = "خلفية الطبيعة وقراءة الكتاب تحت الشجرة",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    } else {
      // 2. Beautiful native Canvas recreation matching the sunny meadow and tree
      Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Sunny Sky Gradient (Azure Blue to Soft Warm Cyan)
        drawRect(
          brush = Brush.verticalGradient(
            colors = listOf(
              Color(0xFF38BDF8), // Sky Blue
              Color(0xFF7DD3FC),
              Color(0xFFBAE6FD),
              Color(0xFFE0F2FE)
            ),
            startY = 0f,
            endY = h * 0.65f
          )
        )

        // Soft Clouds
        drawCircle(
          color = Color.White.copy(alpha = 0.85f),
          radius = w * 0.22f,
          center = Offset(w * 0.25f, h * 0.16f)
        )
        drawCircle(
          color = Color.White.copy(alpha = 0.90f),
          radius = w * 0.18f,
          center = Offset(w * 0.45f, h * 0.17f)
        )
        drawCircle(
          color = Color.White.copy(alpha = 0.85f),
          radius = w * 0.16f,
          center = Offset(w * 0.12f, h * 0.20f)
        )

        // Far Rolling Green Hills
        val farHill = Path().apply {
          moveTo(0f, h * 0.55f)
          cubicTo(w * 0.35f, h * 0.48f, w * 0.65f, h * 0.58f, w, h * 0.50f)
          lineTo(w, h)
          lineTo(0f, h)
          close()
        }
        drawPath(
          path = farHill,
          brush = Brush.verticalGradient(
            listOf(Color(0xFF86EFAC), Color(0xFF4ADE80)),
            startY = h * 0.48f,
            endY = h * 0.70f
          )
        )

        // Lush Tree Canopy on the Upper Right (framing the view)
        drawCircle(
          brush = Brush.radialGradient(
            listOf(Color(0xFF84CC16), Color(0xFF4D7C0F)),
            center = Offset(w * 0.85f, h * 0.10f),
            radius = w * 0.55f
          ),
          radius = w * 0.55f,
          center = Offset(w * 0.85f, h * 0.10f)
        )
        drawCircle(
          brush = Brush.radialGradient(
            listOf(Color(0xFFA3E635), Color(0xFF65A30D)),
            center = Offset(w * 0.40f, h * 0.05f),
            radius = w * 0.38f
          ),
          radius = w * 0.38f,
          center = Offset(w * 0.40f, h * 0.05f)
        )

        // Tree Trunk on the Right
        val trunk = Path().apply {
          moveTo(w * 0.75f, 0f)
          cubicTo(w * 0.78f, h * 0.30f, w * 0.82f, h * 0.60f, w * 0.95f, h)
          lineTo(w, h)
          lineTo(w, 0f)
          close()
        }
        drawPath(
          path = trunk,
          brush = Brush.horizontalGradient(
            listOf(Color(0xFFB45309), Color(0xFF78350F)),
            startX = w * 0.75f,
            endX = w
          )
        )

        // Foreground Grassy Meadow
        val nearHill = Path().apply {
          moveTo(0f, h * 0.68f)
          cubicTo(w * 0.45f, h * 0.64f, w * 0.75f, h * 0.72f, w, h * 0.66f)
          lineTo(w, h)
          lineTo(0f, h)
          close()
        }
        drawPath(
          path = nearHill,
          brush = Brush.verticalGradient(
            listOf(Color(0xFF22C55E), Color(0xFF15803D)),
            startY = h * 0.64f,
            endY = h
          )
        )
      }
    }

    // Soft readability scrim overlay so the chalkboard, 3D buttons, and text remain 100% legible
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color(0x35FFFFFF), // Light tint on top
              Color(0x25FFFFFF),
              Color(0x40FFFFFF)
            )
          )
        )
    )

    // Foreground UI content
    content()
  }
}

/**
 * Searches for background image in assets or app files directory.
 */
private fun findBackgroundImageSource(context: Context): Any? {
  val candidates = listOf(
    "kid_reading_bg.jpg",
    "kid_reading_bg.jpeg",
    "kid_reading_bg.png",
    "background.jpg",
    "background.jpeg",
    "background.png"
  )

  // 1. Check in assets/
  try {
    val assetList = context.assets.list("") ?: emptyArray()
    for (cand in candidates) {
      if (assetList.contains(cand)) {
        return "file:///android_asset/$cand"
      }
    }
  } catch (_: Throwable) {}

  // 2. Check in filesDir/
  try {
    for (cand in candidates) {
      val f = File(context.filesDir, cand)
      if (f.exists() && f.length() > 0) {
        return f
      }
    }
  } catch (_: Throwable) {}

  // 3. Check in res/drawable/ via identifier
  val resId = context.resources.getIdentifier("kid_reading_bg", "drawable", context.packageName)
  if (resId != 0) {
    return resId
  }

  return null
}
