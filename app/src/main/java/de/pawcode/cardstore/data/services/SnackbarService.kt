package de.pawcode.cardstore.data.services

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SnackbarService {
    val snackbarHostState = SnackbarHostState()

    fun showSnackbar(
        message: String,
        scope: CoroutineScope,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null
    ) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                withDismissAction = onAction != null,
            ).let { result ->
                if (result == SnackbarResult.ActionPerformed && onAction != null) {
                    onAction()
                }
            }
        }
    }
}