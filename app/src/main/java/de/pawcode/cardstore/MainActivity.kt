package de.pawcode.cardstore

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import de.pawcode.cardstore.data.services.DeeplinkService
import de.pawcode.cardstore.navigation.Navigation
import de.pawcode.cardstore.ui.theme.CardStoreTheme
import de.pawcode.cardstore.utils.parseDeeplink
import de.pawcode.cardstore.utils.parsePkpass
import de.pawcode.cardstore.utils.readPkpassContentFromUri

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
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

    if (action == Intent.ACTION_VIEW && data != null && scheme != null) {
      if (scheme.startsWith("http")) {
        // Deeplink shared via internal share card feature
        val deeplink = parseDeeplink(data)
        if (deeplink != null) {
          DeeplinkService.deeplinkReceived(deeplink)
        }
      } else if (scheme.startsWith("content") && data.toString().endsWith(".pkpass")) {
        // PKPASS file
        val content = readPkpassContentFromUri(data, contentResolver)

        val deeplink = content?.let { parsePkpass(it) }
        if (deeplink != null) {
          DeeplinkService.deeplinkReceived(deeplink)
        }
      }
    }
  }
}
