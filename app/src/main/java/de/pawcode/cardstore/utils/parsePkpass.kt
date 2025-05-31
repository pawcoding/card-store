package de.pawcode.cardstore.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

val RGB_REGEX = Regex("""rgb\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3})\)""")

fun parsePkpass(passContent: String): Map<String, String?>? {
  if (passContent.isEmpty()) {
    return null
  }

  val json = Json.parseToJsonElement(passContent).jsonObject
  // extract store name
  val description = json["description"]?.jsonPrimitive?.content
  // extract card number
  val barcode = json["barcodes"]?.jsonArray?.get(0)?.jsonObject ?: json["barcode"]?.jsonObject
  val content = barcode?.get("message")?.jsonPrimitive?.content
  // extract barcode format
  val format = barcode?.get("format")?.jsonPrimitive?.content
  val barcodeFormat = format?.let { mapPkpassBarcodeFormat(it) }
  // extract color
  val rgbString = json["backgroundColor"]?.jsonPrimitive?.content
  val rgb = rgbString?.let { RGB_REGEX.find(it)?.groupValues?.drop(1)?.map { it.toInt() } }
  val color = rgb?.let { Color(it[0], it[1], it[2]) }

  return mapOf(
    "storeName" to description,
    "cardNumber" to content,
    "barcodeFormat" to barcodeFormat?.toString(),
    "color" to color?.toArgb()?.toString(),
  )
}
