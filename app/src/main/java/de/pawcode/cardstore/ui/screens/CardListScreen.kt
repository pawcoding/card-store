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
import androidx.compose.material.icons.twotone.DeleteForever
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.enums.SortAttribute
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.navigation.Screen
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.CardsListComponent
import de.pawcode.cardstore.ui.components.LabelsListComponent
import de.pawcode.cardstore.ui.dialogs.ConfirmDialog
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

    val cardsWithLabels by viewModel.allCards.collectAsState(initial = emptyList())
    val labels by viewModel.allLabels.collectAsState(initial = emptyList())

    var selectedLabel by remember { mutableStateOf<String?>(null) }

    var sortMenuExpanded by remember { mutableStateOf(false) }
    val sortBy by preferencesManager.sortAttribute.collectAsState(initial = null)

    var showCardSheet by remember { mutableStateOf<CardEntity?>(null) }
    val cardSheetState = rememberModalBottomSheetState()

    var showCardOptionSheet by remember { mutableStateOf<CardEntity?>(null) }
    val cardOptionSheetState = rememberModalBottomSheetState()

    var openDeleteDialog by remember { mutableStateOf<CardEntity?>(null) }

    val listState = rememberLazyGridState()
    val cards by remember {
        derivedStateOf {
            cardsWithLabels
                .filter { selectedLabel == null || it.labels.any { it.labelId == selectedLabel } }
                .map { it.card }
        }
    }
    val sortedCards by rememberUpdatedState(
        when (sortBy) {
            SortAttribute.ALPHABETICALLY -> cards.sortedBy { it.storeName }
            SortAttribute.RECENTLY_USED -> cards.sortedByDescending { it.lastUsed }
            SortAttribute.MOST_USED -> cards.sortedByDescending { it.useCount }
            else -> null
        }
    )

    LaunchedEffect(sortBy, selectedLabel) {
        listState.scrollToItem(0)
    }

    fun updateSortAttribute(sortAttribute: SortAttribute) {
        scope.launch {
            preferencesManager.saveSortAttribute(sortAttribute)
        }
        sortMenuExpanded = false
    }

    Scaffold(topBar = {
        AppBar(
            title = stringResource(R.string.app_name),
            actions = {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    IconButton(
                        onClick = { sortMenuExpanded = !sortMenuExpanded }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Sort,
                            contentDescription = stringResource(R.string.cards_sort)
                        )
                    }
                    DropdownMenu(
                        expanded = sortMenuExpanded,
                        onDismissRequest = { sortMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.sort_alphabetically)) },
                            trailingIcon = {
                                if (sortBy == SortAttribute.ALPHABETICALLY) {
                                    Icon(Icons.Filled.Check, contentDescription = null)
                                }
                            },
                            onClick = { updateSortAttribute(SortAttribute.ALPHABETICALLY) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.sort_recently_used)) },
                            trailingIcon = {
                                if (sortBy == SortAttribute.RECENTLY_USED) {
                                    Icon(Icons.Filled.Check, contentDescription = null)
                                }
                            },
                            onClick = { updateSortAttribute(SortAttribute.RECENTLY_USED) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.sort_most_used)) },
                            trailingIcon = {
                                if (sortBy == SortAttribute.MOST_USED) {
                                    Icon(Icons.Filled.Check, contentDescription = null)
                                }
                            },
                            onClick = { updateSortAttribute(SortAttribute.MOST_USED) }
                        )
                    }
                }
            })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate(Screen.AddEditCard.route)
            },
            text = { Text(stringResource(R.string.cards_new)) },
            icon = {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.cards_new)
                )
            })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
        ) {
            Box(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                LabelsListComponent(
                    labels = labels,
                    selected = selectedLabel,
                    onLabelClick = { label ->
                        if (selectedLabel == label.labelId) {
                            selectedLabel = null
                        } else {
                            selectedLabel = label.labelId
                        }
                    },
                    onEdit = {
                        navController.navigate(Screen.FilterList.route)
                    }
                )
            }

            sortedCards?.let {
                CardsListComponent(
                    cards = it,
                    isFiltered = selectedLabel != null,
                    listState = listState,
                    onCardClicked = { card ->
                        viewModel.addUsage(card)
                        showCardSheet = card
                    },
                    onCardLongPressed = { showCardOptionSheet = it }
                )
            }

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
                        .safeDrawingPadding(),
                    sheetState = cardOptionSheetState,
                    onDismissRequest = { showCardOptionSheet = null }) {
                    OptionSheet(
                        listOf(
                            Option(
                                label = stringResource(R.string.card_edit),
                                icon = Icons.Filled.Edit,
                                onClick = {
                                    navController.navigate(Screen.AddEditCard.route + "?cardId=${it.cardId}")
                                    showCardOptionSheet = null
                                }
                            ),
                            Option(
                                label = stringResource(R.string.card_delete_title),
                                icon = Icons.Filled.DeleteForever,
                                onClick = {
                                    openDeleteDialog = showCardOptionSheet!!
                                    showCardOptionSheet = null
                                }
                            )
                        )
                    )
                }
            }

            openDeleteDialog?.let {
                ConfirmDialog(
                    onDismissRequest = { openDeleteDialog = null },
                    onConfirmation = {
                        scope.launch {
                            viewModel.deleteCard(openDeleteDialog!!)
                            openDeleteDialog = null
                        }
                    },
                    dialogTitle = stringResource(R.string.card_delete_title),
                    dialogText = stringResource(R.string.card_delete_description),
                    confirmText = stringResource(R.string.common_delete),
                    Icons.TwoTone.DeleteForever
                )
            }
        }
    }
}