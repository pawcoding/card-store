package de.pawcode.cardstore.utils

import com.google.mlkit.vision.barcode.common.Barcode
import com.simonsickle.compose.barcodes.BarcodeType

fun mapBarcodeFormat(barcodeFormat: Int): BarcodeType {
  return when (barcodeFormat) {
    Barcode.FORMAT_AZTEC -> BarcodeType.AZTEC
    Barcode.FORMAT_CODABAR -> BarcodeType.CODABAR
    Barcode.FORMAT_CODE_128 -> BarcodeType.CODE_128
    Barcode.FORMAT_CODE_39 -> BarcodeType.CODE_39
    Barcode.FORMAT_CODE_93 -> BarcodeType.CODE_93
    Barcode.FORMAT_DATA_MATRIX -> BarcodeType.DATA_MATRIX
    Barcode.FORMAT_EAN_13 -> BarcodeType.EAN_13
    Barcode.FORMAT_EAN_8 -> BarcodeType.EAN_8
    Barcode.FORMAT_ITF -> BarcodeType.ITF
    Barcode.FORMAT_PDF417 -> BarcodeType.PDF_417
    Barcode.FORMAT_QR_CODE -> BarcodeType.QR_CODE
    Barcode.FORMAT_UPC_A -> BarcodeType.UPC_A
    Barcode.FORMAT_UPC_E -> BarcodeType.UPC_E
    else -> throw Error("Unknown barcode format")
  }
}
