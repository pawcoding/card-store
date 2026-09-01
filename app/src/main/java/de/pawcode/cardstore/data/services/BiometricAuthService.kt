package de.pawcode.cardstore.data.services

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricAuthService {
  fun isBiometricAvailable(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
      BiometricManager.BIOMETRIC_SUCCESS -> true
      else -> false
    }
  }

  fun authenticate(
    activity: FragmentActivity,
    title: String,
    subtitle: String,
    onSuccess: () -> Unit,
    onError: () -> Unit,
  ) {
    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt =
      BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
          override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            onError()
          }

          override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onSuccess()
          }

          override fun onAuthenticationFailed() {
            // A single failed scan (e.g. unrecognized finger) — the BiometricPrompt UI
            // already shows a retry option, so no action is needed here.
            super.onAuthenticationFailed()
          }
        },
      )

    val promptInfo =
      BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .setSubtitle(subtitle)
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    biometricPrompt.authenticate(promptInfo)
  }
}
