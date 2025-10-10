package de.pawcode.cardstore.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import de.pawcode.cardstore.R

@Composable
fun UnsavedChangesDialog(onDismissRequest: () -> Unit, onDiscard: () -> Unit, onSave: () -> Unit) {
  Dialog(onDismissRequest = { onDismissRequest() }) {
    Card(
      shape = AlertDialogDefaults.shape,
      colors =
        CardDefaults.cardColors(
          containerColor = AlertDialogDefaults.containerColor,
          contentColor = AlertDialogDefaults.textContentColor,
        ),
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Text(
          text = stringResource(R.string.unsaved_changes_title),
          style = MaterialTheme.typography.headlineSmall,
          color = AlertDialogDefaults.titleContentColor,
        )

        Text(
          text = stringResource(R.string.unsaved_changes_description),
          style = MaterialTheme.typography.bodyMedium,
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          TextButton(onClick = { onDismissRequest() }) {
            Text(stringResource(R.string.common_cancel))
          }

          Row {
            TextButton(onClick = { onDiscard() }) { Text(stringResource(R.string.common_discard)) }
            TextButton(onClick = { onSave() }) { Text(stringResource(R.string.common_save)) }
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewUnsavedChangesDialog() {
  UnsavedChangesDialog(onDismissRequest = {}, onDiscard = {}, onSave = {})
}
