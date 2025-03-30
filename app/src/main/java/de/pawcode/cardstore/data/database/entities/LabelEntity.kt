package de.pawcode.cardstore.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/** Entity representing a label. */
@Entity(tableName = "labels")
data class LabelEntity(
  /** Unique identifier of the label. */
  @PrimaryKey @ColumnInfo(name = "label_id") val labelId: String,

  /** Name of the label. */
  val name: String,
)

/** Example label used to preview components. */
val EXAMPLE_LABEL: LabelEntity =
  LabelEntity(labelId = "06c96a85-7dcd-4cfc-b886-2c95e8ea7c62", name = "Groceries")

/** List of example labels used to preview components. */
val EXAMPLE_LABEL_LIST: List<LabelEntity> =
  listOf(
    LabelEntity(labelId = "f3b3b3b3-3b3b-4b3b-b3b3-3b3b3b3b3b3b", name = "Work"),
    LabelEntity(labelId = "f3b3b3b3-3b3b-4b3b-b3b3-3b3b3b3b3b3b", name = "Personal"),
    EXAMPLE_LABEL,
    LabelEntity(labelId = "f3b3b3b3-3b3b-4b3b-b3b3-3b3b3b3b3b3b", name = "Shopping"),
  )

@OptIn(ExperimentalUuidApi::class)
fun emptyLabel(): LabelEntity {
  return LabelEntity(labelId = Uuid.random().toString(), name = "")
}
