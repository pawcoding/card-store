package de.pawcode.cardstore.utils

import android.graphics.Color
import org.json.JSONObject

fun parsePkpass(passContent: String): Map<String, String?>? {
  if (passContent.isEmpty()) {
    return null
  }
  val jsonObject = JSONObject(passContent)

  val organizationName = jsonObject.getString("organizationName")
  val content =
    if (jsonObject.has("barcodes")) {
      jsonObject.getJSONArray("barcodes").getJSONObject(0).getString("message")
    } else {
      jsonObject.getJSONObject("barcode").getString("message")
    }

  val backgroundColorRGB =
    jsonObject
      .getString("backgroundColor")
      .replace("rgb(", "")
      .replace(")", "")
      .split(", ") // remove unnecessary information
  val color =
    Color.rgb(
      backgroundColorRGB[0].toInt(),
      backgroundColorRGB[1].toInt(),
      backgroundColorRGB[2].toInt(),
    )

  return mapOf(
    "storeName" to organizationName,
    "cardNumber" to content,
    "barcodeFormat" to "QR_CODE",
    "color" to color.toString(),
  )
}
