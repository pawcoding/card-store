package de.pawcode.cardstore

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.repositories.CardRepository
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.data.services.BiometricAuthService
import de.pawcode.cardstore.ui.components.BiometricPlaceholder
import de.pawcode.cardstore.ui.sheets.ViewCardSheet
import de.pawcode.cardstore.ui.theme.CardStoreTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CardOverlayActivity : FragmentActivity() {
    companion object {
        const val EXTRA_CARD_ID = "card_id"
    }

    private var card by mutableStateOf<CardEntity?>(null)
    private var isAuthenticated by mutableStateOf(false)
    private var isLoading by mutableStateOf(true)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cardId = intent.getStringExtra(EXTRA_CARD_ID)
        if (cardId == null) {
            finish()
            return
        }

        lifecycleScope.launch {
            val cardRepository = CardRepository(applicationContext)
            val cardWithLabels = cardRepository.getCardById(cardId).first()
            if (cardWithLabels == null) {
                finish()
                return@launch
            }
            card = cardWithLabels.card
            isLoading = false
            checkAuthentication()
        }

        setContent {
            CardStoreTheme {
                if (!isLoading) {
                    val currentCard = card
                    if (currentCard != null) {
                        if (isAuthenticated) {
                            CardOverlayContent(
                                card = currentCard,
                                onDismiss = { finish() },
                            )
                        } else {
                            BiometricPlaceholder(onRetry = { checkAuthentication() })
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val newCardId = intent.getStringExtra(EXTRA_CARD_ID)
        if (newCardId == null) {
            finish()
            return
        }
        isLoading = true
        isAuthenticated = false
        card = null
        lifecycleScope.launch {
            val cardRepository = CardRepository(applicationContext)
            val cardWithLabels = cardRepository.getCardById(newCardId).first()
            if (cardWithLabels == null) {
                finish()
                return@launch
            }
            card = cardWithLabels.card
            isLoading = false
            checkAuthentication()
        }
    }

    private fun checkAuthentication() {
        lifecycleScope.launch {
            val preferencesManager = PreferencesManager(applicationContext)
            val biometricEnabled = preferencesManager.biometricEnabled.first()
            if (biometricEnabled && BiometricAuthService.isBiometricAvailable(this@CardOverlayActivity)) {
                BiometricAuthService.authenticate(
                    activity = this@CardOverlayActivity,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardOverlayContent(card: CardEntity, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier =
            Modifier.fillMaxSize()
                .background(Color.Transparent)
                .clickable(interactionSource = interactionSource, indication = null) { onDismiss() },
    ) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight().windowInsetsPadding(WindowInsets.statusBars),
            sheetState = sheetState,
            onDismissRequest = onDismiss,
        ) {
            ViewCardSheet(card)
        }
    }
}
