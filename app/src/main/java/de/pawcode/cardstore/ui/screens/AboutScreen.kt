package de.pawcode.cardstore.ui.screens

import android.content.Intent
import android.content.pm.PackageInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import de.pawcode.cardstore.R
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.SettingsGroup
import de.pawcode.cardstore.ui.components.SettingsItem

data class Technology(val name: String, val url: String, val icon: ImageVector)

val TECHNOLOGIES =
  listOf<Technology>(
    Technology(name = "Kotlin", url = "https://kotlinlang.org/", icon = Icons.Filled.DataObject),
    Technology(
      name = "Jetpack Compose",
      url = "https://developer.android.com/jetpack/compose",
      icon = Icons.Filled.Android,
    ),
    Technology(
      name = "Room",
      url = "https://developer.android.com/jetpack/androidx/releases/room",
      icon = Icons.Filled.Storage,
    ),
    Technology(
      name = "Material Design 3",
      url = "https://m3.material.io/",
      icon = Icons.Filled.Palette,
    ),
    Technology(
      name = "ML Kit",
      url = "https://developers.google.com/ml-kit",
      icon = Icons.Filled.Scanner,
    ),
    Technology(
      name = "Google Code-Scanner",
      url = "https://developers.google.com/ml-kit/vision/barcode-scanning/code-scanner",
      icon = Icons.Filled.CameraAlt,
    ),
    Technology(
      name = "ColorPickerView",
      url = "https://github.com/skydoves/ColorPickerView",
      icon = Icons.Filled.Colorize,
    ),
    Technology(
      name = "ComposedBarcodes",
      url = "https://github.com/simonsickle/ComposedBarcodes",
      icon = Icons.Filled.QrCode,
    ),
    Technology(
      name = "RevealSwipe",
      url = "https://github.com/ch4rl3x/RevealSwipe",
      icon = Icons.Filled.Gesture,
    ),
  )

@Composable
fun AboutScreen(navController: NavController) {
  val context = LocalContext.current

  var packageInfo = PackageInfo()
  try {
    packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
  } catch (e: Exception) {
    e.printStackTrace()
  }

  AboutScreenComponent(
    packageInfo = packageInfo,
    onBack = { navController.popBackStack() },
    onOpenWebsite = { context.startActivity(Intent(Intent.ACTION_VIEW, it.toUri())) },
  )
}

@Composable
fun AboutScreenComponent(
  packageInfo: PackageInfo,
  onBack: () -> Unit,
  onOpenWebsite: (String) -> Unit,
) {
  val context = LocalContext.current

  val hasVersionName = packageInfo.versionName != null
  val versionName = packageInfo.versionName ?: "Unknown version"
  val isDebug = packageInfo.packageName.endsWith(".debug")

  Scaffold(topBar = { AppBar(title = stringResource(R.string.app_name), onBack = { onBack() }) }) {
    innerPadding ->
    Column(
      modifier = Modifier.padding(innerPadding).fillMaxSize().verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Column(
        modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
      ) {
        // App Information Group
        SettingsGroup(title = stringResource(R.string.about)) {
          SettingsItem(
            icon = Icons.Filled.Info,
            iconColor = MaterialTheme.colorScheme.primaryContainer,
            iconBackground = MaterialTheme.colorScheme.onPrimaryContainer,
            title = stringResource(R.string.version) + if (isDebug) " (debug)" else "",
            subtitle = versionName + " (${packageInfo.longVersionCode})",
            onClick = {
              if (hasVersionName) {
                onOpenWebsite("https://github.com/pawcoding/card-store/releases/tag/v$versionName")
              }
            },
          )
        }

        // Links Group
        SettingsGroup(title = "Links") {
          SettingsItem(
            icon = Icons.Filled.Web,
            iconColor = MaterialTheme.colorScheme.secondaryContainer,
            iconBackground = MaterialTheme.colorScheme.onSecondaryContainer,
            title = "pawcode Development",
            subtitle = stringResource(R.string.website),
            onClick = { onOpenWebsite(context.getString(R.string.website_link)) },
          )

          SettingsItem(
            icon = Icons.Filled.Code,
            iconColor = MaterialTheme.colorScheme.secondaryContainer,
            iconBackground = MaterialTheme.colorScheme.onSecondaryContainer,
            title = stringResource(R.string.source_code),
            subtitle = stringResource(R.string.github_repository),
            onClick = { onOpenWebsite("https://github.com/pawcoding/card-store") },
          )

          SettingsItem(
            icon = Icons.Filled.Store,
            iconColor = MaterialTheme.colorScheme.secondaryContainer,
            iconBackground = MaterialTheme.colorScheme.onSecondaryContainer,
            title = stringResource(R.string.playstore),
            subtitle = stringResource(R.string.playstore_description),
            onClick = {
              onOpenWebsite("https://play.google.com/store/apps/details?id=de.pawcode.cardstore")
            },
          )

          SettingsItem(
            icon = Icons.Filled.BugReport,
            iconColor = MaterialTheme.colorScheme.secondaryContainer,
            iconBackground = MaterialTheme.colorScheme.onSecondaryContainer,
            title = stringResource(R.string.report_issue),
            subtitle = stringResource(R.string.github_issues),
            onClick = { onOpenWebsite("https://github.com/pawcoding/card-store/issues") },
          )
        }

        // Technologies Group
        SettingsGroup(title = stringResource(R.string.technologies)) {
          TECHNOLOGIES.forEach { technology ->
            SettingsItem(
              icon = technology.icon,
              iconColor = MaterialTheme.colorScheme.tertiaryContainer,
              iconBackground = MaterialTheme.colorScheme.onTertiaryContainer,
              title = technology.name,
              subtitle = null,
              onClick = { onOpenWebsite(technology.url) },
            )
          }
        }
      }
    }
  }
}

@Preview
@Preview(device = "id:pixel_tablet")
@Composable
fun PreviewAboutScreenComponent() {
  val packageInfo = PackageInfo()
  packageInfo.packageName = "de.pawcode.cardstore.debug"
  packageInfo.longVersionCode = 0
  packageInfo.versionName = "0.0.0"

  AboutScreenComponent(packageInfo = packageInfo, onBack = {}, onOpenWebsite = {})
}
