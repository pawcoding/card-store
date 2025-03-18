package de.pawcode.cardstore.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.classes.CardWithLabels
import de.pawcode.cardstore.data.database.classes.EXAMPLE_CARD_WITH_LABELS
import de.pawcode.cardstore.data.database.classes.emptyCardWithLabels
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL_LIST
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.EditCardForm
import de.pawcode.cardstore.ui.viewmodels.CardViewModel
import de.pawcode.cardstore.utils.classifyLabelsForUpdate
import de.pawcode.cardstore.utils.hasCardChanged
import de.pawcode.cardstore.utils.isCardValid

@Composable
fun EditCardScreen(
    navController: NavController,
    cardId: String? = null,
    viewModel: CardViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()

    val labels by viewModel.allLabels.collectAsState(initial = emptyList())
    val initialCard by viewModel.getCardById(cardId).collectAsState(initial = null)

    val snackbarMessage = stringResource(
        if (initialCard != null) R.string.card_updated else R.string.card_added
    )

    EditCardScreenComponent(
        initialCard = initialCard,
        labels = labels,
        onBack = { navController.popBackStack() },
        onSave = { card ->
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
                message = snackbarMessage,
                scope = scope
            )
        }
    )
}

@Composable
fun EditCardScreenComponent(
    initialCard: CardWithLabels?,
    labels: List<LabelEntity>,
    onBack: () -> Unit,
    onSave: (CardWithLabels) -> Unit
) {
    var card by remember { mutableStateOf(initialCard ?: emptyCardWithLabels()) }

    LaunchedEffect(initialCard) {
        card = initialCard ?: emptyCardWithLabels()
    }

    val isValid by remember { derivedStateOf { isCardValid(card.card) } }
    val hasChanges by remember {
        derivedStateOf { initialCard == null || hasCardChanged(initialCard, card) }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            AppBar(
                title = stringResource(if (initialCard != null) R.string.card_edit else R.string.card_add),
                navigationIcon = {
                    IconButton(
                        onClick = { onBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back)
                        )
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

                        onSave(card)
                    },
                    text = {
                        if (initialCard != null) {
                            Text(stringResource(R.string.common_update))
                        } else {
                            Text(stringResource(R.string.common_save))
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Save,
                            contentDescription = stringResource(R.string.card_save)
                        )
                    },
                    containerColor = if (isValid) MaterialTheme.colorScheme.primary else Color.Gray,
                    contentColor = if (isValid) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.38f
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditCardForm(
                modifier = Modifier.widthIn(max = 500.dp),
                initialCard = card,
                labels = labels,
                onCardUpdate = { card = it }
            )
        }
    }
}

@Preview
@Preview(device = "id:pixel_tablet")
@Composable
fun PreviewEditCardScreenComponent() {
    EditCardScreenComponent(
        initialCard = EXAMPLE_CARD_WITH_LABELS,
        labels = EXAMPLE_LABEL_LIST,
        onBack = {},
        onSave = {}
    )
}

@Preview
@Preview(device = "id:pixel_tablet")
@Composable
fun PreviewEditCardScreenComponentEmpty() {
    EditCardScreenComponent(
        initialCard = null,
        labels = listOf(),
        onBack = {},
        onSave = {}
    )
}
