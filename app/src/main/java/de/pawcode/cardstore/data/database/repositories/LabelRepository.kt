package de.pawcode.cardstore.data.database.repositories

import android.content.Context
import de.pawcode.cardstore.data.database.CardDatabase
import de.pawcode.cardstore.data.database.daos.LabelDao
import de.pawcode.cardstore.data.database.entities.LabelEntity
import kotlinx.coroutines.flow.Flow

class LabelRepository(context: Context) {
  private val labelDao: LabelDao = CardDatabase.getDatabase(context).labelDao()

  val allLabels: Flow<List<LabelEntity>> = labelDao.getAll()

  fun getLabelById(id: String): Flow<LabelEntity?> {
    return labelDao.getById(id)
  }

  suspend fun insertLabel(label: LabelEntity) {
    labelDao.insert(label)
  }

  suspend fun updateLabel(label: LabelEntity) {
    labelDao.update(label)
  }

  suspend fun deleteLabel(label: LabelEntity) {
    labelDao.delete(label)
  }
}
