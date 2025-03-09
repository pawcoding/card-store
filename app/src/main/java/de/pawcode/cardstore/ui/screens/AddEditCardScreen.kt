package de.pawcode.cardstore.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.data.database.CardEntity
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.dialogs.ColorPickerDialog
import de.pawcode.cardstore.ui.viewmodels.CardViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AddEditCardScreen(
    navController: NavController, cardId: String? = null, viewModel: CardViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    var colorPickerOpen by remember { mutableStateOf(false) }

    val card = cardId?.let { viewModel.getCardById(it) }?.collectAsState(initial = null)?.value

    var storeName by remember { mutableStateOf(card?.storeName ?: "") }
    var cardNumber by remember { mutableStateOf(card?.cardNumber ?: "") }
    var color by remember { mutableStateOf(card?.color ?: "#FFFFFF") }

    val isValid by remember { derivedStateOf { storeName.isNotEmpty() && cardNumber.isNotEmpty() } }
    val hasChanges by remember {
        derivedStateOf {
            storeName != (card?.storeName ?: "") || cardNumber != (card?.cardNumber
                ?: "") || color != (card?.color ?: "#FFFFFF")
        }
    }

    Scaffold(topBar = {
        AppBar(
            title = if (card != null) "Edit card" else "Add card", navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            })
    }, floatingActionButton = {
        AnimatedVisibility(
            visible = hasChanges,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    if (!isValid) {
                        return@ExtendedFloatingActionButton
                    }

                    if (card != null) {
                        viewModel.updateCard(
                            card.copy(
                                storeName = storeName, cardNumber = cardNumber
                            )
                        )
                    } else {
                        viewModel.insertCard(
                            CardEntity(
                                id = Uuid.random().toString(),
                                storeName = storeName,
                                cardNumber = cardNumber,
                                barcodeFormat = 0,
                                color = color,
                            )
                        )
                    }

                    SnackbarService.showSnackbar(
                        message = "Card ${if (card != null) "updated" else "saved"}", scope = scope
                    )
                },
                text = {
                    if (card != null) {
                        Text("Update")
                    } else {
                        Text("Save")
                    }
                },
                icon = { Icon(Icons.Filled.Save, contentDescription = "Save card") },
                containerColor = if (isValid) MaterialTheme.colorScheme.primary else Color.Gray,
                contentColor = if (isValid) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.38f
                )
            )
        }
    }) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            OutlinedTextField(
                value = storeName,
                onValueChange = {
                    storeName = it
                },
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
                onValueChange = {
                    cardNumber = it
                },
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
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(
                            Color(color.toColorInt())
                        )
                        .clip(MaterialTheme.shapes.medium)
                ) {}

                FilledTonalButton(
                    onClick = {
                        colorPickerOpen = true
                    }) {
                    Text("Pick a color")
                }
            }
        }
    }

    when {
        colorPickerOpen -> {
            ColorPickerDialog(
                color = Color(color.toColorInt()), onDismiss = { result ->
                    if (result != null) {
                        color = String.format("#%06X", 0xFFFFFF and result.toArgb())
                    }
                    colorPickerOpen = false
                })
        }
    }
}