package de.pawcode.cardstore

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.simonsickle.compose.barcodes.BarcodeType
import de.pawcode.cardstore.ui.screens.AboutScreen
import de.pawcode.cardstore.ui.screens.CardListScreen
import de.pawcode.cardstore.ui.screens.EditCardScreen
import de.pawcode.cardstore.ui.screens.EditLabelScreen
import de.pawcode.cardstore.ui.screens.LabelListScreen

data object ScreenCardList : NavKey

data class ScreenCardEdit(
  val cardId: String? = null,
  val cardNumber: String? = null,
  val barcodeType: BarcodeType? = null,
  val storeName: String? = null,
  val color: Int? = null,
) : NavKey

data object ScreenLabelList : NavKey

data class ScreenLabelEdit(val labelId: String?) : NavKey

data object ScreenAbout : NavKey

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CardStore() {
  val backStack = remember { mutableStateListOf<Any>(ScreenCardList) }

  Scaffold { _ ->
    NavDisplay(
      backStack = backStack,
      onBack = { backStack.removeLastOrNull() },
      transitionSpec = {
        slideInHorizontally(initialOffsetX = { it }) togetherWith ExitTransition.None
      },
      popTransitionSpec = {
        EnterTransition.None togetherWith slideOutHorizontally(targetOffsetX = { it })
      },
      predictivePopTransitionSpec = {
        EnterTransition.None togetherWith slideOutHorizontally(targetOffsetX = { it })
      },
      entryProvider =
        entryProvider {
          entry<ScreenCardList> { CardListScreen(backStack) }
          entry<ScreenCardEdit> { key ->
            EditCardScreen(
              backStack,
              cardId = key.cardId,
              storeName = key.storeName,
              cardNumber = key.cardNumber,
              barcodeType = key.barcodeType,
              color = key.color,
            )
          }
          entry<ScreenLabelList> { LabelListScreen(backStack) }
          entry<ScreenLabelEdit> { key -> EditLabelScreen(backStack, labelId = key.labelId) }
          entry<ScreenAbout> { AboutScreen(backStack) }
        },
    )
  }
}
