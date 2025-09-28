package de.pawcode.cardstore.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.charlex.compose.RevealSwipe
import de.pawcode.cardstore.R
import de.pawcode.cardstore.ScreenLabelEdit
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL_LIST
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.dialogs.ConfirmDialog
import de.pawcode.cardstore.ui.sheets.Option
import de.pawcode.cardstore.ui.sheets.OptionSheet
import de.pawcode.cardstore.ui.sheets.OptionSheetInfo
import de.pawcode.cardstore.ui.viewmodels.CardViewModel
import kotlinx.coroutines.launch

@Composable
fun LabelListScreen(backStack: SnapshotStateList<Any>, viewModel: CardViewModel = viewModel()) {
  val scope = rememberCoroutineScope()

  val labels by viewModel.allLabels.collectAsState(initial = emptyList())

  LabelListScreenComponent(
    labels = labels,
    onBack = { backStack.removeLastOrNull() },
    onEdit = { label -> backStack.add(ScreenLabelEdit(label?.labelId)) },
    onDelete = { scope.launch { viewModel.deleteLabel(it) } },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelListScreenComponent(
  labels: List<LabelEntity>,
  onBack: () -> Unit,
  onEdit: (LabelEntity?) -> Unit,
  onDelete: (LabelEntity) -> Unit,
) {
  val haptics = LocalHapticFeedback.current

  var showLabelOptionSheet by remember { mutableStateOf<LabelEntity?>(null) }
  val cardOptionSheetState = rememberModalBottomSheetState()

  var openDeleteDialog by remember { mutableStateOf<LabelEntity?>(null) }

  Scaffold(
    topBar = { AppBar(title = stringResource(R.string.card_labels), onBack = { onBack() }) },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { onEdit(null) },
        text = { Text(stringResource(R.string.labels_new)) },
        icon = {
          Icon(
            painterResource(R.drawable.new_label_solid),
            contentDescription = stringResource(R.string.labels_new),
          )
        },
      )
    },
  ) { innerPadding ->
    Column(
      modifier = Modifier.fillMaxSize().padding(innerPadding),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      if (labels.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
          Text(
            text = stringResource(R.string.labels_list_empty),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
          )
        }
      } else {
        Column(
          modifier =
            Modifier.widthIn(max = 500.dp).fillMaxWidth().verticalScroll(rememberScrollState())
        ) {
          labels.forEachIndexed { index, label ->
            RevealSwipe(
              shape = RoundedCornerShape(0.dp),
              backgroundStartActionLabel = stringResource(R.string.label_edit),
              backgroundCardStartColor = MaterialTheme.colorScheme.secondaryContainer,
              hiddenContentStart = {
                Icon(
                  painterResource(R.drawable.edit_solid),
                  contentDescription = stringResource(R.string.label_edit),
                  tint = MaterialTheme.colorScheme.onSecondaryContainer,
                )
              },
              onBackgroundStartClick = {
                onEdit(label)
                true
              },
              backgroundEndActionLabel = stringResource(R.string.label_delete_title),
              backgroundCardEndColor = MaterialTheme.colorScheme.errorContainer,
              hiddenContentEnd = {
                Icon(
                  painterResource(R.drawable.delete_forever_solid),
                  contentDescription = stringResource(R.string.label_delete_title),
                  tint = MaterialTheme.colorScheme.onErrorContainer,
                )
              },
              onBackgroundEndClick = {
                openDeleteDialog = label
                true
              },
              onContentClick = { showLabelOptionSheet = label },
              onContentLongClick = {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                showLabelOptionSheet = label
              },
              card = { shape, content ->
                Card(
                  modifier = Modifier.matchParentSize(),
                  colors =
                    CardDefaults.cardColors(
                      contentColor = MaterialTheme.colorScheme.onBackground,
                      containerColor = Color.Transparent,
                    ),
                  shape = shape,
                  content = content,
                )
              },
            ) {
              ListItem(
                modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(4.dp),
                headlineContent = { Text(label.name) },
              )
            }

            HorizontalDivider()
          }
        }
      }
    }

    showLabelOptionSheet?.let {
      ModalBottomSheet(
        sheetState = cardOptionSheetState,
        dragHandle = {},
        onDismissRequest = { showLabelOptionSheet = null },
      ) {
        OptionSheet(
          Option(
            label = stringResource(R.string.label_edit),
            icon = R.drawable.edit_solid,
            onClick = {
              onEdit(it)
              showLabelOptionSheet = null
            },
          ),
          Option(
            label = stringResource(R.string.label_delete_title),
            icon = R.drawable.delete_forever_solid,
            onClick = {
              openDeleteDialog = it
              showLabelOptionSheet = null
            },
          ),
        ) {
          OptionSheetInfo(
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
            icon = R.drawable.label,
            title = it.name,
          )
        }
      }
    }

    openDeleteDialog?.let {
      ConfirmDialog(
        onDismissRequest = { openDeleteDialog = null },
        onConfirmation = {
          onDelete(it)
          openDeleteDialog = null
        },
        dialogTitle = stringResource(R.string.label_delete_title),
        dialogText = stringResource(R.string.label_delete_description),
        confirmText = stringResource(R.string.common_delete),
      )
    }
  }
}

@Preview
@Preview(device = "id:pixel_tablet")
@Composable
fun PreviewLabelListScreenComponent() {
  LabelListScreenComponent(labels = EXAMPLE_LABEL_LIST, onBack = {}, onEdit = {}, onDelete = {})
}

@Preview
@Preview(device = "id:pixel_tablet")
@Composable
fun PreviewLabelListScreenComponentEmpty() {
  LabelListScreenComponent(labels = listOf(), onBack = {}, onEdit = {}, onDelete = {})
}
