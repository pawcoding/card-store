package de.pawcode.cardstore.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.ui.screens.AddEditCardScreen
import de.pawcode.cardstore.ui.screens.CardListScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val navController = rememberNavController()

    Scaffold(snackbarHost = {
        SnackbarHost(
            hostState = SnackbarService.snackbarHostState, snackbar = { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionColor = MaterialTheme.colorScheme.secondary
                )
            })
    }) { _ ->
        NavHost(
            navController = navController,
            startDestination = Screen.CardList.route,
        ) {
            composable(Screen.CardList.route) {
                CardListScreen(navController = navController)
            }
            composable(
                route = Screen.AddEditCard.route + "?cardId={cardId}",
                arguments = listOf(navArgument("cardId") {
                    type = NavType.StringType
                    nullable = true
                })
            ) { entry ->
                val cardId = entry.arguments?.getString("cardId")
                AddEditCardScreen(navController = navController, cardId = cardId)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object CardList : Screen("card_list")
    object AddEditCard : Screen("add_edit_card")
}

