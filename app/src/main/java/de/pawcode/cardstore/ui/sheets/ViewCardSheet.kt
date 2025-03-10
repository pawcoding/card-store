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
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType
import de.pawcode.cardstore.data.database.CardEntity
import de.pawcode.cardstore.data.database.EXAMPLE_CARD
import de.pawcode.cardstore.ui.utils.isLightColor

@Composable
fun ViewCardSheet(card: CardEntity) {
    val color = Color(card.color.toColorInt())
    val isLightColor = isLightColor(color)

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
                    .aspectRatio(calculateBarcodeAspectRatio(card.barcodeFormat))
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

fun calculateBarcodeAspectRatio(barcodeType: BarcodeType): Float {
    return when (barcodeType) {
        BarcodeType.EAN_8 -> 3.0f / 2.0f
        BarcodeType.UPC_E -> 3.0f / 2.0f
        BarcodeType.EAN_13 -> 4.0f / 3.0f
        BarcodeType.UPC_A -> 4.0f / 3.0f
        BarcodeType.QR_CODE -> 1.0f
        BarcodeType.CODE_39 -> 3.0f / 1.0f
        BarcodeType.CODE_93 -> 3.0f / 1.0f
        BarcodeType.CODE_128 -> 3.0f / 1.0f
        BarcodeType.ITF -> 3.0f / 1.0f
        BarcodeType.PDF_417 -> 4.0f / 1.0f
        BarcodeType.CODABAR -> 3.0f / 1.0f
        BarcodeType.DATA_MATRIX -> 1.0f
        BarcodeType.AZTEC -> 1.0f
    }
}

@Preview
@Composable
fun PreviewViewCardSheet() {
    ViewCardSheet(EXAMPLE_CARD)
}
