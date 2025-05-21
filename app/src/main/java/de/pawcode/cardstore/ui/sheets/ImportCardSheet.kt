package de.pawcode.cardstore.ui.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_CARD
import de.pawcode.cardstore.ui.components.CardComponent

@Composable
fun ImportCardSheet(card: CardEntity, onImport: () -> Unit) {
  Column(
    modifier =
      Modifier.padding(top = 32.dp, end = 16.dp, bottom = 16.dp, start = 16.dp).fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(20.dp),
  ) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Text(
        text = stringResource(R.string.import_card_title),
        style = MaterialTheme.typography.headlineSmall,
      )
      Text(
        text = stringResource(R.string.import_card_description),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    Box(modifier = Modifier.scale(0.8f)) {
      CardComponent(card = card, onClick = { onImport() }, onLongPress = { onImport() })
    }
  }
}

@Preview
@Composable
fun PreviewImportCardSheet() {
  ImportCardSheet(card = EXAMPLE_CARD, onImport = {})
}
