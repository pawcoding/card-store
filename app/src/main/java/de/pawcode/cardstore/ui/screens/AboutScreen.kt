package de.pawcode.cardstore.ui.screens

import android.content.Intent
import android.content.pm.PackageInfo
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.data.services.BiometricAuthService
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.SettingsGroup
import de.pawcode.cardstore.ui.components.SettingsItem
import kotlinx.coroutines.launch

data class Technology(val name: String, val url: String, @param:DrawableRes val icon: Int)

val TECHNOLOGIES =
  listOf(
    Technology(
      name = "Kotlin",
      url = "https://kotlinlang.org/",
      icon = R.drawable.data_object_solid,
    ),
    Technology(
      name = "Jetpack Compose",
      url = "https://developer.android.com/jetpack/compose",
      icon = R.drawable.android_solid,
    ),
    Technology(
      name = "Room",
      url = "https://developer.android.com/jetpack/androidx/releases/room",
      icon = R.drawable.storage_solid,
    ),
    Technology(
      name = "Material Design 3",
      url = "https://m3.material.io/",
      icon = R.drawable.palette_solid,
    ),
    Technology(
      name = "ML Kit",
      url = "https://developers.google.com/ml-kit",
      icon = R.drawable.barcode_scanner_solid,
    ),
    Technology(
      name = "Google Code-Scanner",
      url = "https://developers.google.com/ml-kit/vision/barcode-scanning/code-scanner",
      icon = R.drawable.photo_camera_solid,
    ),
    Technology(
      name = "ColorPickerView",
      url = "https://github.com/skydoves/ColorPickerView",
      icon = R.drawable.colorize_solid,
    ),
    Technology(
      name = "ComposedBarcodes",
      url = "https://github.com/simonsickle/ComposedBarcodes",
      icon = R.drawable.qr_code_2_solid,
    ),
    Technology(
      name = "RevealSwipe",
      url = "https://github.com/ch4rl3x/RevealSwipe",
      icon = R.drawable.swipe_solid,
    ),
  )

@Composable
fun AboutScreen(navController: NavController) {
  val context = LocalContext.current
  val preferencesManager = PreferencesManager(context)
  val biometricEnabled by preferencesManager.biometricEnabled.collectAsState(initial = false)
  val scope = rememberCoroutineScope()

  var packageInfo = PackageInfo()
  try {
    packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
  } catch (e: Exception) {
    e.printStackTrace()
  }

  AboutScreenComponent(
    packageInfo = packageInfo,
    biometricAvailable = BiometricAuthService.isBiometricAvailable(context),
    biometricEnabled = biometricEnabled,
    onBack = { navController.popBackStack() },
    onOpenWebsite = { context.startActivity(Intent(Intent.ACTION_VIEW, it.toUri())) },
    onBiometricToggle = { enabled ->
      if (enabled) {
        // Show authentication prompt before enabling
        val activity = context as? androidx.fragment.app.FragmentActivity
        if (activity != null) {
          BiometricAuthService.authenticate(
            activity = activity,
            title = context.getString(R.string.biometric_activate_title),
            subtitle = context.getString(R.string.biometric_activate_subtitle),
            onSuccess = { scope.launch { preferencesManager.saveBiometricEnabled(true) } },
            onError = {
              // Don't enable if authentication fails
            },
          )
        }
      } else {
        // Disable without authentication
        scope.launch { preferencesManager.saveBiometricEnabled(false) }
      }
    },
  )
}

@Composable
fun AboutScreenComponent(
  packageInfo: PackageInfo,
  biometricAvailable: Boolean,
  biometricEnabled: Boolean,
  onBack: () -> Unit,
  onOpenWebsite: (String) -> Unit,
  onBiometricToggle: (Boolean) -> Unit,
) {
  val context = LocalContext.current

  val hasVersionName = packageInfo.versionName != null
  val versionName = packageInfo.versionName ?: "Unknown version"
  val isDebug = packageInfo.packageName.endsWith(".debug")

  Scaffold(
    topBar = {
      AppBar(
        title = stringResource(R.string.app_name),
        subtitle = stringResource(R.string.about),
        onBack = { onBack() },
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier.padding(innerPadding).fillMaxSize().verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Column(
        modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        // App Information Group
        SettingsGroup(title = stringResource(R.string.about)) {
          SettingsItem(
            icon = painterResource(R.drawable.info),
            iconColor = MaterialTheme.colorScheme.onSecondaryFixedVariant,
            iconBackground = MaterialTheme.colorScheme.primaryFixed,
            title = stringResource(R.string.version) + if (isDebug) " (debug)" else "",
            subtitle = versionName + " (${packageInfo.longVersionCode})",
            onClick = {
              if (hasVersionName) {
                onOpenWebsite("https://github.com/pawcoding/card-store/releases/tag/v$versionName")
              }
            },
          )
        }

        // App Settings Group
        SettingsGroup(title = stringResource(R.string.settings)) {
          SettingsItem(
            icon =
              painterResource(
                if (biometricEnabled) R.drawable.lock_solid else R.drawable.lock_open_solid
              ),
            iconColor = MaterialTheme.colorScheme.onSecondaryFixedVariant,
            iconBackground = MaterialTheme.colorScheme.primaryFixed,
            title = stringResource(R.string.biometric_title),
            subtitle =
              stringResource(
                if (biometricAvailable) R.string.biometric_subtitle
                else R.string.biometric_not_available
              ),
            trailingContent = {
              Switch(
                checked = biometricEnabled,
                onCheckedChange = onBiometricToggle,
                enabled = biometricAvailable,
              )
            },
          )
        }

        // Links Group
        SettingsGroup(title = stringResource(R.string.links)) {
          SettingsItem(
            icon = painterResource(R.drawable.icon),
            iconColor = MaterialTheme.colorScheme.onSecondaryFixedVariant,
            iconBackground = MaterialTheme.colorScheme.secondaryFixed,
            title = "pawcode Development",
            subtitle = stringResource(R.string.website),
            onClick = { onOpenWebsite(context.getString(R.string.website_link)) },
          )

          SettingsItem(
            icon = painterResource(R.drawable.github_mark),
            iconColor = MaterialTheme.colorScheme.onSecondaryFixedVariant,
            iconBackground = MaterialTheme.colorScheme.secondaryFixed,
            title = stringResource(R.string.source_code),
            subtitle = stringResource(R.string.github_repository),
            onClick = { onOpenWebsite("https://github.com/pawcoding/card-store") },
          )

          SettingsItem(
            icon = painterResource(R.drawable.google_play),
            iconColor = MaterialTheme.colorScheme.onSecondaryFixedVariant,
            iconBackground = MaterialTheme.colorScheme.secondaryFixed,
            title = stringResource(R.string.playstore),
            subtitle = stringResource(R.string.playstore_description),
            onClick = {
              onOpenWebsite("https://play.google.com/store/apps/details?id=de.pawcode.cardstore")
            },
          )

          SettingsItem(
            icon = painterResource(R.drawable.bug_report_solid),
            iconColor = MaterialTheme.colorScheme.onSecondaryFixedVariant,
            iconBackground = MaterialTheme.colorScheme.secondaryFixed,
            title = stringResource(R.string.report_issue),
            subtitle = stringResource(R.string.github_issues),
            onClick = { onOpenWebsite("https://github.com/pawcoding/card-store/issues") },
          )
        }

        // Technologies Group
        SettingsGroup(title = stringResource(R.string.technologies)) {
          TECHNOLOGIES.forEach { technology ->
            SettingsItem(
              icon = painterResource(technology.icon),
              iconColor = MaterialTheme.colorScheme.onTertiaryFixedVariant,
              iconBackground = MaterialTheme.colorScheme.tertiaryFixed,
              title = technology.name,
              subtitle = null,
              onClick = { onOpenWebsite(technology.url) },
            )
          }
        }

        Image(
          painter =
            if (isSystemInDarkTheme()) painterResource(R.drawable.pawcode_light)
            else painterResource(R.drawable.pawcode_dark),
          contentDescription = "pawcode Development",
          modifier = Modifier.padding(top = 24.dp).padding(12.dp).fillMaxWidth(.5f),
        )
      }
    }
  }
}

@Preview(showSystemUi = true)
@Preview(device = "id:pixel_tablet", showSystemUi = true)
@Composable
fun PreviewAboutScreenComponent() {
  val packageInfo = PackageInfo()
  packageInfo.packageName = "de.pawcode.cardstore.debug"
  packageInfo.longVersionCode = 0
  packageInfo.versionName = "0.0.0"

  AboutScreenComponent(
    packageInfo = packageInfo,
    biometricAvailable = true,
    biometricEnabled = true,
    onBack = {},
    onOpenWebsite = {},
    onBiometricToggle = {},
  )
}
