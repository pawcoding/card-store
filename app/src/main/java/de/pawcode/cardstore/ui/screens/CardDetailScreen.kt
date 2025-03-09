package de.pawcode.cardstore.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.pawcode.cardstore.ui.viewmodels.CardViewModel

@Composable
fun CardDetailScreen(
    navController: NavController,
    cardId: String,
    viewModel: CardViewModel = viewModel()
) {
}