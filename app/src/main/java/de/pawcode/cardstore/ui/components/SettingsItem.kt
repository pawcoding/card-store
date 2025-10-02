package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsItem(
  icon: ImageVector,
  iconColor: Color,
  iconBackground: Color,
  title: String,
  subtitle: String? = null,
  onClick: (() -> Unit)?,
) {
  Row(
    modifier =
      Modifier.fillMaxWidth()
        .background(
          color = MaterialTheme.colorScheme.surfaceContainer,
          shape = MaterialTheme.shapes.extraSmall,
        )
        .clickable { onClick?.invoke() }
        .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Box(
      modifier = Modifier.size(40.dp).clip(CircleShape).background(iconBackground),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = iconColor,
        modifier = Modifier.size(20.dp),
      )
    }

    Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
      )
      subtitle?.let {
        Text(
          text = it,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsItem() {
  Column {
    SettingsItem(
      icon = Icons.Default.Web,
      iconColor = MaterialTheme.colorScheme.onPrimary,
      iconBackground = MaterialTheme.colorScheme.primary,
      title = "pawcode Development",
      subtitle = "View website",
      onClick = {},
    )
    SettingsItem(
      icon = Icons.Default.Web,
      iconColor = MaterialTheme.colorScheme.onSecondary,
      iconBackground = MaterialTheme.colorScheme.secondary,
      title = "pawcode Development",
      subtitle = "View website",
      onClick = {},
    )
    SettingsItem(
      icon = Icons.Default.Web,
      iconColor = MaterialTheme.colorScheme.onTertiary,
      iconBackground = MaterialTheme.colorScheme.tertiary,
      title = "pawcode Development",
      subtitle = "View website",
      onClick = {},
    )
  }
}
