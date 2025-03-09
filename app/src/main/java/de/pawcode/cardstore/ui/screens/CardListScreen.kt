package de.pawcode.cardstore.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.navigation.Screen
import de.pawcode.cardstore.ui.components.CardsListComponent
import de.pawcode.cardstore.ui.viewmodels.CardViewModel


@Composable
fun CardListScreen(navController: NavController, viewModel: CardViewModel = viewModel()) {
    val cards by viewModel.allCards.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditCard.route)
                },
                text = { Text("Add new card") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Add new card") })
        }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            CardsListComponent(
                cards = cards,
                onCardClicked = {
                    navController.navigate(Screen.CardDetail.route + "/${it.id}")
                },
                onCardLongPressed = {
                    navController.navigate(Screen.AddEditCard.route + "?cardId=${it.id}")
                }
            )
        }
    }
}