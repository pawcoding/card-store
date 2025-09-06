package de.pawcode.cardstore.ui.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_CARD

@Composable
fun ShareCardSheet(card: CardEntity) {
  val deeplink =
    "https://cardstore.apps.pawcode.de/share-card?cardId=${card.cardId}&storeName=${card.storeName}&cardNumber=${card.cardNumber}&barcodeFormat=${card.barcodeFormat}&color=${card.color}"

  Column(
    modifier = Modifier.padding(16.dp).fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(20.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Text(
        text = stringResource(R.string.share_card_title),
        style = MaterialTheme.typography.headlineSmall,
      )
      Text(
        text = stringResource(R.string.share_card_description),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    Box(
      modifier =
        Modifier.fillMaxWidth(0.6f)
          .aspectRatio(1f)
          .clip(MaterialTheme.shapes.medium)
          .background(Color.White)
          .padding(4.dp)
    ) {
      Barcode(
        modifier = Modifier.fillMaxWidth(),
        resolutionFactor = 5,
        value = deeplink,
        type = BarcodeType.QR_CODE,
        width = 80.dp,
        height = 80.dp,
      )
    }
  }
}

@Preview
@Composable
fun PreviewShareCardSheet() {
  ShareCardSheet(card = EXAMPLE_CARD)
}
