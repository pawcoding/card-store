package de.pawcode.cardstore.navigation

import androidx.navigation3.runtime.NavKey
import com.simonsickle.compose.barcodes.BarcodeType
import kotlinx.serialization.Serializable

@Serializable data object ScreenCardList : NavKey

@Serializable
data class ScreenCardEdit(
  val cardId: String? = null,
  val cardNumber: String? = null,
  val barcodeType: BarcodeType? = null,
  val storeName: String? = null,
  val color: Int? = null,
) : NavKey

@Serializable data object ScreenLabelList : NavKey

@Serializable data class ScreenLabelEdit(val labelId: String?) : NavKey

@Serializable data object ScreenAbout : NavKey
