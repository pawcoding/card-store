package de.pawcode.cardstore.data.database.repositories

import android.content.Context
import de.pawcode.cardstore.data.database.CardDatabase
import de.pawcode.cardstore.data.database.daos.CardDao
import de.pawcode.cardstore.data.database.entities.CardEntity
import kotlinx.coroutines.flow.Flow

class CardRepository(context: Context) {
    private val cardDao: CardDao = CardDatabase.getDatabase(context).cardDao()

    val allCards: Flow<List<CardEntity>> = cardDao.getAll()

    fun getCardById(id: String): Flow<CardEntity?> {
        return cardDao.getById(id)
    }

    suspend fun insertCard(card: CardEntity) {
        cardDao.insert(card)
    }

    suspend fun updateCard(card: CardEntity) {
        cardDao.update(card)
    }

    suspend fun deleteCard(card: CardEntity) {
        cardDao.delete(card)
    }
}