package de.pawcode.cardstore.ui.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.services.SnackbarService

@Composable
fun BarcodeScanner(onBarcodeDetected: (Barcode) -> Unit, onCancel: () -> Unit) {
  val context = LocalContext.current

  val options =
    GmsBarcodeScannerOptions.Builder()
      .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
      .enableAutoZoom()
      .build()

  val scanner = GmsBarcodeScanning.getClient(context, options)

  val errorMessage = stringResource(R.string.scan_error)
  val reportError = stringResource(R.string.copy_error)
  val errorCopied = stringResource(R.string.copied_to_clipboard)

  scanner
    .startScan()
    .addOnSuccessListener { onBarcodeDetected(it) }
    .addOnCanceledListener { onCancel() }
    .addOnFailureListener { exception ->
      SnackbarService.showSnackbar(
        message = errorMessage,
        actionLabel = reportError,
        onAction = {
          val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
          val clip =
            ClipData.newPlainText(
              "Barcode Scan Error",
              "${exception.message}\n${Log.getStackTraceString(exception)}",
            )
          clipboardManager.setPrimaryClip(clip)

          SnackbarService.showSnackbar(message = errorCopied)
        },
      )
      onCancel()
    }
}
