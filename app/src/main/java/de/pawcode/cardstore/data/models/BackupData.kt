package de.pawcode.cardstore.data.models

import kotlinx.serialization.Serializable

@Serializable
data class BackupData(
  val version: Int,
  val createdAt: String,
  val labels: List<BackupLabelData>,
  val cards: List<BackupCardData>,
)

@Serializable
data class BackupLabelData(
  val id: String,
  val name: String,
)

@Serializable
data class BackupCardData(
  val id: String,
  val storeName: String,
  val cardNumber: String,
  val barcodeFormat: String,
  val color: Int,
  val logo: String? = null,
  val lastUsed: Long? = null,
  val useCount: Int = 0,
  val isFavorite: Boolean = false,
  val labelIds: List<String> = emptyList(),
)
