package de.pawcode.cardstore.utils

import de.pawcode.cardstore.data.database.classes.CardWithLabels
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.LabelEntity

fun hasCardChanged(initialCard: CardWithLabels, card: CardWithLabels): Boolean {
  return hasOnlyCardChanged(initialCard.card, card.card) ||
    haveLabelsChanged(initialCard.labels, card.labels)
}

private fun hasOnlyCardChanged(initialCard: CardEntity, card: CardEntity): Boolean {
  return initialCard.storeName != card.storeName ||
    initialCard.cardNumber != card.cardNumber ||
    initialCard.color != card.color ||
    initialCard.barcodeFormat != card.barcodeFormat
}

private fun haveLabelsChanged(
  initialLabels: List<LabelEntity>,
  labels: List<LabelEntity>,
): Boolean {
  val initialLabelIds = initialLabels.map { it.labelId }.toSet()
  val labelIds = labels.map { it.labelId }.toSet()

  return initialLabelIds != labelIds
}
