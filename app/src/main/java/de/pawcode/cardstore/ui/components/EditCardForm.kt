package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simonsickle.compose.barcodes.BarcodeType
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.emptyCard
import de.pawcode.cardstore.ui.dialogs.ColorPickerDialog
import de.pawcode.cardstore.ui.utils.isLightColor
import de.pawcode.cardstore.ui.utils.mapBarcodeFormat

@Composable
fun EditCardForm(
    initialCard: CardEntity? = null,
    onCardUpdate: (CardEntity) -> Unit
) {
    var showColorPicker by remember { mutableStateOf(false) }
    var showBarcodeScanner by remember { mutableStateOf(false) }

    var card by remember { mutableStateOf(initialCard?.copy() ?: emptyCard()) }
    val color by remember { derivedStateOf { Color(card.color) } }
    val isLightColor by remember { derivedStateOf { isLightColor(color) } }

    LaunchedEffect(initialCard) {
        card = initialCard?.copy() ?: emptyCard()
    }

    LaunchedEffect(card) {
        onCardUpdate(card)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = card.storeName,
            onValueChange = { card = card.copy(storeName = it) },
            label = { Text("Store name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrectEnabled = true,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        HorizontalDivider()

        OutlinedButton(
            onClick = { showBarcodeScanner = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Scan barcode")
        }

        OutlinedTextField(
            value = card.cardNumber,
            onValueChange = { card = card.copy(cardNumber = it) },
            label = { Text("Card number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Barcode Type:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (card.cardNumber != "" && !card.barcodeFormat.isValueValid(card.cardNumber)) {
                    Text(
                        text = "Invalid barcode format",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(
                    onClick = { expanded = true }
                ) {
                    Text(card.barcodeFormat.name)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    BarcodeType.entries.forEach { type ->
                        DropdownMenuItem(
                            onClick = {
                                card = card.copy(barcodeFormat = type)
                                expanded = false
                            },
                            text = { Text(type.name) }
                        )
                    }
                }
            }
        }

        HorizontalDivider()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Color:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(
                        MaterialTheme.shapes.medium
                    )
                    .background(color)
                    .clickable { showColorPicker = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Colorize,
                    contentDescription = "Pick a color",
                    tint = if (isLightColor) Color.Black else Color.White
                )
            }
        }
    }

    if (showColorPicker) {
        ColorPickerDialog(
            color = color,
            onDismiss = { newColor ->
                if (newColor != null) {
                    card = card.copy(color = newColor.toArgb())
                }
                showColorPicker = false
            }
        )
    }

    if (showBarcodeScanner) {
        BarcodeScanner(
            onBarcodeDetected = { barcode ->
                card = card.copy(
                    cardNumber = barcode.rawValue ?: "",
                    barcodeFormat = mapBarcodeFormat(barcode.format)
                )

                showBarcodeScanner = false
            },
            onCancel = { showBarcodeScanner = false }
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewEditCardForm() {
    EditCardForm(onCardUpdate = {})
}