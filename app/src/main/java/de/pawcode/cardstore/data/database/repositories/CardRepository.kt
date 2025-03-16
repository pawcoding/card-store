package de.pawcode.cardstore.data.database.repositories

import android.content.Context
import de.pawcode.cardstore.data.database.CardDatabase
import de.pawcode.cardstore.data.database.classes.CardWithLabels
import de.pawcode.cardstore.data.database.daos.CardDao
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.CardLabelCrossRef
import kotlinx.coroutines.flow.Flow

class CardRepository(context: Context) {
    private val cardDao: CardDao = CardDatabase.getDatabase(context).cardDao()

    val allCards: Flow<List<CardWithLabels>> = cardDao.getAll()

    fun getCardById(id: String): Flow<CardWithLabels?> {
        return cardDao.getById(id)
    }

    suspend fun insertCard(card: CardEntity) {
        cardDao.insert(card)
    }

    suspend fun addLabelsToCard(cardId: String, labelIds: List<String>) {
        val cardLabels = labelIds.map { CardLabelCrossRef(cardId, it) }
        cardDao.addLabelsToCard(cardLabels)
    }

    suspend fun updateCard(card: CardEntity) {
        cardDao.update(card)
    }

    suspend fun deleteCard(card: CardEntity) {
        cardDao.delete(card)
    }

    suspend fun removeLabelsFromCard(cardId: String, labelIds: List<String>) {
        val cardLabels = labelIds.map { CardLabelCrossRef(cardId, it) }
        cardDao.removeLabelsFromCard(cardLabels)
    }
}