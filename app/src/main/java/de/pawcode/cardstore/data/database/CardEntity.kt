package de.pawcode.cardstore.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.simonsickle.compose.barcodes.BarcodeType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Entity representing a card.
 */
@Entity(tableName = "cards")
data class CardEntity(
    /**
     * Unique identifier of the card.
     */
    @PrimaryKey
    val id: String,

    /**
     * Store where the card is used.
     */
    val storeName: String,

    /**
     * Number of the card.
     */
    @ColumnInfo(name = "card_number")
    val cardNumber: String,

    /**
     * Format of the barcode.
     */
    @ColumnInfo(name = "barcode_format")
    val barcodeFormat: BarcodeType,

    /**
     * Color of the card (hex code).
     */
    val color: String,

    /**
     * Logo of the store.
     */
    val logo: String? = null,

    /**
     * Last used date of the card.
     */
    @ColumnInfo(name = "last_used")
    val lastUsed: Long? = null,

    /**
     * Number of times the card was used.
     */
    @ColumnInfo(name = "use_count")
    val useCount: Int = 0
)

/**
 * Example card used to preview components.
 */
val EXAMPLE_CARD: CardEntity = CardEntity(
    id = "06c96a85-7dcd-4cfc-b886-2c95e8ea7c62",
    storeName = "pawcode Development",
    cardNumber = "1234567890",
    barcodeFormat = BarcodeType.QR_CODE,
    color = "#4472c4",
    logo = null,
    lastUsed = null,
    useCount = 0
)

@OptIn(ExperimentalUuidApi::class)
fun emptyCard(): CardEntity {
    return CardEntity(
        id = Uuid.random().toString(),
        storeName = "",
        cardNumber = "",
        barcodeFormat = BarcodeType.QR_CODE,
        color = "#FFFFFF"
    )
}
