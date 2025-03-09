package de.pawcode.cardstore.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.ui.screens.AddEditCardScreen
import de.pawcode.cardstore.ui.screens.CardListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = navigationIcon ?: {},
        actions = actions ?: {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Scaffold(topBar = {
        var currentRoute = currentDestination(navController)
        when (currentRoute?.route) {
            Screen.CardList.route -> AppBar(title = "Card Store")
            Screen.AddEditCard.route + "?cardId={cardId}" -> AppBar(
                title = "Add/Edit Card", navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                })

            else -> AppBar(title = "Card Store")
        }
    }, snackbarHost = {
        SnackbarHost(
            hostState = SnackbarService.snackbarHostState, snackbar = { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionColor = MaterialTheme.colorScheme.secondary
                )
            })
    }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.CardList.route,
            modifier = Modifier.padding(paddingValues)
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

@Composable
fun currentDestination(navController: NavController): NavDestination? {
    val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial = null)
    return navBackStackEntry?.destination
}

sealed class Screen(val route: String) {
    object CardList : Screen("card_list")
    object AddEditCard : Screen("add_edit_card")
}

