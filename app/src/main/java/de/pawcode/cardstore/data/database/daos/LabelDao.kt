package de.pawcode.cardstore.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.pawcode.cardstore.data.database.entities.LabelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelDao {
  @Insert(onConflict = OnConflictStrategy.Companion.ABORT) suspend fun insert(label: LabelEntity)

  @Update suspend fun update(label: LabelEntity)

  @Delete suspend fun delete(label: LabelEntity)

  @Query("SELECT * FROM labels ORDER BY name ASC") fun getAll(): Flow<List<LabelEntity>>

  @Query("SELECT * FROM labels WHERE label_id = :id") fun getById(id: String): Flow<LabelEntity?>
}
