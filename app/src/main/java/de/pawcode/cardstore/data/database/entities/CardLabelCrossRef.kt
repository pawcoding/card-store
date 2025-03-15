package de.pawcode.cardstore.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "card_labels",
    primaryKeys = ["card_id", "label_id"],
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["card_id"],
            childColumns = ["card_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LabelEntity::class,
            parentColumns = ["label_id"],
            childColumns = ["label_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("label_id")
    ]
)
data class CardLabelCrossRef(
    @ColumnInfo(name = "card_id")
    val cardId: String,

    @ColumnInfo(name = "label_id")
    val labelId: String
)
