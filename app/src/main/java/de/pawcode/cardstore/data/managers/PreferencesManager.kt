package de.pawcode.cardstore.data.managers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.pawcode.cardstore.data.enums.SortAttribute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
  companion object {
    private val SORT_ATTRIBUTE = stringPreferencesKey("sort_attribute")
  }

  val sortAttribute: Flow<SortAttribute> =
    context.dataStore.data.map { preferences -> SortAttribute.fromKey(preferences[SORT_ATTRIBUTE]) }

  suspend fun saveSortAttribute(sortAttribute: SortAttribute) {
    context.dataStore.edit { preferences -> preferences[SORT_ATTRIBUTE] = sortAttribute.key }
  }
}
