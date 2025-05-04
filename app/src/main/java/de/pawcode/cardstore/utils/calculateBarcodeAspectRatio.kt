package de.pawcode.cardstore.utils

import com.simonsickle.compose.barcodes.BarcodeType

fun calculateBarcodeAspectRatio(barcodeType: BarcodeType): Float {
  return when (barcodeType) {
    BarcodeType.EAN_8,
    BarcodeType.UPC_E -> 3.0f / 2.0f

    BarcodeType.EAN_13,
    BarcodeType.UPC_A -> 4.0f / 3.0f

    BarcodeType.QR_CODE,
    BarcodeType.DATA_MATRIX,
    BarcodeType.AZTEC -> 1.0f

    BarcodeType.CODE_39,
    BarcodeType.CODE_93,
    BarcodeType.CODE_128,
    BarcodeType.ITF,
    BarcodeType.CODABAR -> 3.0f / 1.0f

    BarcodeType.PDF_417 -> 4.0f / 1.0f
  }
}
