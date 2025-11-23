package de.pawcode.cardstore.ui

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import de.pawcode.cardstore.data.services.DeeplinkService
import de.pawcode.cardstore.data.services.SnackbarService
import de.pawcode.cardstore.navigation.Navigator
import de.pawcode.cardstore.navigation.ScreenAbout
import de.pawcode.cardstore.navigation.ScreenCardEdit
import de.pawcode.cardstore.navigation.ScreenCardList
import de.pawcode.cardstore.navigation.ScreenLabelEdit
import de.pawcode.cardstore.navigation.ScreenLabelList
import de.pawcode.cardstore.navigation.rememberNavigationState
import de.pawcode.cardstore.navigation.toEntries
import de.pawcode.cardstore.ui.screens.AboutScreen
import de.pawcode.cardstore.ui.screens.CardListScreen
import de.pawcode.cardstore.ui.screens.EditCardScreen
import de.pawcode.cardstore.ui.screens.EditLabelScreen
import de.pawcode.cardstore.ui.screens.LabelListScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CardStore(modifier: Modifier = Modifier) {
  val navigationState =
    rememberNavigationState(startRoute = ScreenCardList, topLevelRoutes = setOf(ScreenCardList))
  val navigator = remember { Navigator(navigationState) }
  val hasDeeplink by DeeplinkService.hasDeeplinkFlow.collectAsState(initial = false)

  LaunchedEffect(hasDeeplink) {
    if (
      hasDeeplink &&
        navigationState.backStacks[navigationState.topLevelRoute]?.last() == ScreenCardList
    ) {
      navigator.navigate(ScreenCardList)
    }
  }

  val entryProvider = entryProvider {
    entry<ScreenCardList> { CardListScreen(navigator) }
    entry<ScreenCardEdit> { data ->
      EditCardScreen(
        navigator,
        data.cardId,
        data.storeName,
        data.cardNumber,
        data.barcodeType,
        data.color,
      )
    }
    entry<ScreenLabelList> { LabelListScreen(navigator) }
    entry<ScreenLabelEdit> { data -> EditLabelScreen(navigator, data.labelId) }
    entry<ScreenAbout> { AboutScreen(navigator) }
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
    NavDisplay(
      entries = navigationState.toEntries(entryProvider),
      onBack = { navigator.goBack() },
      sceneStrategy = remember { DialogSceneStrategy() },
      transitionSpec = {
        slideInHorizontally(initialOffsetX = { it }) togetherWith ExitTransition.None
      },
      popTransitionSpec = {
        EnterTransition.None togetherWith slideOutHorizontally(targetOffsetX = { it })
      },
    )
  }
}
