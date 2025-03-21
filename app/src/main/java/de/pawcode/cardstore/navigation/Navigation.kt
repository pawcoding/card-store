package de.pawcode.cardstore.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import de.pawcode.cardstore.ui.screens.AboutScreen
import de.pawcode.cardstore.ui.screens.CardListScreen
import de.pawcode.cardstore.ui.screens.EditCardScreen
import de.pawcode.cardstore.ui.screens.EditLabelScreen
import de.pawcode.cardstore.ui.screens.LabelListScreen

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
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            composable(Screen.CardList.route) {
                CardListScreen(navController = navController)
            }
            composable(
                route = Screen.EditCard.route + "?cardId={cardId}",
                arguments = listOf(navArgument("cardId") {
                    type = NavType.StringType
                    nullable = true
                })
            ) { entry ->
                val cardId = entry.arguments?.getString("cardId")
                EditCardScreen(navController = navController, cardId = cardId)
            }
            composable(Screen.LabelList.route) {
                LabelListScreen(navController = navController)
            }
            composable(
                route = Screen.EditLabel.route + "?labelId={labelId}",
                arguments = listOf(navArgument("labelId") {
                    type = NavType.StringType
                    nullable = true
                })
            ) { entry ->
                val labelId = entry.arguments?.getString("labelId")
                EditLabelScreen(navController = navController, labelId = labelId)
            }
            composable(Screen.About.route) {
                AboutScreen(navController = navController)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object CardList : Screen("card_list")
    object EditCard : Screen("edit_card")
    object LabelList : Screen("label_list")
    object EditLabel : Screen("edit_label")
    object About : Screen("about")
}

