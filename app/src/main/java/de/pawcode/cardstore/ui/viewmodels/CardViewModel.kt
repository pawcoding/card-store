package de.pawcode.cardstore.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.data.database.repositories.CardRepository
import de.pawcode.cardstore.data.database.repositories.LabelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CardViewModel(application: Application) : AndroidViewModel(application) {
    private val cardRepository = CardRepository(application)
    private val labelRepository = LabelRepository(application)

    val allCards = cardRepository.allCards
    val allLabels = labelRepository.allLabels

    fun getCardById(id: String?): Flow<CardEntity?> = flow {
        if (id == null) {
            emit(null)
        } else {
            cardRepository.getCardById(id).collect { emit(it) }
        }
    }

    fun insertCard(card: CardEntity) = viewModelScope.launch {
        cardRepository.insertCard(card)
    }

    fun insertLabel(label: LabelEntity) = viewModelScope.launch {
        labelRepository.insertLabel(label)
    }

    fun updateCard(card: CardEntity) = viewModelScope.launch {
        cardRepository.updateCard(card)
    }

    fun addUsage(card: CardEntity) = viewModelScope.launch {
        val updatedCard = card.copy(
            useCount = card.useCount + 1,
            lastUsed = System.currentTimeMillis()
        )

        updateCard(updatedCard)
    }

    fun deleteCard(card: CardEntity) = viewModelScope.launch {
        cardRepository.deleteCard(card)
    }
}