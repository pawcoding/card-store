package de.pawcode.cardstore.ui.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.lightspark.composeqr.QrCodeView
import de.pawcode.cardstore.data.database.CardEntity
import de.pawcode.cardstore.data.database.EXAMPLE_CARD

@Composable
fun ViewCardSheet(card: CardEntity) {
    val color = Color(card.color.toColorInt())
    val isLightColor = (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) > 0.5

    Column(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .clip(MaterialTheme.shapes.large)
            .fillMaxWidth()
            .background(color)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = card.storeName,
            style = MaterialTheme.typography.headlineMedium,
            color = if (isLightColor) Color.Black else Color.White
        )

        HorizontalDivider(
            color = if (isLightColor) Color(0, 0, 0, 128) else Color(255, 255, 255, 128)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
                .background(Color.White)
                .padding(12.dp)
        ) {
            QrCodeView(
                data = card.cardNumber, modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }

        Text(
            text = card.cardNumber, color = if (isLightColor) Color.Black else Color.White
        )
    }
}

@Preview
@Composable
fun PreviewViewCardSheet() {
    ViewCardSheet(EXAMPLE_CARD)
}
