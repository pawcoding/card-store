package de.pawcode.cardstore.utils

import android.net.Uri
import androidx.core.net.toUri

fun parseDeeplink(deeplink: String): Map<String, String?>? {
  if (deeplink.isEmpty()) {
    return null
  }

  return parseDeeplink(deeplink.toUri())
}

fun parseDeeplink(deeplink: Uri): Map<String, String?>? {
  if (deeplink.host != "cardstore.apps.pawcode.de" || deeplink.path != "/share-card") {
    return null
  }

  return mapOf(
    "cardId" to (deeplink.getQueryParameter("cardId")),
    "storeName" to (deeplink.getQueryParameter("storeName")),
    "cardNumber" to (deeplink.getQueryParameter("cardNumber")),
    "barcodeFormat" to (deeplink.getQueryParameter("barcodeFormat")),
    "color" to (deeplink.getQueryParameter("color")),
  )
}
