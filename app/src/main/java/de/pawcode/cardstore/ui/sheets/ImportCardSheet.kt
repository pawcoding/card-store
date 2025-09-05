package de.pawcode.cardstore.ui.sheets

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_CARD
import de.pawcode.cardstore.ui.components.CardComponent
import de.pawcode.cardstore.ui.utils.dpToPx

@Composable
fun ImportCardSheet(card: CardEntity, isUpdate: Boolean = false, onImport: () -> Unit) {
  val infiniteTransition = rememberInfiniteTransition(label = "hover")
  val rotation by
    infiniteTransition.animateFloat(
      initialValue = -10f,
      targetValue = 10f,
      animationSpec =
        infiniteRepeatable(
          animation = tween(5000, easing = EaseInOutCubic),
          repeatMode = RepeatMode.Reverse,
        ),
      label = "rotation",
    )

  Column(
    modifier =
      Modifier.padding(top = 32.dp, end = 16.dp, bottom = 16.dp, start = 16.dp).fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(20.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Text(
        text = stringResource(if (isUpdate) R.string.update_card_title else R.string.import_card_title),
        style = MaterialTheme.typography.headlineSmall,
      )
      Text(
        text = stringResource(if (isUpdate) R.string.update_card_description else R.string.import_card_description),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    Box(
      modifier =
        Modifier.scale(0.8f)
          .graphicsLayer(
            cameraDistance = 12.dp.dpToPx(),
            rotationY = rotation,
            rotationZ = rotation / 4,
            rotationX = 15f,
          )
    ) {
      CardComponent(card = card, onClick = { onImport() }, onLongPress = { onImport() })
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewImportCardSheet() {
  ImportCardSheet(card = EXAMPLE_CARD, isUpdate = false, onImport = {})
}
