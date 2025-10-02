package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsGroup(modifier: Modifier = Modifier, title: String, content: @Composable () -> Unit) {
  Column(modifier = modifier) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.primary,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )

    Surface(
      modifier = Modifier.fillMaxWidth(),
      color = MaterialTheme.colorScheme.surface,
      shape = MaterialTheme.shapes.extraLarge,
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(2.dp)) { content() }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsGroup() {
  SettingsGroup(title = "About") {
    SettingsItem(
      icon = Icons.Default.Web,
      iconColor = MaterialTheme.colorScheme.primaryContainer,
      iconBackground = MaterialTheme.colorScheme.onPrimaryContainer,
      title = "pawcode Development",
      subtitle = "View website",
      onClick = {},
    )
    SettingsItem(
      icon = Icons.Default.Web,
      iconColor = MaterialTheme.colorScheme.secondaryContainer,
      iconBackground = MaterialTheme.colorScheme.onSecondaryContainer,
      title = "pawcode Development",
      subtitle = "View website",
      onClick = {},
    )
    SettingsItem(
      icon = Icons.Default.Web,
      iconColor = MaterialTheme.colorScheme.tertiaryContainer,
      iconBackground = MaterialTheme.colorScheme.onTertiaryContainer,
      title = "pawcode Development",
      subtitle = "View website",
      onClick = {},
    )
  }
}
