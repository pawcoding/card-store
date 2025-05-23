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
    else -> throw IllegalArgumentException("Unknown barcode format")
  }
}

fun mapBarcodeFormat(barcodeFormat: String): BarcodeType {
  return when (barcodeFormat) {
    "AZTEC" -> BarcodeType.AZTEC
    "CODABAR" -> BarcodeType.CODABAR
    "CODE_128" -> BarcodeType.CODE_128
    "CODE_39" -> BarcodeType.CODE_39
    "CODE_93" -> BarcodeType.CODE_93
    "DATA_MATRIX" -> BarcodeType.DATA_MATRIX
    "EAN_13" -> BarcodeType.EAN_13
    "EAN_8" -> BarcodeType.EAN_8
    "ITF" -> BarcodeType.ITF
    "PDF417" -> BarcodeType.PDF_417
    "QR_CODE" -> BarcodeType.QR_CODE
    "UPC_A" -> BarcodeType.UPC_A
    "UPC_E" -> BarcodeType.UPC_E
    else -> throw IllegalArgumentException("Unknown barcode format")
  }
}

fun mapPkpassBarcodeFormat(pkpassBarcodeFormat: String): BarcodeType {
  return when (pkpassBarcodeFormat) {
    "PKBarcodeFormatQR" -> BarcodeType.QR_CODE
    "PKBarcodeFormatPDF417" -> BarcodeType.PDF_417
    "PKBarcodeFormatAztec" -> BarcodeType.AZTEC
    "PKBarcodeFormatCode128" -> BarcodeType.CODE_128
    else -> throw IllegalArgumentException("Unknown barcode format")
  }
}
