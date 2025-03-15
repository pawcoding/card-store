package de.pawcode.cardstore.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NewLabel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.emptyLabel
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.viewmodels.CardViewModel

@Composable
fun EditLabelScreen(
    navController: NavController,
    labelId: String? = null,
    viewModel: CardViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()

    val initialLabel by viewModel.getLabelById(labelId).collectAsState(initial = null)
    var label by remember { mutableStateOf(initialLabel ?: emptyLabel()) }

    LaunchedEffect(initialLabel) {
        label = initialLabel ?: emptyLabel()
    }

    val isValid by remember { derivedStateOf { label.name.isNotEmpty() } }
    val hasChanges by remember {
        derivedStateOf { initialLabel == null || initialLabel!!.name != label.name }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            AppBar(
                title = stringResource(if (labelId != null) R.string.label_edit else R.string.label_add),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = hasChanges,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                val snackbarMessage = stringResource(
                    if (labelId != null) R.string.label_updated else R.string.label_added
                )

                ExtendedFloatingActionButton(
                    onClick = {
                        if (!isValid) {
                            return@ExtendedFloatingActionButton
                        }

                        if (initialLabel != null) {
                            viewModel.updateLabel(label)
                        } else {
                            viewModel.insertLabel(label)
                        }

                        navController.popBackStack()

                        SnackbarService.showSnackbar(
                            message = snackbarMessage,
                            scope = scope
                        )
                    },
                    text = {
                        if (labelId != null) {
                            Text(stringResource(R.string.common_update))
                        } else {
                            Text(stringResource(R.string.common_save))
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Save,
                            contentDescription = stringResource(R.string.label_save)
                        )
                    },
                    containerColor = if (isValid) MaterialTheme.colorScheme.primary else Color.Gray,
                    contentColor = if (isValid) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.38f
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    Icons.Filled.NewLabel,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )

                Text(
                    text = stringResource(R.string.label_name),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            OutlinedTextField(
                value = label.name,
                onValueChange = { label = label.copy(name = it) },
                label = { Text(stringResource(R.string.label_name) + "*") },
                supportingText = { Text("*" + stringResource(R.string.common_required)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrectEnabled = true,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                )
            )
        }
    }
}