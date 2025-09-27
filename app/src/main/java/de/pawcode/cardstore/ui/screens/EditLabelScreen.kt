package de.pawcode.cardstore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewLabel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.data.database.entities.emptyLabel
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.SaveFabComponent
import de.pawcode.cardstore.ui.dialogs.UnsavedChangesDialog
import de.pawcode.cardstore.ui.viewmodels.CardViewModel

@Composable
fun EditLabelScreen(
  navController: NavController,
  labelId: String? = null,
  viewModel: CardViewModel = viewModel(),
) {
  rememberCoroutineScope()

  val initialLabel by viewModel.getLabelById(labelId).collectAsState(initial = null)

  val snackbarMessage =
    stringResource(if (initialLabel != null) R.string.label_updated else R.string.label_added)

  EditLabelScreenComponent(
    initialLabel = initialLabel,
    onBack = { navController.popBackStack() },
    onSave = { label ->
      if (initialLabel != null) {
        viewModel.updateLabel(label)
      } else {
        viewModel.insertLabel(label)
      }

      navController.popBackStack()

      SnackbarService.showSnackbar(message = snackbarMessage)
    },
  )
}

@Composable
fun EditLabelScreenComponent(
  initialLabel: LabelEntity?,
  onBack: () -> Unit,
  onSave: (LabelEntity) -> Unit,
) {
  var label by remember { mutableStateOf(initialLabel ?: emptyLabel()) }
  var showUnsavedChangesDialog by remember { mutableStateOf(false) }

  LaunchedEffect(initialLabel) { label = initialLabel ?: emptyLabel() }

  val isValid by remember { derivedStateOf { label.name.isNotEmpty() } }
  val hasChanges by remember {
    derivedStateOf { initialLabel == null || initialLabel.name != label.name }
  }

  val handleBack = {
    if (hasChanges && initialLabel != null) {
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
        onSave(label)
      },
    )
  }

  Scaffold(
    modifier = Modifier.imePadding(),
    topBar = {
      AppBar(
        title =
          stringResource(if (initialLabel != null) R.string.label_edit else R.string.label_add),
        onBack = { handleBack() },
      )
    },
    floatingActionButton = {
      SaveFabComponent(
        hadInitialValue = initialLabel != null,
        hasChanges = hasChanges,
        isValid = isValid,
        onSave = { onSave(label) },
      )
    },
  ) { innerPadding ->
    Column(
      modifier = Modifier.padding(innerPadding).fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Column(
        modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          Icon(
            Icons.Filled.NewLabel,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp),
          )

          Text(
            text = stringResource(R.string.label_name),
            style = MaterialTheme.typography.bodyLarge,
          )
        }

        OutlinedTextField(
          value = label.name,
          onValueChange = { label = label.copy(name = it) },
          label = { Text(stringResource(R.string.label_name) + "*") },
          supportingText = { Text("*" + stringResource(R.string.common_required)) },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true,
          keyboardOptions =
            KeyboardOptions(
              keyboardType = KeyboardType.Text,
              autoCorrectEnabled = true,
              capitalization = KeyboardCapitalization.Sentences,
              imeAction = ImeAction.Done,
            ),
        )
      }
    }
  }
}

@Preview
@Preview(device = "id:pixel_tablet")
@Composable
fun PreviewEditLabelScreenComponent() {
  EditLabelScreenComponent(initialLabel = EXAMPLE_LABEL, onBack = {}, onSave = {})
}

@Preview
@Preview(device = "id:pixel_tablet")
@Composable
fun PreviewEditLabelScreenComponentEmpty() {
  EditLabelScreenComponent(initialLabel = null, onBack = {}, onSave = {})
}
