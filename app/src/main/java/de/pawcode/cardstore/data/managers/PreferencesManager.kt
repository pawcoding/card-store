package de.pawcode.cardstore.data.managers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.pawcode.cardstore.data.enums.SortAttribute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
  companion object {
    private val SORT_ATTRIBUTE = stringPreferencesKey("sort_attribute")
  }

  val sortAttribute: Flow<SortAttribute> =
    context.dataStore.data.map { preferences ->
      preferences[SORT_ATTRIBUTE]?.let { SortAttribute.valueOf(it) } ?: SortAttribute.ALPHABETICALLY
    }

  suspend fun saveSortAttribute(sortAttribute: SortAttribute) {
    context.dataStore.edit { preferences -> preferences[SORT_ATTRIBUTE] = sortAttribute.name }
  }
}
