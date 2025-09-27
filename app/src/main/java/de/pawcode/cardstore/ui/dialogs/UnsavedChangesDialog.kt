package de.pawcode.cardstore.ui.dialogs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R

@Composable
fun UnsavedChangesDialog(onDismissRequest: () -> Unit, onDiscard: () -> Unit, onSave: () -> Unit) {
  AlertDialog(
    title = { Text(stringResource(R.string.unsaved_changes_title)) },
    text = { Text(stringResource(R.string.unsaved_changes_description)) },
    onDismissRequest = { onDismissRequest() },
    confirmButton = {
      Row(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = { onDismissRequest() }) {
          Text(stringResource(R.string.common_cancel))
        }
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(onClick = { onDiscard() }) { Text(stringResource(R.string.common_discard)) }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = { onSave() }) { Text(stringResource(R.string.common_save)) }
      }
    },
  )
}

@Preview(showBackground = true)
@Composable
fun PreviewUnsavedChangesDialog() {
  UnsavedChangesDialog(onDismissRequest = {}, onDiscard = {}, onSave = {})
}
