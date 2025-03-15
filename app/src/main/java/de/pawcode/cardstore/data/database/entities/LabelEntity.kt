package de.pawcode.cardstore.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Entity representing a label.
 */
@Entity(tableName = "labels")
data class LabelEntity(
    /**
     * Unique identifier of the label.
     */
    @PrimaryKey
    @ColumnInfo(name = "label_id")
    val labelId: String,

    /**
     * Name of the label.
     */
    val name: String
)

/**
 * Example label used to preview components.
 */
val EXAMPLE_LABEL: LabelEntity = LabelEntity(
    labelId = "06c96a85-7dcd-4cfc-b886-2c95e8ea7c62",
    name = "Groceries"
)

@OptIn(ExperimentalUuidApi::class)
fun emptyLabel(): LabelEntity {
    return LabelEntity(
        labelId = Uuid.random().toString(),
        name = ""
    )
}