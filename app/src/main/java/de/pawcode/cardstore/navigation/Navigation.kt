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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.pawcode.cardstore.data.services.DeeplinkService
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.ui.screens.AboutScreen
import de.pawcode.cardstore.ui.screens.CardListScreen
import de.pawcode.cardstore.ui.screens.EditCardScreen
import de.pawcode.cardstore.ui.screens.EditLabelScreen
import de.pawcode.cardstore.ui.screens.LabelListScreen
import de.pawcode.cardstore.utils.mapBarcodeFormat

@SuppressLint(
  "UnusedMaterial3ScaffoldPaddingParameter",
  "UseKtx",
  "CoroutineCreationDuringComposition",
)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
  val navController = rememberNavController()
  val hasDeeplink by DeeplinkService.hasDeeplinkFlow.collectAsState(initial = false)

  LaunchedEffect(hasDeeplink) {
    if (hasDeeplink && navController.currentDestination?.route != Screen.CardList.route) {
      navController.navigate(Screen.CardList.route) {
        popUpTo(Screen.CardList.route) { inclusive = true }
        launchSingleTop = true
      }
    }
  }

  Scaffold(
    modifier = modifier,
    snackbarHost = {
      SnackbarHost(
        hostState = SnackbarService.snackbarHostState,
        snackbar = { snackbarData ->
          Snackbar(
            snackbarData = snackbarData,
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            actionColor = MaterialTheme.colorScheme.inversePrimary,
          )
        },
      )
    },
  ) { _ ->
    NavHost(
      navController = navController,
      startDestination = Screen.CardList.route,
      enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
      exitTransition = { ExitTransition.None },
      popEnterTransition = { EnterTransition.None },
      popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
    ) {
      composable(Screen.CardList.route) { CardListScreen(navController = navController) }
      composable(
        route =
          Screen.EditCard.route +
            "?cardId={cardId}&cardNumber={cardNumber}&barcodeFormat={barcodeFormat}&storeName={storeName}&color={color}",
        arguments =
          listOf(
            navArgument("cardId") {
              type = NavType.StringType
              nullable = true
            },
            navArgument("cardNumber") {
              type = NavType.StringType
              nullable = true
            },
            navArgument("barcodeFormat") {
              type = NavType.StringType
              nullable = true
            },
            navArgument("storeName") {
              type = NavType.StringType
              nullable = true
            },
            navArgument("color") {
              type = NavType.StringType
              nullable = true
            },
          ),
      ) { entry ->
        val cardId = entry.arguments?.getString("cardId")
        val storeName = entry.arguments?.getString("storeName")
        val cardNumber = entry.arguments?.getString("cardNumber")
        val barcodeType = entry.arguments?.getString("barcodeFormat")?.let { mapBarcodeFormat(it) }
        val color = entry.arguments?.getString("color")?.toIntOrNull()

        EditCardScreen(
          navController = navController,
          cardId = cardId,
          storeName = storeName,
          cardNumber = cardNumber,
          barcodeType = barcodeType,
          color = color,
        )
      }
      composable(Screen.LabelList.route) { LabelListScreen(navController = navController) }
      composable(
        route = Screen.EditLabel.route + "?labelId={labelId}",
        arguments =
          listOf(
            navArgument("labelId") {
              type = NavType.StringType
              nullable = true
            }
          ),
      ) { entry ->
        val labelId = entry.arguments?.getString("labelId")
        EditLabelScreen(navController = navController, labelId = labelId)
      }
      composable(Screen.About.route) { AboutScreen(navController = navController) }
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
