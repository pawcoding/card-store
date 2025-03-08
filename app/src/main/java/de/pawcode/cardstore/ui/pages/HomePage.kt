package de.pawcode.cardstore.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.data.entities.Card
import de.pawcode.cardstore.data.model.CardViewModel
import de.pawcode.cardstore.ui.components.CardsListComponent
import de.pawcode.cardstore.ui.modals.AddCardModal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(cardViewModel: CardViewModel) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val scope = rememberCoroutineScope()
    var isAddCardModalVisible by rememberSaveable { mutableStateOf(false) }

    val showCardDeleteDialog = remember { mutableStateOf(false) }
    var cardToModify: Card? = null

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearchBarVisible by rememberSaveable { mutableStateOf(false) }

    val cards by cardViewModel.allCards.collectAsState(initial = emptyList())
    val filteredCards = if (searchQuery.isEmpty()) cards else cards.filter {
        it.store.contains(
            searchQuery, ignoreCase = true
        )
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            if (isSearchBarVisible) {
                OutlinedTextField(value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search cards...") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    })
            } else {
                Text("Card Store")
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ), actions = {
            if (isSearchBarVisible) {
                IconButton(onClick = { isSearchBarVisible = false; searchQuery = "" }) {
                    Icon(Icons.Default.Close, contentDescription = "Close search")
                }
            } else {
                IconButton(onClick = { isSearchBarVisible = true }) {
                    Icon(Icons.Default.Search, contentDescription = "Search cards")
                }
            }
        })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(onClick = {
            isAddCardModalVisible = true
        },
            text = { Text("Add new card") },
            icon = { Icon(Icons.Default.Add, contentDescription = "Add new card") })
    }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardsListComponent(cards = filteredCards, onEditCard = {
                cardToModify = it
                isAddCardModalVisible = true
            }, onDeleteCard = {
                cardToModify = it
                showCardDeleteDialog.value = true
            })
        }

        if (isAddCardModalVisible) {
            ModalBottomSheet(
                sheetState = sheetState, onDismissRequest = {
                    isAddCardModalVisible = false
                }, modifier = Modifier.safeDrawingPadding()
            ) {
                AddCardModal(card = cardToModify, onAddCard = { card ->
                    scope.launch {
                        if (cardToModify != null) {
                            cardViewModel.updateCard(card)
                        } else {
                            cardViewModel.insertCard(card)
                        }
                        cardToModify = null
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            isAddCardModalVisible = false
                        }
                    }
                })
            }
        }

        when {
            showCardDeleteDialog.value -> {
                AlertDialog(title = { Text("Delete card") },
                    text = { Text("Are you sure you want to delete this card? It can not be restored.") },
                    onDismissRequest = {
                        showCardDeleteDialog.value = false
                        cardToModify = null
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val card = cardToModify
                                ?: throw IllegalArgumentException("Card to delete is null")
                            scope.launch {
                                cardViewModel.deleteCard(card)
                            }.invokeOnCompletion {
                                cardToModify = null
                                showCardDeleteDialog.value = false
                            }
                        }) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showCardDeleteDialog.value = false
                            cardToModify = null
                        }) {
                            Text("Cancel")
                        }
                    })
            }
        }
    }
}