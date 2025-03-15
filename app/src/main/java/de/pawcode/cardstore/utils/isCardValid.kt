package de.pawcode.cardstore.utils

import de.pawcode.cardstore.data.database.entities.CardEntity

fun isCardValid(
    card: CardEntity
): Boolean {
    return card.storeName.isNotEmpty()
            && card.cardNumber.isNotEmpty()
            && card.barcodeFormat.isValueValid(card.cardNumber)
}