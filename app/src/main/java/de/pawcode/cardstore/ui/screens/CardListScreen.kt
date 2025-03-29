package de.pawcode.cardstore.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.twotone.DeleteForever
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.classes.CardWithLabels
import de.pawcode.cardstore.data.database.classes.EXAMPLE_CARD_WITH_LABELS
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL_LIST
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.data.enums.SortAttribute
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.navigation.Screen
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.CardsListComponent
import de.pawcode.cardstore.ui.components.DropdownOption
import de.pawcode.cardstore.ui.components.LabelsListComponent
import de.pawcode.cardstore.ui.components.SelectDropdownMenu
import de.pawcode.cardstore.ui.dialogs.ConfirmDialog
import de.pawcode.cardstore.ui.sheets.Option
import de.pawcode.cardstore.ui.sheets.OptionSheet
import de.pawcode.cardstore.ui.sheets.ViewCardSheet
import de.pawcode.cardstore.ui.utils.BarcodeScanner
import de.pawcode.cardstore.ui.viewmodels.CardViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@Composable
fun CardListScreen(navController: NavController, viewModel: CardViewModel = viewModel()) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val scope = rememberCoroutineScope()

    CardListScreenComponent(
        cardsFlow = viewModel.allCards,
        labelsFlow = viewModel.allLabels,
        sortByFlow = preferencesManager.sortAttribute,
        onCreateCard = { cardNumber, barcodeFormat ->
            val route = buildString {
                append(Screen.EditCard.route)
                if (cardNumber != null || barcodeFormat != null) {
                    append("?")
                    if (cardNumber != null) {
                        append("cardNumber=$cardNumber")
                    }
                    if (barcodeFormat != null) {
                        if (cardNumber != null) append("&")
                        append("barcodeFormat=$barcodeFormat")
                    }
                }
            }
            navController.navigate(route)
        },
        onEditCard = { card ->
            navController.navigate(Screen.EditCard.route + "?cardId=${card.cardId}")
        },
        onShowCard = {
            viewModel.addUsage(it)
        },
        onDeleteCard = {
            scope.launch {
                viewModel.deleteCard(it)
            }
        },
        onViewLabels = {
            navController.navigate(Screen.LabelList.route)
        },
        onSortChange = {
            scope.launch {
                preferencesManager.saveSortAttribute(it)
            }
        },
        onShowAbout = {
            navController.navigate(Screen.About.route)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreenComponent(
    cardsFlow: Flow<List<CardWithLabels>>,
    labelsFlow: Flow<List<LabelEntity>>,
    sortByFlow: Flow<SortAttribute?>,
    onCreateCard: (cardNumber: String?, barcodeFormat: Int?) -> Unit,
    onEditCard: (CardEntity) -> Unit,
    onShowCard: (CardEntity) -> Unit,
    onDeleteCard: (CardEntity) -> Unit,
    onViewLabels: () -> Unit,
    onSortChange: (SortAttribute) -> Unit,
    onShowAbout: () -> Unit,
) {
    val cards by cardsFlow.collectAsState(initial = emptyList())
    val labels by labelsFlow.collectAsState(initial = emptyList())
    val sortBy by sortByFlow.collectAsState(initial = null)

    var showCardSheet by remember { mutableStateOf<CardEntity?>(null) }
    var showCardOptionSheet by remember { mutableStateOf<CardEntity?>(null) }
    var showCardCreateSheet by remember { mutableStateOf(false) }
    var openDeleteDialog by remember { mutableStateOf<CardEntity?>(null) }
    var showBarcodeScanner by remember { mutableStateOf(false) }

    val listState = rememberLazyGridState()
    val cardSheetState = rememberModalBottomSheetState()
    val cardOptionSheetState = rememberModalBottomSheetState()
    val cardCreateSheetState = rememberModalBottomSheetState()

    var selectedLabel by remember { mutableStateOf<String?>(null) }

    val cardsFiltered by remember {
        derivedStateOf {
            cards
                .filter { selectedLabel == null || it.labels.any { it.labelId == selectedLabel } }
                .map { it.card }
        }
    }
    val cardsSorted by rememberUpdatedState(
        when (sortBy) {
            SortAttribute.ALPHABETICALLY -> cardsFiltered.sortedBy { it.storeName }
            SortAttribute.RECENTLY_USED -> cardsFiltered.sortedByDescending { it.lastUsed }
            SortAttribute.MOST_USED -> cardsFiltered.sortedByDescending { it.useCount }
            else -> null
        }
    )

    LaunchedEffect(sortBy, selectedLabel) {
        listState.scrollToItem(0)
    }

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.app_name),
                actions = {
                    IconButton(
                        onClick = { onShowAbout() }
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.about)
                        )
                    }
                    SelectDropdownMenu(
                        icon = Icons.AutoMirrored.Filled.Sort,
                        title = stringResource(R.string.cards_sort),
                        value = sortBy,
                        values = listOf(
                            DropdownOption(
                                title = stringResource(R.string.sort_alphabetically),
                                value = SortAttribute.ALPHABETICALLY
                            ),
                            DropdownOption(
                                title = stringResource(R.string.sort_most_used),
                                value = SortAttribute.MOST_USED
                            ),
                            DropdownOption(
                                title = stringResource(R.string.sort_recently_used),
                                value = SortAttribute.RECENTLY_USED
                            )
                        ),
                        onValueChange = {
                            onSortChange(it)
                        },
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCardCreateSheet = true },
                text = { Text(stringResource(R.string.cards_new)) },
                icon = {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.cards_new))
                }
            )
        }
    ) { innerPadding ->
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
                    onLabelClick = {
                        selectedLabel = if (selectedLabel == it.labelId) null else it.labelId
                    },
                    onEdit = { onViewLabels() },
                )
            }

            cardsSorted?.let {
                CardsListComponent(
                    cards = it,
                    isFiltered = selectedLabel != null,
                    listState = listState,
                    onCardClicked = { card ->
                        onShowCard(card)
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
                    onDismissRequest = { showCardSheet = null }
                ) {
                    ViewCardSheet(it)
                }
            }

            showCardOptionSheet?.let {
                ModalBottomSheet(
                    modifier = Modifier
                        .safeDrawingPadding(),
                    sheetState = cardOptionSheetState,
                    onDismissRequest = { showCardOptionSheet = null }) {
                    OptionSheet(
                        Option(
                            label = stringResource(R.string.card_edit),
                            icon = Icons.Filled.Edit,
                            onClick = {
                                onEditCard(it)
                                showCardOptionSheet = null
                            }
                        ),
                        Option(
                            label = stringResource(R.string.card_delete_title),
                            icon = Icons.Filled.DeleteForever,
                            onClick = {
                                openDeleteDialog = it
                                showCardOptionSheet = null
                            }
                        )
                    )
                }
            }

            if (showCardCreateSheet) {
                ModalBottomSheet(
                    modifier = Modifier
                        .safeDrawingPadding(),
                    sheetState = cardCreateSheetState,
                    onDismissRequest = { showCardCreateSheet = false }) {
                    OptionSheet(
                        Option(
                            label = stringResource(R.string.scan_barcode),
                            icon = Icons.Outlined.QrCodeScanner,
                            onClick = {
                                showBarcodeScanner = true
                                showCardCreateSheet = false
                            }
                        ),
                        Option(
                            label = stringResource(R.string.card_create_manual),
                            icon = Icons.Filled.Edit,
                            onClick = {
                                onCreateCard(null, null)
                                showCardCreateSheet = false
                            }
                        )
                    )
                }
            }

            openDeleteDialog?.let {
                ConfirmDialog(
                    onDismissRequest = { openDeleteDialog = null },
                    onConfirmation = {
                        onDeleteCard(it)
                        openDeleteDialog = null
                    },
                    dialogTitle = stringResource(R.string.card_delete_title),
                    dialogText = stringResource(R.string.card_delete_description),
                    confirmText = stringResource(R.string.common_delete),
                    Icons.TwoTone.DeleteForever
                )
            }

            if (showBarcodeScanner) {
                BarcodeScanner(
                    onBarcodeDetected = { barcode ->
                        onCreateCard(barcode.rawValue ?: "", barcode.format)
                        showBarcodeScanner = false
                    },
                    onCancel = { showBarcodeScanner = false }
                )
            }
        }
    }
}

@Preview
@Preview(device = "id:pixel_tablet")
@Composable
fun PreviewCardListScreenComponent() {
    CardListScreenComponent(
        cardsFlow = flowOf(listOf(EXAMPLE_CARD_WITH_LABELS)),
        labelsFlow = flowOf(EXAMPLE_LABEL_LIST),
        sortByFlow = flowOf(SortAttribute.ALPHABETICALLY),
        onCreateCard = { _, _ -> },
        onEditCard = {},
        onShowCard = {},
        onDeleteCard = {},
        onViewLabels = {},
        onSortChange = {},
        onShowAbout = {},
    )
}

@Preview
@Preview(device = "id:pixel_tablet")
@Composable
fun PreviewCardListScreenComponentEmpty() {
    CardListScreenComponent(
        cardsFlow = flowOf(emptyList()),
        labelsFlow = flowOf(emptyList()),
        sortByFlow = flowOf(SortAttribute.ALPHABETICALLY),
        onCreateCard = { _, _ -> },
        onEditCard = {},
        onShowCard = {},
        onDeleteCard = {},
        onViewLabels = {},
        onSortChange = {},
        onShowAbout = {},
    )
}
