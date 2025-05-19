package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
  title: String,
  onBack: (() -> Unit)? = null,
  actions: @Composable (RowScope.() -> Unit)? = null,
) {
  TopAppBar(
    title = { Text(text = title) },
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
        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
    onBack = {},
    actions = {
      IconButton(onClick = {}) {
        Icon(
          Icons.AutoMirrored.Filled.Sort,
          contentDescription = stringResource(R.string.cards_sort),
        )
      }
    },
  )
}
