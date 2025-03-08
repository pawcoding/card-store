package de.pawcode.cardstore.data.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import de.pawcode.cardstore.data.daos.CardDao
import de.pawcode.cardstore.data.entities.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CardViewModel(private val cardDao: CardDao) : ViewModel() {
    val allCards: Flow<List<Card>> = cardDao.getAll()

    fun insertCard(card: Card) {
        viewModelScope.launch {
            cardDao.insert(card)
        }
    }

    fun updateCard(card: Card) {
        viewModelScope.launch {
            cardDao.update(card)
        }
    }

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            cardDao.delete(card)
        }
    }
}

class CardViewModelFactory(private val cardDao: CardDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardViewModel(cardDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}