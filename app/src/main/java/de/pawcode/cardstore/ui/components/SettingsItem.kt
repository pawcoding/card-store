package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R

@Composable
fun SettingsItem(
  icon: Painter,
  iconColor: Color,
  iconBackground: Color,
  title: String,
  subtitle: String? = null,
  onClick: (() -> Unit)? = null,
  trailingContent: @Composable (() -> Unit)? = null,
) {
  ListItem(
    modifier =
      Modifier.clip(MaterialTheme.shapes.extraSmall)
        .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
    tonalElevation = 2.dp,
    headlineContent = { Text(text = title) },
    supportingContent = { subtitle?.let { Text(text = it) } },
    trailingContent = trailingContent,
    leadingContent = {
      Box(
        modifier =
          Modifier.padding(vertical = 8.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(iconBackground),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          painter = icon,
          contentDescription = null,
          tint = iconColor,
          modifier = Modifier.size(20.dp),
        )
      }
    },
  )
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsItem() {
  Column {
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
      icon = painterResource(R.drawable.lock_solid),
      iconColor = MaterialTheme.colorScheme.onTertiaryFixedVariant,
      iconBackground = MaterialTheme.colorScheme.tertiaryFixed,
      title = "Restrict app access",
      subtitle = "Lock app with biometric security",
      trailingContent = { Switch(checked = false, onCheckedChange = {}) },
    )
  }
}
