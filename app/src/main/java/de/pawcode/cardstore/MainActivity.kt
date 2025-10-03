package de.pawcode.cardstore

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.data.services.BiometricAuthService
import de.pawcode.cardstore.data.services.DeeplinkService
import de.pawcode.cardstore.data.services.ReviewService
import de.pawcode.cardstore.data.services.ReviewStatus
import de.pawcode.cardstore.navigation.Navigation
import de.pawcode.cardstore.ui.theme.CardStoreTheme
import de.pawcode.cardstore.utils.parseDeeplink
import de.pawcode.cardstore.utils.parsePkpass
import de.pawcode.cardstore.utils.readPkpassContentFromUri
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
  private var preferencesManager: PreferencesManager? = null
  private var reviewManager: ReviewManager? = null
  private var reviewInfo: ReviewInfo? = null
  private var isAuthenticated by mutableStateOf(false)
  private var lastPauseTime: Long = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    preferencesManager = PreferencesManager(applicationContext)

    installSplashScreen()

    super.onCreate(savedInstanceState)

    enableEdgeToEdge()

    // Check if barcode scanner is installed
    val moduleInstallClient = ModuleInstall.getClient(this)
    val barcodeScanner = GmsBarcodeScanning.getClient(this)
    moduleInstallClient.areModulesAvailable(barcodeScanner).addOnSuccessListener {
      // If not installed, request a background install
      if (!it.areModulesAvailable()) {
        moduleInstallClient.deferredInstall(barcodeScanner)
      }
    }

    handleIncomingIntent(intent)

    lifecycleScope.launch { ReviewService.reviewStatus.collect { handleReviewStatus(it) } }

    checkAuthentication()

    setContent {
      CardStoreTheme {
        if (isAuthenticated) {
          Navigation()
        } else {
          BiometricPlaceholder(onRetry = { checkAuthentication() })
        }
      }
    }

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        // Re-check authentication when app comes to foreground
        if (lastPauseTime > 0) {
          val timeSincePause = System.currentTimeMillis() - lastPauseTime
          val fiveMinutesInMillis = 5 * 60 * 1000
          if (timeSincePause > fiveMinutesInMillis) {
            isAuthenticated = false
            checkAuthentication()
          }
        }
      }
    }
  }

  override fun onPause() {
    super.onPause()
    lastPauseTime = System.currentTimeMillis()
  }

  override fun onResume() {
    super.onResume()
    lifecycleScope.launch {
      val biometricEnabled = preferencesManager?.biometricEnabled?.first() ?: false
      if (biometricEnabled && lastPauseTime > 0) {
        val timeSincePause = System.currentTimeMillis() - lastPauseTime
        val fiveMinutesInMillis = 5 * 60 * 1000
        if (timeSincePause > fiveMinutesInMillis) {
          isAuthenticated = false
        }
      }
    }
  }

  private fun checkAuthentication() {
    lifecycleScope.launch {
      val biometricEnabled = preferencesManager?.biometricEnabled?.first() ?: false
      if (biometricEnabled && BiometricAuthService.isBiometricAvailable(this@MainActivity)) {
        val lastAuthTime = preferencesManager?.lastAuthTime() ?: 0L
        val timeSinceAuth = System.currentTimeMillis() - lastAuthTime
        val fiveMinutesInMillis = 5 * 60 * 1000

        if (timeSinceAuth > fiveMinutesInMillis) {
          BiometricAuthService.authenticate(
            activity = this@MainActivity,
            title = getString(R.string.biometric_auth_title),
            subtitle = getString(R.string.biometric_auth_subtitle),
            onSuccess = {
              isAuthenticated = true
              lifecycleScope.launch { preferencesManager?.saveLastAuthTime() }
            },
            onError = { isAuthenticated = false },
          )
        } else {
          isAuthenticated = true
        }
      } else {
        isAuthenticated = true
      }
    }
  }

  @Composable
  private fun BiometricPlaceholder(onRetry: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(24.dp),
          modifier = Modifier.padding(32.dp),
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier.size(120.dp),
          )
          Text(
            text = stringResource(R.string.biometric_required),
            style = MaterialTheme.typography.headlineSmall,
          )
          Button(onClick = onRetry) { Text(text = stringResource(R.string.biometric_auth_retry)) }
        }
      }
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    handleIncomingIntent(intent)
  }

  private fun handleIncomingIntent(intent: Intent) {
    val action = intent.action
    val data = intent.data
    val scheme = data?.scheme
    val mimeType = intent.type

    if (action == Intent.ACTION_VIEW && data != null && scheme != null) {
      if (scheme.startsWith("http")) {
        // Deeplink shared via internal share card feature
        val deeplink = parseDeeplink(data)
        if (deeplink != null) {
          DeeplinkService.deeplinkReceived(deeplink)
        }
      } else if (scheme.startsWith("content") && mimeType == "application/vnd.apple.pkpass") {
        // PKPASS file
        val content = readPkpassContentFromUri(data, contentResolver)

        val deeplink = content?.let { parsePkpass(it) }
        if (deeplink != null) {
          DeeplinkService.deeplinkReceived(deeplink)
        }
      }
    }
  }

  private suspend fun handleReviewStatus(reviewStatus: ReviewStatus) {
    val lastPromptTime = preferencesManager?.reviewPromptTime() ?: System.currentTimeMillis()
    if (!ReviewService.canRequestReview(lastPromptTime)) {
      return
    }

    when (reviewStatus) {
      ReviewStatus.PREPARE -> {
        if (reviewManager == null) {
          reviewManager = ReviewManagerFactory.create(applicationContext)
        }

        if (reviewInfo == null) {
          val reviewRequest = reviewManager?.requestReviewFlow()
          reviewRequest?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
              reviewInfo = task.result
            }
          }
        }
      }
      ReviewStatus.REQUEST_REVIEW -> {
        if (reviewManager != null && reviewInfo != null) {
          val reviewFlow = reviewManager!!.launchReviewFlow(this, reviewInfo!!)
          reviewFlow.addOnCompleteListener { _ ->
            lifecycleScope.launch { preferencesManager?.saveReviewPromptTime() }
          }
        }
      }
    }
  }
}
