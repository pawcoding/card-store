package de.pawcode.cardstore.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a card.
 */
@Entity(tableName = "cards")
data class Card(
    /**
     * Unique identifier of the card.
     */
    @PrimaryKey
    val id: String,

    /**
     * Store where the card is used.
     */
    val store: String,

    /**
     * Number of the card.
     */
    @ColumnInfo(name = "card_number")
    val cardNumber: String,

    /**
     * Color of the card (hex code).
     */
    val color: String
)
