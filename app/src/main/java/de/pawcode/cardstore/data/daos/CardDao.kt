package de.pawcode.cardstore.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.pawcode.cardstore.data.entities.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(card: Card)

    @Query("SELECT * FROM cards WHERE id = :id")
    fun getById(id: String): Flow<Card?>

    @Query("SELECT * FROM cards")
    fun getAll(): Flow<List<Card>>

    @Update
    suspend fun update(card: Card)

    @Delete
    suspend fun delete(card: Card)
}