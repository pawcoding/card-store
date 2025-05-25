package de.pawcode.cardstore.data.managers

import android.content.Context
import android.util.Log
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.pawcode.cardstore.data.enums.SortAttribute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val TAG = "PreferencesManager"
private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
  companion object {
    private val SORT_ATTRIBUTE = stringPreferencesKey("sort_attribute")
  }

  val sortAttribute: Flow<SortAttribute> =
    context.dataStore.data
      .catch { exception ->
        Log.e(TAG, "Error reading preferences: ${exception.message}", exception)
        emit(emptyPreferences())
      }
      .map { preferences ->
        try {
          SortAttribute.fromKey(preferences[SORT_ATTRIBUTE])
        } catch (exception: Exception) {
          Log.e(TAG, "Failed to parse sort attribute: ${exception.message}", exception)
          SortAttribute.INTELLIGENT
        }
      }

  suspend fun saveSortAttribute(sortAttribute: SortAttribute) {
    try {
      context.dataStore.edit { preferences -> preferences[SORT_ATTRIBUTE] = sortAttribute.key }
    } catch (exception: IOException) {
      Log.e(TAG, "Failed to save sort attribute: ${exception.message}", exception)
    }
  }
}
