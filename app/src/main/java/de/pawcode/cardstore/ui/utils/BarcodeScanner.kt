package de.pawcode.cardstore.ui.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.services.SnackbarService

@Composable
fun BarcodeScanner(onBarcodeDetected: (Barcode) -> Unit, onCancel: () -> Unit) {
  val context = LocalContext.current

  // Set up the barcode scanner options
  val options =
    GmsBarcodeScannerOptions.Builder()
      .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
      .enableAutoZoom()
      .build()

  /** Handles errors during the barcode scanning process. */
  fun handleError(exception: Exception) {
    SnackbarService.showSnackbar(
      message = context.getString(R.string.scan_error),
      actionLabel = context.getString(R.string.copy_error),
      onAction = {
        val clipboardManager =
          context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip =
          ClipData.newPlainText(
            "Barcode Scan Error",
            "${exception.message}\n${Log.getStackTraceString(exception)}",
          )
        clipboardManager.setPrimaryClip(clip)

        SnackbarService.showSnackbar(message = context.getString(R.string.copied_to_clipboard))
      },
    )
    onCancel()
  }

  val moduleInstallClient = ModuleInstall.getClient(context)
  val barcodeScannerModule = GmsBarcodeScanning.getClient(context)

  /** Starts the barcode scanning process. */
  fun scan() {
    val barcodeScanner = GmsBarcodeScanning.getClient(context, options)
    barcodeScanner
      .startScan()
      .addOnSuccessListener { onBarcodeDetected(it) }
      .addOnCanceledListener { onCancel() }
      .addOnFailureListener { handleError(it) }
  }

  // Check if the barcode scanner module is available
  moduleInstallClient
    .areModulesAvailable(barcodeScannerModule)
    .addOnSuccessListener {
      if (it.areModulesAvailable()) {
        // Module is already available, start scanning
        scan()
      } else {
        // Module is not available, request installation
        val moduleInstallRequest =
          ModuleInstallRequest.newBuilder().addApi(barcodeScannerModule).build()

        moduleInstallClient
          .installModules(moduleInstallRequest)
          // Module installation was successful, start scanning
          .addOnSuccessListener { scan() }
          // Module installation failed, handle the error
          .addOnFailureListener { handleError(it) }
      }
    }
    // Module availability check failed, handle the error
    .addOnFailureListener { handleError(it) }
}
