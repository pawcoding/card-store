package de.pawcode.cardstore.ui.modals

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.data.entities.Card
import de.pawcode.cardstore.data.entities.EXAMPLE_CARD
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddCardModal(
    card: Card? = null, onAddCard: (card: Card) -> Unit
) {
    val hasCard = card != null

    var store by remember { mutableStateOf(card?.store ?: "") }
    var cardNumber by remember { mutableStateOf(card?.cardNumber ?: "") }
    var color by remember {
        mutableStateOf(card?.color?.let {
            Color(
                android.graphics.Color.parseColor(
                    it
                )
            )
        } ?: Color.White)
    }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var isColorPicketModalOpen by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (hasCard) "Edit card" else "Add a new card",
                style = MaterialTheme.typography.headlineSmall,
            )

            Button(
                onClick = {
                    onAddCard(
                        Card(
                            id = card?.id ?: Uuid.random().toString(),
                            store = store,
                            cardNumber = cardNumber,
                            color = String.format("#%06X", 0xFFFFFF and color.toArgb())
                        )
                    )
                },
                enabled = store.isNotEmpty() && cardNumber.isNotEmpty(),
                contentPadding = PaddingValues(16.dp, 8.dp)
            ) {
                Text(
                    text = if (hasCard) "Save" else "Add",
                )
            }
        }

        HorizontalDivider()

        OutlinedTextField(
            value = store,
            onValueChange = { store = it },
            label = { Text("Store") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrectEnabled = true,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Card Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                colors = ButtonColors(
                    containerColor = color,
                    contentColor = color,
                    disabledContainerColor = color,
                    disabledContentColor = color
                ),
                modifier = Modifier.size(40.dp),
                onClick = {
                    isColorPicketModalOpen = true
                },
            ) {}

            OutlinedButton(onClick = {
                isColorPicketModalOpen = true
            }) {
                Text("Pick a color")
            }
        }
    }

    if (isColorPicketModalOpen) {
        ModalBottomSheet(
            sheetState = sheetState, onDismissRequest = {
                isColorPicketModalOpen = false
            }, modifier = Modifier.safeDrawingPadding()
        ) {
            ColorPickerModal(color = color, onColorChange = { newColor ->
                scope.launch {
                    color = newColor
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        isColorPicketModalOpen = false
                    }
                }
            })
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun PreviewAddCardModal() {
    AddCardModal(
        card = EXAMPLE_CARD,
        onAddCard = {
            /* no-op */
        }
    )
}