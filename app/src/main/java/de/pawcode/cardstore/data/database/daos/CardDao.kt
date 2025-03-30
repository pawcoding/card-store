package de.pawcode.cardstore.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import de.pawcode.cardstore.data.database.classes.CardWithLabels
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.CardLabelCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
  @Insert(onConflict = OnConflictStrategy.Companion.ABORT) suspend fun insert(card: CardEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addLabelsToCard(cardLabels: List<CardLabelCrossRef>)

  @Update suspend fun update(card: CardEntity)

  @Delete suspend fun delete(card: CardEntity)

  @Delete suspend fun removeLabelsFromCard(cardLabels: List<CardLabelCrossRef>)

  @Transaction @Query("SELECT * FROM cards") fun getAll(): Flow<List<CardWithLabels>>

  @Transaction
  @Query("SELECT * FROM cards WHERE card_id = :id")
  fun getById(id: String): Flow<CardWithLabels?>
}
