package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_CARD
import de.pawcode.cardstore.utils.isLightColor

@Composable
fun CardComponent(card: CardEntity, onClick: () -> Unit, onLongPress: () -> Unit) {
  val haptics = LocalHapticFeedback.current

  val color = Color(card.color)
  val isLightColor = isLightColor(color)

  ElevatedCard(
    modifier =
      Modifier.fillMaxWidth()
        .aspectRatio(1.586f)
        .combinedClickable(
          onClick = { onClick() },
          onLongClick = {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            onLongPress()
          },
        ),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    colors = CardDefaults.cardColors(containerColor = color),
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(8.dp).fillMaxSize(),
    ) {
      Text(
        text = card.storeName,
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        color = if (isLightColor) Color.Black else Color.White,
      )
    }
  }
}

@Preview
@Composable
fun PreviewCardComponent() {
  CardComponent(card = EXAMPLE_CARD, onClick = {}, onLongPress = {})
}
