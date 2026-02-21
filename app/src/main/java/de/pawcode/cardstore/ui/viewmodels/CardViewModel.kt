package de.pawcode.cardstore.ui.viewmodels

import android.app.Application
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.pawcode.cardstore.CardOverlayActivity
import de.pawcode.cardstore.data.database.classes.CardWithLabels
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.data.database.repositories.CardRepository
import de.pawcode.cardstore.data.database.repositories.LabelRepository
import de.pawcode.cardstore.data.utils.SHORTCUT_ACTION
import de.pawcode.cardstore.data.utils.cardShortcutId
import de.pawcode.cardstore.data.utils.createShortcutIcon
import de.pawcode.cardstore.data.utils.updateShortcuts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CardViewModel(application: Application) : AndroidViewModel(application) {
  private val cardRepository = CardRepository(application)
  private val labelRepository = LabelRepository(application)

  val allCards: Flow<List<CardWithLabels>> = cardRepository.allCards
  val allLabels = labelRepository.allLabels

  fun getCardById(id: String): Flow<CardWithLabels?> = flow {
    cardRepository.getCardById(id).collect { emit(it) }
  }

  fun getLabelById(id: String?): Flow<LabelEntity?> = flow {
    if (id == null) {
      emit(null)
    } else {
      labelRepository.getLabelById(id).collect { emit(it) }
    }
  }

  fun insertCard(card: CardEntity) = viewModelScope.launch { cardRepository.insertCard(card) }

  suspend fun cardExists(id: String): Boolean = cardRepository.cardExistsById(id)

  fun insertLabel(label: LabelEntity) = viewModelScope.launch { labelRepository.insertLabel(label) }

  fun addLabelsToCard(cardId: String, labelIds: List<String>) =
    viewModelScope.launch { cardRepository.addLabelsToCard(cardId, labelIds) }

  fun updateCard(card: CardEntity) = viewModelScope.launch { cardRepository.updateCard(card) }

  fun updateLabel(label: LabelEntity) = viewModelScope.launch { labelRepository.updateLabel(label) }

  fun addUsage(card: CardEntity) =
    viewModelScope.launch {
      val updatedCard =
        card.copy(useCount = card.useCount + 1, lastUsed = System.currentTimeMillis())

      cardRepository.updateCard(updatedCard)
      updateShortcuts(getApplication())
    }

  fun pinShortcut(card: CardEntity) {
    val context = getApplication<Application>()
    if (!ShortcutManagerCompat.isRequestPinShortcutSupported(context)) return

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

    ShortcutManagerCompat.requestPinShortcut(context, shortcut, null)
  }

  fun deleteCard(card: CardEntity) = viewModelScope.launch { cardRepository.deleteCard(card) }

  fun deleteLabel(label: LabelEntity) = viewModelScope.launch { labelRepository.deleteLabel(label) }

  fun removeLabelsFromCard(cardId: String, labelIds: List<String>) =
    viewModelScope.launch { cardRepository.removeLabelsFromCard(cardId, labelIds) }
}
