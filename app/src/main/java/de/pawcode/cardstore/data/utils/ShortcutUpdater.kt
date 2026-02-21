package de.pawcode.cardstore.data.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import de.pawcode.cardstore.CardOverlayActivity
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.repositories.CardRepository
import de.pawcode.cardstore.data.enums.SortAttribute
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.utils.calculateCardScore
import kotlinx.coroutines.flow.first

private const val MAX_SHORTCUTS = 3
internal const val SHORTCUT_ACTION = "de.pawcode.cardstore.ACTION_VIEW_CARD"

internal fun cardShortcutId(cardId: String) = "card_shortcut_$cardId"

suspend fun updateShortcuts(context: Context) {
    val cardRepository = CardRepository(context)
    val preferencesManager = PreferencesManager(context)

    val allCards = cardRepository.allCards.first().map { it.card }
    val sortAttribute = preferencesManager.sortAttribute.first()

    val sortedCards: List<CardEntity> =
        when (sortAttribute) {
            SortAttribute.INTELLIGENT -> allCards.sortedByDescending { calculateCardScore(it) }
            SortAttribute.ALPHABETICALLY -> allCards.sortedBy { it.storeName }
            SortAttribute.RECENTLY_USED -> allCards.sortedByDescending { it.lastUsed }
            SortAttribute.MOST_USED -> allCards.sortedByDescending { it.useCount }
        }

    val topCards = sortedCards.take(MAX_SHORTCUTS)

    val newShortcutIds = topCards.map { cardShortcutId(it.cardId) }.toSet()
    val existingShortcuts = ShortcutManagerCompat.getDynamicShortcuts(context)
    val staleIds = existingShortcuts.filter { it.id !in newShortcutIds }.map { it.id }
    if (staleIds.isNotEmpty()) ShortcutManagerCompat.removeDynamicShortcuts(context, staleIds)

    topCards.forEach { card ->
        val shortcutId = cardShortcutId(card.cardId)
        val icon = createShortcutIcon(card)

        val intent =
            Intent(context, CardOverlayActivity::class.java).apply {
                action = SHORTCUT_ACTION
                putExtra(CardOverlayActivity.EXTRA_CARD_ID, card.cardId)
            }

        val shortcut =
            ShortcutInfoCompat.Builder(context, shortcutId)
                .setShortLabel(card.storeName.take(25))
                .setLongLabel(card.storeName)
                .setIcon(icon)
                .setIntent(intent)
                .build()

        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }
}

internal fun createShortcutIcon(card: CardEntity): IconCompat {
    val size = 108
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val circlePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = card.color
            style = Paint.Style.FILL
        }
    canvas.drawCircle(54f, 54f, 36f, circlePaint)

    val r = android.graphics.Color.red(card.color) / 255f
    val g = android.graphics.Color.green(card.color) / 255f
    val b = android.graphics.Color.blue(card.color) / 255f
    val isLight = (0.299 * r + 0.587 * g + 0.114 * b) > 0.5
    val textPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (isLight) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            textSize = size * 0.45f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }

    val firstLetter = card.storeName.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    val textY = 54f - (textPaint.descent() + textPaint.ascent()) / 2f
    canvas.drawText(firstLetter, 54f, textY, textPaint)

    return IconCompat.createWithAdaptiveBitmap(bitmap).also { bitmap.recycle() }
}
