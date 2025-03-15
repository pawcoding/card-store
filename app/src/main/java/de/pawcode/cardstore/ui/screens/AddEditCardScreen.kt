package de.pawcode.cardstore.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
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
import de.pawcode.cardstore.data.database.classes.emptyCardWithLabels
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.EditCardForm
import de.pawcode.cardstore.ui.viewmodels.CardViewModel
import de.pawcode.cardstore.utils.classifyLabelsForUpdate
import de.pawcode.cardstore.utils.hasCardChanged
import de.pawcode.cardstore.utils.isCardValid
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditCardScreen(
    navController: NavController, cardId: String? = null, viewModel: CardViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()

    val labels by viewModel.allLabels.collectAsState(initial = emptyList())
    val initialCard by viewModel.getCardById(cardId).collectAsState(initial = null)
    var card by remember { mutableStateOf(initialCard ?: emptyCardWithLabels()) }

    LaunchedEffect(initialCard) {
        card = initialCard ?: emptyCardWithLabels()
    }

    val isValid by remember { derivedStateOf { isCardValid(card.card) } }
    val hasChanges by remember {
        derivedStateOf { initialCard == null || hasCardChanged(initialCard!!, card) }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            AppBar(
                title = if (cardId != null) "Edit card" else "Add card",
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
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

                        if (initialCard != null) {
                            viewModel.updateCard(card.card)

                            val (labelsToAdd, labelsToRemove) = classifyLabelsForUpdate(
                                initialCard!!.labels,
                                card.labels
                            )
                            viewModel.removeLabelsFromCard(
                                initialCard!!.card.cardId,
                                labelsToRemove
                            )
                            viewModel.addLabelsToCard(initialCard!!.card.cardId, labelsToAdd)
                        } else {
                            viewModel.insertCard(card.card)
                            viewModel.addLabelsToCard(
                                card.card.cardId,
                                card.labels.map { it.labelId })
                        }

                        navController.popBackStack()

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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            EditCardForm(
                initialCard = card,
                labels = labels,
                onCardUpdate = { card = it }
            )
        }
    }
}
