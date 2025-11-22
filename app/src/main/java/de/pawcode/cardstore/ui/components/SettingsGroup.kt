package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R

@Composable
fun SettingsGroup(modifier: Modifier = Modifier, title: String, content: @Composable () -> Unit) {
  Column(modifier = modifier) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.primary,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )

    Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.extraLarge) {
      Column(verticalArrangement = Arrangement.spacedBy(2.dp)) { content() }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsGroup() {
  SettingsGroup(title = "About") {
    SettingsItem(
      icon = painterResource(R.drawable.web_solid),
      iconColor = MaterialTheme.colorScheme.onPrimaryFixedVariant,
      iconBackground = MaterialTheme.colorScheme.primaryFixed,
      title = "pawcode Development",
      subtitle = "View website",
      onClick = {},
    )
    SettingsItem(
      icon = painterResource(R.drawable.web_solid),
      iconColor = MaterialTheme.colorScheme.onSecondaryFixedVariant,
      iconBackground = MaterialTheme.colorScheme.secondaryFixed,
      title = "pawcode Development",
    )
    SettingsItem(
      icon = painterResource(R.drawable.web_solid),
      iconColor = MaterialTheme.colorScheme.onTertiaryFixedVariant,
      iconBackground = MaterialTheme.colorScheme.tertiaryFixed,
      title = "pawcode Development",
      subtitle = "View website",
    )
  }
}
