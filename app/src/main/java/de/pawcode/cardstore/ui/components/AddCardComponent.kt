package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R

@Composable
fun AddCardComponent(hasCards: Boolean, onClick: () -> Unit) {
  OutlinedCard(
    modifier = Modifier.fillMaxWidth().aspectRatio(1.586f).clickable { onClick() },
    colors =
      CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(8.dp).fillMaxSize(),
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Icon(
          painterResource(R.drawable.add_card),
          contentDescription =
            if (hasCards) stringResource(R.string.cards_new)
            else stringResource(R.string.cards_list_empty),
          modifier = Modifier.size(64.dp),
        )

        Text(
          text =
            if (hasCards) stringResource(R.string.cards_new)
            else stringResource(R.string.cards_list_empty),
          style = MaterialTheme.typography.headlineMedium,
          textAlign = TextAlign.Center,
        )
      }
    }
  }
}

@Preview
@Composable
fun PreviewAddCardComponent() {
  AddCardComponent(hasCards = true, onClick = {})
}

@Preview
@Composable
fun PreviewEmptyAddCardComponent() {
  AddCardComponent(hasCards = false, onClick = {})
}
