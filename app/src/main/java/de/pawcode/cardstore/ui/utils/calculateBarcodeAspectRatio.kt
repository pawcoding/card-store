package de.pawcode.cardstore.ui.utils

import com.simonsickle.compose.barcodes.BarcodeType

fun calculateBarcodeAspectRatio(barcodeType: BarcodeType): Float {
    return when (barcodeType) {
        BarcodeType.EAN_8 -> 3.0f / 2.0f
        BarcodeType.UPC_E -> 3.0f / 2.0f
        BarcodeType.EAN_13 -> 4.0f / 3.0f
        BarcodeType.UPC_A -> 4.0f / 3.0f
        BarcodeType.QR_CODE -> 1.0f
        BarcodeType.CODE_39 -> 3.0f / 1.0f
        BarcodeType.CODE_93 -> 3.0f / 1.0f
        BarcodeType.CODE_128 -> 3.0f / 1.0f
        BarcodeType.ITF -> 3.0f / 1.0f
        BarcodeType.PDF_417 -> 4.0f / 1.0f
        BarcodeType.CODABAR -> 3.0f / 1.0f
        BarcodeType.DATA_MATRIX -> 1.0f
        BarcodeType.AZTEC -> 1.0f
    }
}
