package de.pawcode.cardstore.utils

import android.content.ContentResolver
import android.net.Uri
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun readPkpassContentFromUri(uri: Uri, contentResolver: ContentResolver): String {
  try {
    val inputStream = contentResolver.openInputStream(uri) ?: return ""

    ZipInputStream(inputStream).use { zipInputStream ->
      var entry: ZipEntry?
      while (zipInputStream.nextEntry.also { entry = it } != null) {
        if (entry?.name == "pass.json") {
          return String(zipInputStream.readBytes(), Charsets.UTF_8)
        }
      }
    }
  } catch (exception: Exception) {
    println("Error reading content from file: ${exception.message}")
    return ""
  }

  return ""
}
