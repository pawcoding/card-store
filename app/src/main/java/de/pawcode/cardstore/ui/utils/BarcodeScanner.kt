package de.pawcode.cardstore.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun BarcodeScanner(onBarcodeDetected: (Barcode) -> Unit, onCancel: () -> Unit) {
  val context = LocalContext.current

  val options =
    GmsBarcodeScannerOptions.Builder()
      .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
      .enableAutoZoom()
      .build()

  val scanner = GmsBarcodeScanning.getClient(context, options)

  scanner
    .startScan()
    .addOnSuccessListener { onBarcodeDetected(it) }
    .addOnCanceledListener { onCancel }
    .addOnFailureListener { onCancel }
}
