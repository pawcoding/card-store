package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simonsickle.compose.barcodes.BarcodeType
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_CARD
import de.pawcode.cardstore.utils.isLightColor

@Composable
fun CardComponent(card: CardEntity, onClick: () -> Unit, onLongPress: () -> Unit) {
  val haptics = LocalHapticFeedback.current

  val color = Color(card.color)
  val isLightColor = isLightColor(color)

  val textColor = if (isLightColor) Color.Black else Color.White
  val transparentBackgroundColor = textColor.copy(alpha = 0.2f)

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
    shape = RoundedCornerShape(24.dp),
  ) {
    Column(
      verticalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp).fillMaxSize(),
    ) {
      Text(
        text = card.storeName,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        color = textColor,
      )

      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HorizontalDivider(
          color = transparentBackgroundColor,
          thickness = 2.dp,
        )

        Row(
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Text(
            text = card.cardNumber,
            style = MaterialTheme.typography.bodyLarge.copy(fontFeatureSettings = "tnum"),
            textAlign = TextAlign.Left,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, false).padding(end = 4.dp),
          )

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier =
              Modifier.background(
                  color = transparentBackgroundColor,
                  shape = RoundedCornerShape(8.dp),
                )
                .padding(vertical = 3.dp, horizontal = 4.dp),
          ) {
            Icon(
              painterResource(
                if (card.barcodeFormat == BarcodeType.QR_CODE) R.drawable.qr_code_solid
                else R.drawable.barcode_solid
              ),
              contentDescription = null,
              tint = textColor,
            )

            Text(
              text = card.barcodeFormat.name.replace("_", ""),
              style = MaterialTheme.typography.bodyMedium,
              color = textColor,
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun PreviewCardComponent() {
  CardComponent(
    card =
      EXAMPLE_CARD.copy(
        cardNumber =
          "https://stackoverflow.com/questions/70277204/how-to-resize-an-item-in-a-compose-column-depending-on-another-item"
      ),
    onClick = {},
    onLongPress = {},
  )
}

@Preview
@Composable
fun BarcodePreviewCardComponent() {
  CardComponent(
    card = EXAMPLE_CARD.copy(barcodeFormat = BarcodeType.EAN_13),
    onClick = {},
    onLongPress = {},
  )
}
