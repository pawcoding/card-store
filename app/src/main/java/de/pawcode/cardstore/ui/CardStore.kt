package de.pawcode.cardstore.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import de.pawcode.cardstore.navigation.Navigator
import de.pawcode.cardstore.navigation.ScreenAbout
import de.pawcode.cardstore.navigation.ScreenCardEdit
import de.pawcode.cardstore.navigation.ScreenCardList
import de.pawcode.cardstore.navigation.ScreenLabelEdit
import de.pawcode.cardstore.navigation.ScreenLabelList
import de.pawcode.cardstore.navigation.rememberNavigationState
import de.pawcode.cardstore.ui.screens.AboutScreen
import de.pawcode.cardstore.ui.screens.CardListScreen
import de.pawcode.cardstore.ui.screens.EditCardScreen
import de.pawcode.cardstore.ui.screens.EditLabelScreen
import de.pawcode.cardstore.ui.screens.LabelListScreen

@Composable
fun CardStore(modifier: Modifier = Modifier) {
  val navigationState =
    rememberNavigationState(startRoute = ScreenCardList, topLevelRoutes = setOf(ScreenCardList))

  val navigator = remember { Navigator(navigationState) }

  entryProvider {
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
}
