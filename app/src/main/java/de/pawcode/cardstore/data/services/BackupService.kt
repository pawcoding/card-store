package de.pawcode.cardstore.data.services

import android.content.Context
import android.net.Uri
import com.simonsickle.compose.barcodes.BarcodeType
import de.pawcode.cardstore.data.database.classes.CardWithLabels
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.CardLabelCrossRef
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.data.models.BackupCardData
import de.pawcode.cardstore.data.models.BackupData
import de.pawcode.cardstore.data.models.BackupLabelData
import java.time.Instant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

object BackupService {
  private val json = Json { ignoreUnknownKeys = true }

  fun createBackup(
    context: Context,
    uri: Uri,
    cards: List<CardWithLabels>,
    labels: List<LabelEntity>,
  ) {
    val backup =
      BackupData(
        version = 1,
        createdAt = Instant.now().toString(),
        labels = labels.map { BackupLabelData(id = it.labelId, name = it.name) },
        cards =
          cards.map { cwl ->
            BackupCardData(
              id = cwl.card.cardId,
              storeName = cwl.card.storeName,
              cardNumber = cwl.card.cardNumber,
              barcodeFormat = cwl.card.barcodeFormat.name,
              color = cwl.card.color,
              logo = cwl.card.logo,
              lastUsed = cwl.card.lastUsed,
              useCount = cwl.card.useCount,
              isFavorite = cwl.card.isFavorite,
              labelIds = cwl.labels.map { it.labelId },
            )
          },
      )
    val jsonString = json.encodeToString(BackupData.serializer(), backup)
    context.contentResolver.openOutputStream(uri)?.use { it.write(jsonString.toByteArray()) }
      ?: error("Could not open output stream for URI: $uri")
  }

  fun readBackup(context: Context, uri: Uri): BackupData {
    val jsonString =
      context.contentResolver.openInputStream(uri)?.use { it.readBytes().decodeToString() }
        ?: error("Could not open input stream for URI: $uri")
    return json.decodeFromString(BackupData.serializer(), jsonString)
  }

  fun validateBackup(data: BackupData): Boolean {
    if (data.version != 1) return false
    if (data.createdAt.isBlank()) return false
    val labelIds = data.labels.map { it.id }.toSet()
    if (data.cards.any { card -> card.labelIds.any { it !in labelIds } }) return false
    data.cards.forEach { card ->
      runCatching { BarcodeType.valueOf(card.barcodeFormat) }
        .getOrElse {
          return false
        }
    }
    return true
  }

  suspend fun performCreateBackup(
    context: Context,
    uri: Uri,
    cardRepository: de.pawcode.cardstore.data.database.repositories.CardRepository,
    labelRepository: de.pawcode.cardstore.data.database.repositories.LabelRepository,
  ): Result<Unit> = runCatching {
    val cards = cardRepository.allCards.first()
    val labels = labelRepository.allLabels.first()
    withContext(Dispatchers.IO) { createBackup(context, uri, cards, labels) }
  }

  suspend fun performRestoreBackup(
    context: Context,
    uri: Uri,
    cardRepository: de.pawcode.cardstore.data.database.repositories.CardRepository,
  ): Result<Unit> = runCatching {
    val data = withContext(Dispatchers.IO) { readBackup(context, uri) }
    if (!validateBackup(data)) error("Invalid backup")
    val (cards, labels, crossRefs) = toEntities(data)
    cardRepository.restoreBackup(cards, labels, crossRefs)
  }

  fun toEntities(
    data: BackupData
  ): Triple<List<CardEntity>, List<LabelEntity>, List<CardLabelCrossRef>> {
    val labelEntities = data.labels.map { LabelEntity(labelId = it.id, name = it.name) }
    val cardEntities =
      data.cards.map { c ->
        CardEntity(
          cardId = c.id,
          storeName = c.storeName,
          cardNumber = c.cardNumber,
          barcodeFormat = BarcodeType.valueOf(c.barcodeFormat),
          color = c.color,
          logo = c.logo,
          lastUsed = c.lastUsed,
          useCount = c.useCount,
          isFavorite = c.isFavorite,
        )
      }
    val crossRefs =
      data.cards.flatMap { c -> c.labelIds.map { labelId -> CardLabelCrossRef(c.id, labelId) } }
    return Triple(cardEntities, labelEntities, crossRefs)
  }
}
