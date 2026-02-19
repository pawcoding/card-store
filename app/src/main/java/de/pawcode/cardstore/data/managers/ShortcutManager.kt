package de.pawcode.cardstore.data.managers

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import de.pawcode.cardstore.CardShortcutActivity
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.enums.SortAttribute
import de.pawcode.cardstore.utils.calculateCardScore

object ShortcutManager {
  private const val MAX_SHORTCUTS = 4
  const val EXTRA_CARD_ID = "card_id"

  fun updateShortcuts(context: Context, cards: List<CardEntity>, sortAttribute: SortAttribute) {
    val topCards = getTopCards(cards, sortAttribute).take(MAX_SHORTCUTS)

    val shortcuts =
      topCards.mapIndexed { index, card ->
        val intent =
          Intent(context, CardShortcutActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra(EXTRA_CARD_ID, card.cardId)
          }

        ShortcutInfoCompat.Builder(context, "card_${card.cardId}")
          .setShortLabel(card.storeName)
          .setLongLabel(card.storeName)
          .setIcon(IconCompat.createWithResource(context, R.mipmap.ic_launcher))
          .setIntent(intent)
          .setRank(index)
          .build()
      }

    ShortcutManagerCompat.removeAllDynamicShortcuts(context)
    ShortcutManagerCompat.addDynamicShortcuts(context, shortcuts)
  }

  fun clearShortcuts(context: Context) {
    ShortcutManagerCompat.removeAllDynamicShortcuts(context)
  }

  private fun getTopCards(cards: List<CardEntity>, sortAttribute: SortAttribute): List<CardEntity> {
    return when (sortAttribute) {
      SortAttribute.INTELLIGENT -> cards.sortedByDescending { calculateCardScore(it) }
      SortAttribute.ALPHABETICALLY -> cards.sortedBy { it.storeName }
      SortAttribute.RECENTLY_USED -> cards.sortedByDescending { it.lastUsed }
      SortAttribute.MOST_USED -> cards.sortedByDescending { it.useCount }
    }
  }
}
