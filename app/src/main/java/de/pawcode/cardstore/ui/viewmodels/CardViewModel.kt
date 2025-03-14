package de.pawcode.cardstore.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.repositories.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CardRepository(application)
    val allCards = repository.allCards

    fun getCardById(id: String?): Flow<CardEntity?> = flow {
        if (id == null) {
            emit(null)
        } else {
            repository.getCardById(id).collect { emit(it) }
        }
    }

    fun insertCard(card: CardEntity) = viewModelScope.launch {
        repository.insertCard(card)
    }

    fun updateCard(card: CardEntity) = viewModelScope.launch {
        repository.updateCard(card)
    }

    fun addUsage(card: CardEntity) = viewModelScope.launch {
        val updatedCard = card.copy(
            useCount = card.useCount + 1,
            lastUsed = System.currentTimeMillis()
        )

        updateCard(updatedCard)
    }

    fun deleteCard(card: CardEntity) = viewModelScope.launch {
        repository.deleteCard(card)
    }
}