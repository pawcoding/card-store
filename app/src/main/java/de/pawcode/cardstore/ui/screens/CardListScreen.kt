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
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.data.database.CardEntity
import de.pawcode.cardstore.data.enums.SortAttribute
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.navigation.Screen
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.CardsListComponent
import de.pawcode.cardstore.ui.sheets.Option
import de.pawcode.cardstore.ui.sheets.OptionSheet
import de.pawcode.cardstore.ui.sheets.ViewCardSheet
import de.pawcode.cardstore.ui.viewmodels.CardViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(navController: NavController, viewModel: CardViewModel = viewModel()) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val scope = rememberCoroutineScope()

    val cards by viewModel.allCards.collectAsState(initial = emptyList())

    var sortMenuExpanded by remember { mutableStateOf(false) }
    var sortBy by remember { mutableStateOf(SortAttribute.ALPHABETICALLY) }
    val storedSortOrder by preferencesManager.sortAttribute.collectAsState(initial = SortAttribute.ALPHABETICALLY)

    LaunchedEffect(storedSortOrder) {
        sortBy = storedSortOrder
    }

    var showCardSheet by remember { mutableStateOf<CardEntity?>(null) }
    val cardSheetState = rememberModalBottomSheetState()

    var showCardOptionSheet by remember { mutableStateOf<CardEntity?>(null) }
    val cardOptionSheetState = rememberModalBottomSheetState()

    val listState = rememberLazyGridState()
    val sortedCards by rememberUpdatedState(
        when (sortBy) {
        SortAttribute.ALPHABETICALLY -> cards.sortedBy { it.storeName }
        SortAttribute.RECENTLY_USED -> cards.sortedByDescending { it.lastUsed }
        SortAttribute.MOST_USED -> cards.sortedByDescending { it.useCount }
    })

    LaunchedEffect(sortBy) {
        listState.scrollToItem(0)
    }

    fun updateSortAttribute(sortAttribute: SortAttribute) {
        scope.launch {
            preferencesManager.saveSortAttribute(sortAttribute)
        }
        sortBy = sortAttribute
        sortMenuExpanded = false
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
                            if (sortBy == SortAttribute.ALPHABETICALLY) {
                                Icon(Icons.Filled.Check, contentDescription = null)
                            }
                        }, onClick = { updateSortAttribute(SortAttribute.ALPHABETICALLY) })
                        DropdownMenuItem(text = { Text("Recently used") }, trailingIcon = {
                            if (sortBy == SortAttribute.RECENTLY_USED) {
                                Icon(Icons.Filled.Check, contentDescription = null)
                            }
                        }, onClick = { updateSortAttribute(SortAttribute.RECENTLY_USED) })
                        DropdownMenuItem(text = { Text("Most used") }, trailingIcon = {
                            if (sortBy == SortAttribute.MOST_USED) {
                                Icon(Icons.Filled.Check, contentDescription = null)
                            }
                        }, onClick = { updateSortAttribute(SortAttribute.MOST_USED) })
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
                    sheetState = cardSheetState,
                    onDismissRequest = { showCardSheet = null }) {
                    ViewCardSheet(showCardSheet!!)
                }
            }

            showCardOptionSheet?.let {
                ModalBottomSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .safeDrawingPadding(),
                    sheetState = cardOptionSheetState,
                    onDismissRequest = { showCardOptionSheet = null }) {
                    OptionSheet(
                        listOf(
                            Option(
                                label = "Edit card", icon = Icons.Filled.Edit, onClick = {
                                    navController.navigate(Screen.AddEditCard.route + "?cardId=${showCardOptionSheet!!.id}")
                                }), Option(
                                label = "Delete card",
                                icon = Icons.Filled.DeleteForever,
                                onClick = {})
                        )
                    )
                }
            }
        }
    }
}