package de.pawcode.cardstore.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.twotone.DeleteForever
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.data.database.entities.emptyLabel
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.dialogs.ConfirmDialog
import de.pawcode.cardstore.ui.sheets.Option
import de.pawcode.cardstore.ui.sheets.OptionSheet
import de.pawcode.cardstore.ui.viewmodels.CardViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterListScreen(
    navController: NavController,
    viewModel: CardViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()

    val labels by viewModel.allLabels.collectAsState(initial = emptyList())

    var showLabelOptionSheet by remember { mutableStateOf<LabelEntity?>(null) }
    val cardOptionSheetState = rememberModalBottomSheetState()

    var openDeleteDialog by remember { mutableStateOf<LabelEntity?>(null) }

    Scaffold(
        topBar = {
            AppBar(
                title = "Labels",
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.insertLabel(
                        emptyLabel().copy(name = "Test label")
                    )
                },
                text = { Text("Add new label") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add new label") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            if (labels.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Create your first label to categorize your cards",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                labels.forEachIndexed { index, label ->
                    Text(
                        text = label.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = { showLabelOptionSheet = label }
                                )
                            }
                    )

                    HorizontalDivider()
                }
            }
        }

        showLabelOptionSheet?.let {
            ModalBottomSheet(
                modifier = Modifier.safeDrawingPadding(),
                sheetState = cardOptionSheetState,
                onDismissRequest = { showLabelOptionSheet = null }
            ) {
                OptionSheet(
                    listOf(
                        Option(
                            label = "Edit label",
                            icon = Icons.Filled.Edit,
                            onClick = {
                                showLabelOptionSheet = null
                            }
                        ),
                        Option(
                            label = "Delete label",
                            icon = Icons.Filled.DeleteForever,
                            onClick = {
                                openDeleteDialog = showLabelOptionSheet!!
                                showLabelOptionSheet = null
                            }
                        )
                    )
                )
            }
        }

        openDeleteDialog?.let {
            ConfirmDialog(
                onDismissRequest = { openDeleteDialog = null },
                onConfirmation = {
                    scope.launch {
                        viewModel.deleteLabel(openDeleteDialog!!)
                        openDeleteDialog = null
                    }
                },
                dialogTitle = "Delete label",
                dialogText = "Are you sure you want to delete the label? It cannot be restored.",
                Icons.TwoTone.DeleteForever
            )
        }
    }
}