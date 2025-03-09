package de.pawcode.cardstore.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.pawcode.cardstore.data.database.CardEntity
import de.pawcode.cardstore.data.repository.CardRepository
import kotlinx.coroutines.launch

class CardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CardRepository(application)
    val allCards = repository.allCards

    fun getCardById(id: String) = repository.getCardById(id)

    fun insertCard(card: CardEntity) = viewModelScope.launch {
        repository.insertCard(card)
    }

    fun updateCard(card: CardEntity) = viewModelScope.launch {
        repository.updateCard(card)
    }

    fun addUsage(card: CardEntity) = viewModelScope.launch {
        card.useCount++
        card.lastUsed = System.currentTimeMillis()

        updateCard(card)
    }

    fun deleteCard(card: CardEntity) = viewModelScope.launch {
        repository.deleteCard(card)
    }
}