package de.pawcode.cardstore.ui.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_CARD
import de.pawcode.cardstore.ui.components.CardComponent

@Composable
fun ImportCardSheet(card: CardEntity, onImport: () -> Unit, onCancel: () -> Unit) {
  Column(
    modifier = Modifier.padding(16.dp).fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      TextButton(onClick = { onCancel() }) { Text("Cancel") }

      TextButton(onClick = { onImport() }) { Text("Import card") }
    }

    CardComponent(card = card, onClick = {}, onLongPress = {})
  }
}

@Preview
@Composable
fun PreviewImportCardSheet() {
  ImportCardSheet(card = EXAMPLE_CARD, onImport = {}, onCancel = {})
}
