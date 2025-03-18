package de.pawcode.cardstore.data.database.classes

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.CardLabelCrossRef
import de.pawcode.cardstore.data.database.entities.EXAMPLE_CARD
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.data.database.entities.emptyCard

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

val EXAMPLE_CARD_WITH_LABELS = CardWithLabels(
    EXAMPLE_CARD,
    listOf(
        EXAMPLE_LABEL
    )
)

fun emptyCardWithLabels(): CardWithLabels {
    return CardWithLabels(
        emptyCard(),
        listOf()
    )
}
