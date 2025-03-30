package de.pawcode.cardstore.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.twotone.DeleteForever
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL_LIST
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.navigation.Screen
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.dialogs.ConfirmDialog
import de.pawcode.cardstore.ui.sheets.Option
import de.pawcode.cardstore.ui.sheets.OptionSheet
import de.pawcode.cardstore.ui.viewmodels.CardViewModel
import kotlinx.coroutines.launch

@Composable
fun LabelListScreen(navController: NavController, viewModel: CardViewModel = viewModel()) {
  val scope = rememberCoroutineScope()

  val labels by viewModel.allLabels.collectAsState(initial = emptyList())

  LabelListScreenComponent(
    labels = labels,
    onBack = { navController.popBackStack() },
    onEdit = { label ->
      if (label != null) {
        navController.navigate(Screen.EditLabel.route + "?labelId=${label.labelId}")
      } else {
        navController.navigate(Screen.EditLabel.route)
      }
    },
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
  var showLabelOptionSheet by remember { mutableStateOf<LabelEntity?>(null) }
  val cardOptionSheetState = rememberModalBottomSheetState()

  var openDeleteDialog by remember { mutableStateOf<LabelEntity?>(null) }

  Scaffold(
    topBar = { AppBar(title = stringResource(R.string.card_labels), onBack = { onBack() }) },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { onEdit(null) },
        text = { Text(stringResource(R.string.labels_new)) },
        icon = { Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.labels_new)) },
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
            ListItem(
              headlineContent = { Text(label.name) },
              trailingContent = {
                Icon(
                  Icons.Filled.MoreHoriz,
                  contentDescription = stringResource(R.string.labels_options),
                )
              },
              modifier =
                Modifier.pointerInput(Unit) {
                  detectTapGestures(onTap = { showLabelOptionSheet = label })
                },
            )

            HorizontalDivider()
          }
        }
      }
    }

    showLabelOptionSheet?.let {
      ModalBottomSheet(
        modifier = Modifier.safeDrawingPadding(),
        sheetState = cardOptionSheetState,
        onDismissRequest = { showLabelOptionSheet = null },
      ) {
        OptionSheet(
          Option(
            label = stringResource(R.string.label_edit),
            icon = Icons.Filled.Edit,
            onClick = {
              onEdit(it)
              showLabelOptionSheet = null
            },
          ),
          Option(
            label = stringResource(R.string.label_delete_title),
            icon = Icons.Filled.DeleteForever,
            onClick = {
              openDeleteDialog = it
              showLabelOptionSheet = null
            },
          ),
        )
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
        Icons.TwoTone.DeleteForever,
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
