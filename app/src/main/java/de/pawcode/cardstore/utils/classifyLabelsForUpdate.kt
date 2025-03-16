package de.pawcode.cardstore.utils

import de.pawcode.cardstore.data.database.entities.LabelEntity

fun classifyLabelsForUpdate(
    initialLabels: List<LabelEntity>,
    updatedLabels: List<LabelEntity>
): Pair<List<String>, List<String>> {
    val initialLabelIds = initialLabels.map { it.labelId }.toSet()
    val updatedLabelIds = updatedLabels.map { it.labelId }.toSet()

    val labelsToAdd = updatedLabelIds.minus(initialLabelIds)
    val labelsToRemove = initialLabelIds.minus(updatedLabelIds)

    return Pair(labelsToAdd.toList(), labelsToRemove.toList())
}