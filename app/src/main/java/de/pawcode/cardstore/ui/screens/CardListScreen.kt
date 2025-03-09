package de.pawcode.cardstore.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.data.database.CardEntity
import de.pawcode.cardstore.navigation.Screen
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.CardsListComponent
import de.pawcode.cardstore.ui.sheets.ViewCardSheet
import de.pawcode.cardstore.ui.viewmodels.CardViewModel


enum class SortOption {
    ALPHABETICALLY, RECENTLY_USED, MOST_USED
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(navController: NavController, viewModel: CardViewModel = viewModel()) {
    val cards by viewModel.allCards.collectAsState(initial = emptyList())

    var sortMenuExpanded by remember { mutableStateOf(false) }
    var sortBy by remember { mutableStateOf(SortOption.ALPHABETICALLY) }

    var showCardSheet by remember { mutableStateOf<CardEntity?>(null) }
    val sheetState = rememberModalBottomSheetState()

    val listState = rememberLazyGridState()
    val sortedCards by rememberUpdatedState(
        when (sortBy) {
            SortOption.ALPHABETICALLY -> cards.sortedBy { it.storeName }
            SortOption.RECENTLY_USED -> cards.sortedByDescending { it.lastUsed }
            SortOption.MOST_USED -> cards.sortedByDescending { it.useCount }
        })

    LaunchedEffect(sortBy) {
        listState.scrollToItem(0)
    }

    Scaffold(topBar = {
        AppBar(
            title = "Card Store", actions = {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    IconButton(
                        onClick = { sortMenuExpanded = !sortMenuExpanded }) {
                        Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort cards")
                    }
                    DropdownMenu(
                        expanded = sortMenuExpanded,
                        onDismissRequest = { sortMenuExpanded = false }) {
                        DropdownMenuItem(text = { Text("Alphabetically") }, trailingIcon = {
                            if (sortBy == SortOption.ALPHABETICALLY) {
                                Icon(Icons.Filled.Check, contentDescription = null)
                            }
                        }, onClick = {
                            sortBy = SortOption.ALPHABETICALLY
                            sortMenuExpanded = false
                        })
                        DropdownMenuItem(text = { Text("Recently used") }, trailingIcon = {
                            if (sortBy == SortOption.RECENTLY_USED) {
                                Icon(Icons.Filled.Check, contentDescription = null)
                            }
                        }, onClick = {
                            sortBy = SortOption.RECENTLY_USED
                            sortMenuExpanded = false
                        })
                        DropdownMenuItem(text = { Text("Most used") }, trailingIcon = {
                            if (sortBy == SortOption.MOST_USED) {
                                Icon(Icons.Filled.Check, contentDescription = null)
                            }
                        }, onClick = {
                            sortBy = SortOption.MOST_USED
                            sortMenuExpanded = false
                        })
                    }
                }
            })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate(Screen.AddEditCard.route)
            },
            text = { Text("Add new card") },
            icon = { Icon(Icons.Filled.Add, contentDescription = "Add new card") })
    }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            CardsListComponent(cards = sortedCards, listState = listState, onCardClicked = { card ->
                viewModel.addUsage(card)
                showCardSheet = card
            }, onCardLongPressed = {
                navController.navigate(Screen.AddEditCard.route + "?cardId=${it.id}")
            })

            showCardSheet?.let {
                ModalBottomSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .safeDrawingPadding(),
                    sheetState = sheetState,
                    onDismissRequest = { showCardSheet = null }) {
                    ViewCardSheet(showCardSheet!!)
                }
            }
        }
    }
}