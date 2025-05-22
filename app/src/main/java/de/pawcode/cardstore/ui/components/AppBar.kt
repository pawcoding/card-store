package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.pawcode.cardstore.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppBar(
  title: String,
  subtitle: String? = null,
  onBack: (() -> Unit)? = null,
  actions: @Composable (RowScope.() -> Unit)? = null,
) {
  TopAppBar(
    title = { Text(text = title) },
    subtitle = {
      if (subtitle != null) {
        Text(text = subtitle)
      }
    },
    navigationIcon = {
      if (onBack != null) {
        IconButton(onClick = { onBack() }) {
          Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.common_back),
          )
        }
      }
    },
    actions = actions ?: {},
    colors =
      TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      ),
  )
}

@Preview
@Composable
fun PreviewAppBar() {
  AppBar(title = "Card Store")
}

@Preview
@Composable
fun PreviewAppBarActions() {
  AppBar(
    title = "Card Store",
    subtitle = "My cool card",
    onBack = {},
    actions = {
      FilledIconButton(onClick = {}) {
        Icon(
          Icons.AutoMirrored.Filled.Sort,
          contentDescription = stringResource(R.string.cards_sort),
        )
      }
    },
  )
}
