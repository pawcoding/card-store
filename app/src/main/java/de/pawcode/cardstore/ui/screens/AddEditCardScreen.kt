package de.pawcode.cardstore.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.data.database.emptyCard
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.navigation.Screen
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.EditCardForm
import de.pawcode.cardstore.ui.viewmodels.CardViewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditCardScreen(
    navController: NavController, cardId: String? = null, viewModel: CardViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()

    val initialCard by viewModel.getCardById(cardId).collectAsState(initial = emptyCard())

    var card by remember { mutableStateOf(initialCard ?: emptyCard()) }

    LaunchedEffect(initialCard) {
        card = initialCard ?: emptyCard()
    }

    val isValid by remember { derivedStateOf { card.storeName.isNotEmpty() && card.cardNumber.isNotEmpty() } }
    val hasChanges by remember {
        derivedStateOf {
            card.storeName != (initialCard?.storeName) || card.cardNumber != (initialCard?.cardNumber) || card.color != (initialCard?.color) || card.barcodeFormat != (initialCard?.barcodeFormat)
        }
    }

    Scaffold(topBar = {
        AppBar(
            title = if (cardId != null) "Edit card" else "Add card",
            navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
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

                    if (cardId != null) {
                        viewModel.updateCard(card)
                    } else {
                        viewModel.insertCard(card)
                        navController.popBackStack()
                        navController.navigate(Screen.AddEditCard.route + "?cardId=${card.id}")
                    }

                    SnackbarService.showSnackbar(
                        message = "Card ${if (cardId != null) "updated" else "saved"}",
                        scope = scope
                    )
                },
                text = {
                    if (cardId != null) {
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
            modifier = Modifier.padding(innerPadding)
        ) {
            EditCardForm(
                initialCard = card,
                onCardUpdate = { card = it }
            )
        }
    }
}
