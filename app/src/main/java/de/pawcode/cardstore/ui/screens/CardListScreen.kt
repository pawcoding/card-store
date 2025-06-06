package de.pawcode.cardstore.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.twotone.CreditCard
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.simonsickle.compose.barcodes.BarcodeType
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.classes.CardWithLabels
import de.pawcode.cardstore.data.database.classes.EXAMPLE_CARD_WITH_LABELS
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL_LIST
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.data.enums.SortAttribute
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.data.services.DeeplinkService
import de.pawcode.cardstore.data.services.ReviewService
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.navigation.Screen
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.CardsListComponent
import de.pawcode.cardstore.ui.components.DropdownOption
import de.pawcode.cardstore.ui.components.LabelsListComponent
import de.pawcode.cardstore.ui.components.SelectDropdownMenu
import de.pawcode.cardstore.ui.dialogs.ConfirmDialog
import de.pawcode.cardstore.ui.sheets.ImportCardSheet
import de.pawcode.cardstore.ui.sheets.Option
import de.pawcode.cardstore.ui.sheets.OptionSheet
import de.pawcode.cardstore.ui.sheets.OptionSheetInfo
import de.pawcode.cardstore.ui.sheets.ShareCardSheet
import de.pawcode.cardstore.ui.sheets.ViewCardSheet
import de.pawcode.cardstore.ui.utils.BarcodeScanner
import de.pawcode.cardstore.ui.utils.PkpassFilePicker
import de.pawcode.cardstore.ui.viewmodels.CardViewModel
import de.pawcode.cardstore.utils.calculateCardScore
import de.pawcode.cardstore.utils.isLightColor
import de.pawcode.cardstore.utils.mapBarcodeFormat
import de.pawcode.cardstore.utils.parseDeeplink
import de.pawcode.cardstore.utils.parsePkpass
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
      if (cardNumber != null && barcodeFormat != null) {
        val deeplink = parseDeeplink(cardNumber)
        if (deeplink != null) {
          DeeplinkService.deeplinkReceived(deeplink)
          return@CardListScreenComponent
        }
      }

      val route = buildString {
        append(Screen.EditCard.route)
        if (cardNumber != null || barcodeFormat != null) {
          append("?")
          if (cardNumber != null) {
            append("cardNumber=$cardNumber&")
          }
          if (barcodeFormat != null) {
            append("barcodeFormat=$barcodeFormat&")
          }
        }
        if (endsWith("&")) {
          deleteCharAt(length - 1)
        }
      }
      navController.navigate(route)
    },
    onImportCard = { card ->
      viewModel.insertCard(card)
      SnackbarService.showSnackbar(context.getString(R.string.import_card_success))
      DeeplinkService.clearDeeplink()
    },
    onEditCard = { card ->
      navController.navigate(Screen.EditCard.route + "?cardId=${card.cardId}")
    },
    onShowCard = { viewModel.addUsage(it) },
    onDeleteCard = { scope.launch { viewModel.deleteCard(it) } },
    onViewLabels = { navController.navigate(Screen.LabelList.route) },
    onSortChange = { scope.launch { preferencesManager.saveSortAttribute(it) } },
    onShowAbout = { navController.navigate(Screen.About.route) },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreenComponent(
  cardsFlow: Flow<List<CardWithLabels>>,
  labelsFlow: Flow<List<LabelEntity>>,
  sortByFlow: Flow<SortAttribute?>,
  onCreateCard: (cardNumber: String?, barcodeType: BarcodeType?) -> Unit,
  onImportCard: (CardEntity) -> Unit,
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
  var showCardShareSheet by remember { mutableStateOf<CardEntity?>(null) }
  var showCardCreateSheet by remember { mutableStateOf(false) }
  var openDeleteDialog by remember { mutableStateOf<CardEntity?>(null) }
  var showBarcodeScanner by remember { mutableStateOf(false) }
  var showPkpassPicker by remember { mutableStateOf(false) }
  val showCardImportSheet by DeeplinkService.deeplinkFlow.collectAsState(initial = null)

  val listState = rememberLazyGridState()
  val cardSheetState = rememberModalBottomSheetState()
  val cardShareSheetState = rememberModalBottomSheetState()
  val cardImportSheetState = rememberModalBottomSheetState()
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
  val cardsSorted by
    rememberUpdatedState(
      when (sortBy) {
        SortAttribute.INTELLIGENT -> cardsFiltered.sortedByDescending { calculateCardScore(it) }
        SortAttribute.ALPHABETICALLY -> cardsFiltered.sortedBy { it.storeName }
        SortAttribute.RECENTLY_USED -> cardsFiltered.sortedByDescending { it.lastUsed }
        SortAttribute.MOST_USED -> cardsFiltered.sortedByDescending { it.useCount }
        else -> null
      }
    )

  LaunchedEffect(sortBy, selectedLabel) { listState.scrollToItem(0) }

  Scaffold(
    topBar = {
      AppBar(
        title = stringResource(R.string.app_name),
        actions = {
          IconButton(onClick = { onShowAbout() }) {
            Icon(Icons.Outlined.Info, contentDescription = stringResource(R.string.about))
          }
          SelectDropdownMenu(
            icon = Icons.AutoMirrored.Filled.Sort,
            title = stringResource(R.string.cards_sort),
            value = sortBy,
            values =
              listOf(
                DropdownOption(
                  title = stringResource(R.string.sort_intelligent),
                  icon = Icons.Filled.AutoFixHigh,
                  value = SortAttribute.INTELLIGENT,
                ),
                DropdownOption(
                  title = stringResource(R.string.sort_alphabetically),
                  icon = Icons.Filled.SortByAlpha,
                  value = SortAttribute.ALPHABETICALLY,
                ),
                DropdownOption(
                  title = stringResource(R.string.sort_most_used),
                  icon = Icons.AutoMirrored.Filled.TrendingUp,
                  value = SortAttribute.MOST_USED,
                ),
                DropdownOption(
                  title = stringResource(R.string.sort_recently_used),
                  icon = Icons.Filled.History,
                  value = SortAttribute.RECENTLY_USED,
                ),
              ),
            onValueChange = { onSortChange(it) },
          )
        },
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { showCardCreateSheet = true },
        text = { Text(stringResource(R.string.cards_new)) },
        icon = {
          Icon(Icons.Filled.AddCard, contentDescription = stringResource(R.string.cards_new))
        },
      )
    },
  ) { innerPadding ->
    Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 8.dp)) {
      Box(modifier = Modifier.padding(vertical = 8.dp)) {
        LabelsListComponent(
          labels = labels,
          selected = selectedLabel,
          onLabelClick = { selectedLabel = if (selectedLabel == it.labelId) null else it.labelId },
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
          onCardLongPressed = { showCardOptionSheet = it },
          onCardCreate = { showCardCreateSheet = true },
        )
      }

      showCardSheet?.let {
        ModalBottomSheet(
          modifier = Modifier.fillMaxHeight().windowInsetsPadding(WindowInsets.statusBars),
          sheetState = cardSheetState,
          onDismissRequest = { showCardSheet = null },
        ) {
          ViewCardSheet(it)
        }
      }

      showCardImportSheet?.let {
        ModalBottomSheet(
          sheetState = cardImportSheetState,
          dragHandle = {},
          onDismissRequest = { DeeplinkService.clearDeeplink() },
        ) {
          ImportCardSheet(card = it, onImport = { onImportCard(it) })
        }
      }

      showCardShareSheet?.let {
        ModalBottomSheet(
          modifier = Modifier.fillMaxHeight().windowInsetsPadding(WindowInsets.statusBars),
          sheetState = cardShareSheetState,
          onDismissRequest = {
            showCardShareSheet = null
            ReviewService.sendReviewRequest()
          },
        ) {
          ShareCardSheet(it)
        }
      }

      showCardOptionSheet?.let {
        ModalBottomSheet(
          sheetState = cardOptionSheetState,
          dragHandle = {},
          onDismissRequest = { showCardOptionSheet = null },
        ) {
          OptionSheet(
            Option(
              label = stringResource(R.string.card_edit),
              icon = Icons.Filled.Edit,
              onClick = {
                onEditCard(it)
                showCardOptionSheet = null
              },
            ),
            Option(
              label = stringResource(R.string.card_share),
              icon = Icons.Filled.Share,
              onClick = {
                showCardShareSheet = it
                showCardOptionSheet = null
                ReviewService.prepareReviewRequest()
              },
            ),
            Option(
              label = stringResource(R.string.card_delete_title),
              icon = Icons.Filled.DeleteForever,
              onClick = {
                openDeleteDialog = it
                showCardOptionSheet = null
              },
            ),
          ) {
            val color by remember { mutableStateOf(Color(it.color)) }
            val isLightColor by remember { derivedStateOf { isLightColor(color) } }

            OptionSheetInfo(
              backgroundColor = color,
              iconTint = if (isLightColor) Color.Black else Color.White,
              icon = Icons.TwoTone.CreditCard,
              title = it.storeName,
              subtitle = it.cardNumber,
            )
          }
        }
      }

      if (showCardCreateSheet) {
        ModalBottomSheet(
          sheetState = cardCreateSheetState,
          dragHandle = {},
          onDismissRequest = { showCardCreateSheet = false },
        ) {
          OptionSheet(
            Option(
              label = stringResource(R.string.scan_barcode),
              icon = Icons.Outlined.QrCodeScanner,
              onClick = {
                showBarcodeScanner = true
                showCardCreateSheet = false
              },
            ),
            Option(
              label = stringResource(R.string.card_create_pkpass),
              icon = Icons.Outlined.FileOpen,
              onClick = {
                showPkpassPicker = true
                showCardCreateSheet = false
              },
            ),
            Option(
              label = stringResource(R.string.card_create_manual),
              icon = Icons.Filled.Edit,
              onClick = {
                onCreateCard(null, null)
                showCardCreateSheet = false
              },
            ),
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
          Icons.TwoTone.DeleteForever,
        )
      }

      if (showBarcodeScanner) {
        BarcodeScanner(
          onBarcodeDetected = { barcode ->
            onCreateCard(
              barcode.rawValue ?: barcode.displayValue ?: "",
              mapBarcodeFormat(barcode.format),
            )
            showBarcodeScanner = false
          },
          onCancel = { showBarcodeScanner = false },
        )
      }

      if (showPkpassPicker) {
        PkpassFilePicker(
          onFileRead = { content ->
            val deeplink = parsePkpass(content)
            if (deeplink != null) {
              DeeplinkService.deeplinkReceived(deeplink)
            }

            showPkpassPicker = false
          },
          onCancel = { showPkpassPicker = false },
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
    sortByFlow = flowOf(SortAttribute.INTELLIGENT),
    onCreateCard = { _, _ -> },
    onImportCard = {},
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
    sortByFlow = flowOf(SortAttribute.INTELLIGENT),
    onCreateCard = { _, _ -> },
    onImportCard = {},
    onEditCard = {},
    onShowCard = {},
    onDeleteCard = {},
    onViewLabels = {},
    onSortChange = {},
    onShowAbout = {},
  )
}
