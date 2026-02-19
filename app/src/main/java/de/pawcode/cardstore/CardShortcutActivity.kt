package de.pawcode.cardstore

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.repositories.CardRepository
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.data.managers.ShortcutManager
import de.pawcode.cardstore.data.services.BiometricAuthService
import de.pawcode.cardstore.ui.components.BiometricPlaceholder
import de.pawcode.cardstore.ui.sheets.ViewCardSheet
import de.pawcode.cardstore.ui.theme.CardStoreTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CardShortcutActivity : FragmentActivity() {
  private var cardRepository: CardRepository? = null
  private var preferencesManager: PreferencesManager? = null
  private var card by mutableStateOf<CardEntity?>(null)
  private var isAuthenticated by mutableStateOf(false)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    preferencesManager = PreferencesManager(applicationContext)
    cardRepository = CardRepository(applicationContext)

    val cardId = intent.getStringExtra(ShortcutManager.EXTRA_CARD_ID)

    if (cardId == null) {
      finish()
      return
    }

    lifecycleScope.launch {
      val cardWithLabels = cardRepository?.getCardById(cardId)?.first()
      card = cardWithLabels?.card
      if (card == null) {
        finish()
        return@launch
      }

      // Increment usage count
      card?.let { currentCard ->
        val updatedCard =
          currentCard.copy(useCount = currentCard.useCount + 1, lastUsed = System.currentTimeMillis())
        cardRepository?.updateCard(updatedCard)
      }
    }

    checkAuthentication()

    setContent {
      CardStoreTheme {
        Box(
          modifier =
            Modifier.fillMaxSize()
              .background(Color.Black.copy(alpha = 0.5f))
              .pointerInput(Unit) { detectTapGestures(onTap = { finish() }) },
          contentAlignment = Alignment.Center,
        ) {
          Box(
            modifier =
              Modifier.padding(16.dp).pointerInput(Unit) {
                detectTapGestures(onTap = {
                  // Prevent tap from propagating to parent
                })
              }
          ) {
            if (isAuthenticated) {
              card?.let { ViewCardSheet(it) }
            } else {
              BiometricPlaceholder(onRetry = { checkAuthentication() })
            }
          }
        }
      }
    }
  }

  private fun checkAuthentication() {
    lifecycleScope.launch {
      val biometricEnabled = preferencesManager?.biometricEnabled?.first() ?: false
      if (biometricEnabled && BiometricAuthService.isBiometricAvailable(this@CardShortcutActivity)) {
        BiometricAuthService.authenticate(
          activity = this@CardShortcutActivity,
          title = getString(R.string.biometric_auth_title),
          subtitle = getString(R.string.biometric_auth_subtitle),
          onSuccess = { isAuthenticated = true },
          onError = { finish() },
        )
      } else {
        isAuthenticated = true
      }
    }
  }
}
