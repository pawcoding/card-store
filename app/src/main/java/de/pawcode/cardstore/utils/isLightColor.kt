package de.pawcode.cardstore.utils

import androidx.compose.ui.graphics.Color

fun isLightColor(color: Color): Boolean {
  return (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) > 0.5
}
