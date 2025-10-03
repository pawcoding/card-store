package de.pawcode.cardstore.data.managers

import android.content.Context
import android.util.Log
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.pawcode.cardstore.data.enums.SortAttribute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val TAG = "PreferencesManager"
private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
  companion object {
    private val SORT_ATTRIBUTE = stringPreferencesKey("sort_attribute")
    private val REVIEW_PROMPT_TIME = longPreferencesKey("review_prompt_time")
    private val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    private val LAST_AUTH_TIME = longPreferencesKey("last_auth_time")
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

  suspend fun reviewPromptTime(): Long {
    try {
      return context.dataStore.data.first()[REVIEW_PROMPT_TIME] ?: 0L
    } catch (exception: IOException) {
      Log.e(TAG, "Failed to load review prompt time: ${exception.message}", exception)
      return 0L
    }
  }

  suspend fun saveReviewPromptTime() {
    try {
      context.dataStore.edit { preferences ->
        preferences[REVIEW_PROMPT_TIME] = System.currentTimeMillis()
      }
    } catch (exception: IOException) {
      Log.e(TAG, "Failed to save review prompt time: ${exception.message}", exception)
    }
  }

  val biometricEnabled: Flow<Boolean> =
    context.dataStore.data
      .catch { exception ->
        Log.e(TAG, "Error reading biometric preference: ${exception.message}", exception)
        emit(emptyPreferences())
      }
      .map { preferences -> preferences[BIOMETRIC_ENABLED] ?: false }

  suspend fun saveBiometricEnabled(enabled: Boolean) {
    try {
      context.dataStore.edit { preferences -> preferences[BIOMETRIC_ENABLED] = enabled }
    } catch (exception: IOException) {
      Log.e(TAG, "Failed to save biometric preference: ${exception.message}", exception)
    }
  }

  suspend fun lastAuthTime(): Long {
    try {
      return context.dataStore.data.first()[LAST_AUTH_TIME] ?: 0L
    } catch (exception: IOException) {
      Log.e(TAG, "Failed to load last auth time: ${exception.message}", exception)
      return 0L
    }
  }

  suspend fun saveLastAuthTime() {
    try {
      context.dataStore.edit { preferences ->
        preferences[LAST_AUTH_TIME] = System.currentTimeMillis()
      }
    } catch (exception: IOException) {
      Log.e(TAG, "Failed to save last auth time: ${exception.message}", exception)
    }
  }
}
