package de.pawcode.cardstore.ui.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.pawcode.cardstore.R

@Composable
fun ConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    confirmText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = dialogTitle)
        },
        title = {
            Text(dialogTitle)
        },
        text = {
            Text(dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.common_cancel))
            }
        }
    )
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewConfirmDialog() {
    ConfirmDialog(
        onDismissRequest = {},
        onConfirmation = {},
        dialogTitle = "Example",
        dialogText = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam",
        confirmText = "Confirm",
        Icons.TwoTone.AccountCircle
    )
}
