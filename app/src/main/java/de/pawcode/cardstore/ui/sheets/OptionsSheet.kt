package de.pawcode.cardstore.ui.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** Option to be displayed in the [OptionSheet]. */
data class Option(
  /** Label that is shown to the user. */
  val label: String,
  /** Icons that is shown in front of the label. */
  val icon: ImageVector,
  /** Action that is executed when the option is clicked. */
  val onClick: () -> Unit,
)

@Composable
fun OptionSheet(vararg options: Option) {
  Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
    options.forEach {
      Row(
        modifier =
          Modifier.clickable { it.onClick() }
            .clip(MaterialTheme.shapes.medium)
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Icon(it.icon, contentDescription = null)

        Text(it.label)
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewOptionSheet() {
  OptionSheet(
    Option(label = "Edit card", icon = Icons.Filled.Edit, onClick = {}),
    Option(label = "Delete card", icon = Icons.Filled.DeleteForever, onClick = {}),
  )
}
