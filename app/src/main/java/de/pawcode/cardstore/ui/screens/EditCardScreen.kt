package de.pawcode.cardstore.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.simonsickle.compose.barcodes.BarcodeType
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.classes.CardWithLabels
import de.pawcode.cardstore.data.database.classes.EXAMPLE_CARD_WITH_LABELS
import de.pawcode.cardstore.data.database.classes.emptyCardWithLabels
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL_LIST
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.EditCardForm
import de.pawcode.cardstore.ui.components.SaveFabComponent
import de.pawcode.cardstore.ui.dialogs.UnsavedChangesDialog
import de.pawcode.cardstore.ui.viewmodels.CardViewModel
import de.pawcode.cardstore.utils.classifyLabelsForUpdate
import de.pawcode.cardstore.utils.hasCardChanged
import de.pawcode.cardstore.utils.isCardValid
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun EditCardScreen(
  navController: NavController,
  cardId: String? = null,
  storeName: String? = null,
  cardNumber: String? = null,
  barcodeType: BarcodeType? = null,
  color: Int? = null,
  viewModel: CardViewModel = viewModel(),
) {
  val labels by viewModel.allLabels.collectAsState(initial = emptyList())
  val initialCard =
    if (cardId != null) {
      viewModel.getCardById(cardId).collectAsState(initial = null).value
    } else {
      CardWithLabels(
        card =
          CardEntity(
            cardId = Uuid.random().toString(),
            storeName = storeName ?: "",
            cardNumber = cardNumber ?: "",
            barcodeFormat = barcodeType ?: BarcodeType.QR_CODE,
            color = color ?: Color.White.toArgb(),
          ),
        labels = emptyList(),
      )
    }

  val snackbarMessage =
    stringResource(if (cardId != null) R.string.card_updated else R.string.card_added)

  initialCard?.let {
    EditCardScreenComponent(
      isCreateCard = cardId == null,
      initialCard = it,
      labels = labels,
      onBack = { navController.popBackStack() },
      onSave = { card ->
        if (cardId != null) {
          viewModel.updateCard(card.card)

          val (labelsToAdd, labelsToRemove) =
            classifyLabelsForUpdate(initialCard.labels, card.labels)
          viewModel.removeLabelsFromCard(initialCard.card.cardId, labelsToRemove)
          viewModel.addLabelsToCard(initialCard.card.cardId, labelsToAdd)
        } else {
          viewModel.insertCard(card.card)
          viewModel.addLabelsToCard(card.card.cardId, card.labels.map { it.labelId })
        }

        SnackbarService.showSnackbar(message = snackbarMessage)

        navController.popBackStack()
      },
    )
  }
}

@Composable
fun EditCardScreenComponent(
  isCreateCard: Boolean,
  initialCard: CardWithLabels,
  labels: List<LabelEntity>,
  onBack: () -> Unit,
  onSave: (CardWithLabels) -> Unit,
) {
  var card by remember { mutableStateOf(initialCard) }
  var showUnsavedChangesDialog by remember { mutableStateOf(false) }

  LaunchedEffect(initialCard.card.cardId) { card = initialCard }

  val isValid by remember { derivedStateOf { isCardValid(card.card) } }
  val hasChanges by remember {
    derivedStateOf { isCreateCard || hasCardChanged(initialCard, card) }
  }

  val handleBack = {
    if (hasChanges && !isCreateCard) {
      showUnsavedChangesDialog = true
    } else {
      onBack()
    }
  }

  if (showUnsavedChangesDialog) {
    UnsavedChangesDialog(
      onDismissRequest = { showUnsavedChangesDialog = false },
      onDiscard = {
        showUnsavedChangesDialog = false
        onBack()
      },
      onSave = {
        showUnsavedChangesDialog = false
        onSave(card)
      },
    )
  }

  Scaffold(
    modifier = Modifier.imePadding(),
    topBar = {
      AppBar(
        title = stringResource(if (!isCreateCard) R.string.card_edit else R.string.card_add),
        subtitle = if (!isCreateCard) initialCard.card.storeName else null,
        onBack = { handleBack() },
      )
    },
    floatingActionButton = {
      SaveFabComponent(
        hadInitialValue = !isCreateCard,
        hasChanges = hasChanges,
        isValid = isValid,
        onSave = { onSave(card) },
      )
    },
  ) { innerPadding ->
    Column(
      modifier = Modifier.padding(innerPadding).fillMaxSize().verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      EditCardForm(
        modifier = Modifier.widthIn(max = 500.dp),
        card = card,
        labels = labels,
        onCardUpdate = { card = it },
      )
    }
  }
}

@Preview(showSystemUi = true)
@Preview(device = "id:pixel_tablet", showSystemUi = true)
@Composable
fun PreviewEditCardScreenComponent() {
  EditCardScreenComponent(
    isCreateCard = false,
    initialCard = EXAMPLE_CARD_WITH_LABELS,
    labels = EXAMPLE_LABEL_LIST,
    onBack = {},
    onSave = {},
  )
}

@Preview(showSystemUi = true)
@Preview(device = "id:pixel_tablet", showSystemUi = true)
@Composable
fun PreviewEditCardScreenComponentEmpty() {
  EditCardScreenComponent(
    isCreateCard = true,
    initialCard = emptyCardWithLabels(),
    labels = listOf(),
    onBack = {},
    onSave = {},
  )
}
