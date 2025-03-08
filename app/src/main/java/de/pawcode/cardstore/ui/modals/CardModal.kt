package de.pawcode.cardstore.ui.modals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lightspark.composeqr.QrCodeView
import de.pawcode.cardstore.data.entities.Card
import de.pawcode.cardstore.data.entities.EXAMPLE_CARD

@Composable
fun CardModal(card: Card) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = card.store,
            style = MaterialTheme.typography.headlineSmall,
        )

        HorizontalDivider()

        QrCodeView(
            data = card.cardNumber,
            modifier = Modifier
                .widthIn(max = 400.dp)
                .aspectRatio(1f)
        )
    }

}

@Preview
@Composable
fun PreviewCardModal() {
    CardModal(EXAMPLE_CARD)
}