package de.pawcode.cardstore.data.database.classes

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.CardLabelCrossRef
import de.pawcode.cardstore.data.database.entities.LabelEntity

data class CardWithLabels(
    @Embedded
    val card: CardEntity,

    @Relation(
        parentColumn = "card_id",
        entityColumn = "label_id",
        associateBy = Junction(CardLabelCrossRef::class)
    )
    val labels: List<LabelEntity>
)
