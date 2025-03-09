package de.pawcode.cardstore.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.data.database.CardEntity
import de.pawcode.cardstore.navigation.Screen
import de.pawcode.cardstore.ui.components.CardsListComponent
import de.pawcode.cardstore.ui.sheets.ViewCardSheet
import de.pawcode.cardstore.ui.viewmodels.CardViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(navController: NavController, viewModel: CardViewModel = viewModel()) {
    val cards by viewModel.allCards.collectAsState(initial = emptyList())

    var showCardSheet by remember { mutableStateOf<CardEntity?>(null) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditCard.route)
                },
                text = { Text("Add new card") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add new card") })
        }) { innerPadding ->
        Column {
            CardsListComponent(cards = cards, onCardClicked = { card ->
                viewModel.addUsage(card)
                showCardSheet = card
            }, onCardLongPressed = {
                navController.navigate(Screen.AddEditCard.route + "?cardId=${it.id}")
            })

            showCardSheet?.let {
                ModalBottomSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .safeDrawingPadding(),
                    sheetState = sheetState,
                    onDismissRequest = { showCardSheet = null }) {
                    ViewCardSheet(showCardSheet!!)
                }
            }
        }
    }
}