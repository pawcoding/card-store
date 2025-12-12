package de.pawcode.cardstore.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.pawcode.cardstore.R

@Composable
fun SaveFabComponent(
  hasChanges: Boolean,
  isValid: Boolean,
  hadInitialValue: Boolean,
  onSave: () -> Unit,
) {
  AnimatedVisibility(
    visible = hasChanges,
    enter = slideInVertically(initialOffsetY = { it }),
    exit = slideOutVertically(targetOffsetY = { it }),
  ) {
    ExtendedFloatingActionButton(
      onClick = {
        if (!isValid) {
          return@ExtendedFloatingActionButton
        }

        onSave()
      },
      text = {
        if (hadInitialValue) {
          Text(stringResource(R.string.common_update))
        } else {
          Text(stringResource(R.string.common_save))
        }
      },
      icon = { Icon(painterResource(R.drawable.save_solid), contentDescription = null) },
      containerColor = if (isValid) MaterialTheme.colorScheme.primary else Color.Gray,
      contentColor =
        if (isValid) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    )
  }
}

@Preview
@Composable
fun PreviewSaveFabComponent() {
  SaveFabComponent(hadInitialValue = true, hasChanges = true, isValid = true, onSave = {})
}

@Preview
@Composable
fun PreviewSaveFabComponentInvalid() {
  SaveFabComponent(hadInitialValue = false, hasChanges = true, isValid = false, onSave = {})
}
