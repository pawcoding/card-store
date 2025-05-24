package de.pawcode.cardstore

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.data.services.DeeplinkService
import de.pawcode.cardstore.data.services.ReviewService
import de.pawcode.cardstore.data.services.ReviewStatus
import de.pawcode.cardstore.navigation.Navigation
import de.pawcode.cardstore.ui.theme.CardStoreTheme
import de.pawcode.cardstore.utils.parseDeeplink
import de.pawcode.cardstore.utils.parsePkpass
import de.pawcode.cardstore.utils.readPkpassContentFromUri
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  private var preferencesManager: PreferencesManager? = null
  private var reviewManager: ReviewManager? = null
  private var reviewInfo: ReviewInfo? = null

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

    setContent { CardStoreTheme { Navigation() } }
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
