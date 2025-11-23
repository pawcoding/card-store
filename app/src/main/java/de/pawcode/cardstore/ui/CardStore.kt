package de.pawcode.cardstore.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import de.pawcode.cardstore.navigation.Navigator
import de.pawcode.cardstore.navigation.ScreenCardList
import de.pawcode.cardstore.navigation.rememberNavigationState

@Composable
fun CardStore(modifier: Modifier = Modifier) {
  val navigationState =
    rememberNavigationState(startRoute = ScreenCardList, topLevelRoutes = setOf(ScreenCardList))

  val navigator = remember { Navigator(navigationState) }
}
