package de.pawcode.cardstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import de.pawcode.cardstore.navigation.Navigation
import de.pawcode.cardstore.ui.theme.CardStoreTheme

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

    setContent {
      CardStoreTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Navigation()
        }
      }
    }
  }
}
