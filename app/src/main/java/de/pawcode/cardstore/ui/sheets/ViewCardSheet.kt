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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simonsickle.compose.barcodes.Barcode
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_CARD
import de.pawcode.cardstore.ui.utils.calculateBarcodeAspectRatio
import de.pawcode.cardstore.ui.utils.isLightColor

@Composable
fun ViewCardSheet(card: CardEntity) {
    val aspectRatio by remember { mutableFloatStateOf(calculateBarcodeAspectRatio(card.barcodeFormat)) }
    val color by remember { mutableStateOf(Color(card.color)) }
    val isLightColor by remember { derivedStateOf { isLightColor(color) } }

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

        if (card.barcodeFormat.isValueValid(card.cardNumber)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(aspectRatio)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Barcode(
                    modifier = Modifier.fillMaxWidth(),
                    resolutionFactor = 10,
                    value = card.cardNumber,
                    type = card.barcodeFormat,
                )
            }
        } else {
            Text("Cannot display barcode")
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
