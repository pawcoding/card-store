package de.pawcode.cardstore.data.services

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.simonsickle.compose.barcodes.BarcodeType
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.utils.mapBarcodeFormat
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

object DeeplinkService {
  private val _deeplinkFlow = MutableSharedFlow<CardEntity?>(replay = 1)

  val deeplinkFlow = _deeplinkFlow.map { it }
  val hasDeeplinkFlow = _deeplinkFlow.map { card -> card != null }

  @OptIn(ExperimentalUuidApi::class)
  fun deeplinkReceived(deeplink: Map<String, String?>) {
    val card =
      CardEntity(
        cardId = Uuid.random().toString(),
        storeName = deeplink["storeName"] ?: "",
        cardNumber = deeplink["cardNumber"] ?: "",
        barcodeFormat =
          deeplink["barcodeFormat"]?.let { mapBarcodeFormat(it) } ?: BarcodeType.QR_CODE,
        color = deeplink["color"]?.toIntOrNull() ?: Color.White.toArgb(),
      )
    _deeplinkFlow.tryEmit(card)
  }

  fun clearDeeplink() {
    _deeplinkFlow.tryEmit(null)
  }
}
