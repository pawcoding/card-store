package de.pawcode.cardstore.ui.screens

import android.app.backup.BackupManager
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.repositories.CardRepository
import de.pawcode.cardstore.data.database.repositories.LabelRepository
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.data.services.BackupService
import de.pawcode.cardstore.data.services.BiometricAuthService
import de.pawcode.cardstore.navigation.Navigator
import de.pawcode.cardstore.ui.components.AppBar
import de.pawcode.cardstore.ui.components.SettingsGroup
import de.pawcode.cardstore.ui.components.SettingsItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.first
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navigator: Navigator) {
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

  val isAutoBackupEnabled = runCatching {
    val bm = BackupManager(context)
    val method = bm.javaClass.getMethod("isBackupEnabled")
    method.invoke(bm) as Boolean
  }
    .getOrDefault(false)

  val cardRepository = CardRepository(context)
  val labelRepository = LabelRepository(context)

  var showBackupSheet by remember { mutableStateOf(false) }
  var pendingRestoreUri by remember { mutableStateOf<Uri?>(null) }
  var showRestoreConfirmDialog by remember { mutableStateOf(false) }

  val createBackupLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) {
      uri ->
      if (uri == null) return@rememberLauncherForActivityResult
      scope.launch {
        runCatching {
          val cards = cardRepository.allCards.first()
          val labels = labelRepository.allLabels.first()
          BackupService.createBackup(context, uri, cards, labels)
          Toast.makeText(context, context.getString(R.string.backup_success), Toast.LENGTH_SHORT)
            .show()
        }
          .onFailure {
            Toast.makeText(context, context.getString(R.string.backup_failed), Toast.LENGTH_SHORT)
              .show()
          }
      }
    }

  val restoreBackupLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
      if (uri == null) return@rememberLauncherForActivityResult
      pendingRestoreUri = uri
      showRestoreConfirmDialog = true
    }

  fun triggerCreateBackup() {
    if (biometricEnabled) {
      val activity = context as? androidx.fragment.app.FragmentActivity ?: return
      BiometricAuthService.authenticate(
        activity = activity,
        title = context.getString(R.string.backup_biometric_title),
        subtitle = context.getString(R.string.backup_biometric_subtitle),
        onSuccess = {
          val date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
          createBackupLauncher.launch("cardstore-backup-$date.json")
        },
        onError = {},
      )
    } else {
      val date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
      createBackupLauncher.launch("cardstore-backup-$date.json")
    }
  }

  if (showRestoreConfirmDialog) {
    AlertDialog(
      onDismissRequest = {
        showRestoreConfirmDialog = false
        pendingRestoreUri = null
      },
      title = { Text(stringResource(R.string.restore_confirm_title)) },
      text = { Text(stringResource(R.string.restore_confirm_body)) },
      confirmButton = {
        TextButton(
          onClick = {
            showRestoreConfirmDialog = false
            val uri = pendingRestoreUri ?: return@TextButton
            pendingRestoreUri = null
            scope.launch {
              runCatching {
                val data = BackupService.readBackup(context, uri)
                if (!BackupService.validateBackup(data)) error("Invalid backup")
                val (cards, labels, crossRefs) = BackupService.toEntities(data)
                cardRepository.restoreBackup(cards, labels, crossRefs)
                Toast.makeText(
                    context,
                    context.getString(R.string.restore_success),
                    Toast.LENGTH_SHORT,
                  )
                  .show()
              }
                .onFailure {
                  Toast.makeText(
                      context,
                      context.getString(R.string.restore_failed),
                      Toast.LENGTH_SHORT,
                    )
                    .show()
                }
            }
          }
        ) {
          Text(stringResource(android.R.string.ok))
        }
      },
      dismissButton = {
        TextButton(
          onClick = {
            showRestoreConfirmDialog = false
            pendingRestoreUri = null
          }
        ) {
          Text(stringResource(android.R.string.cancel))
        }
      },
    )
  }

  if (showBackupSheet) {
    ModalBottomSheet(
      onDismissRequest = { showBackupSheet = false },
      sheetState = rememberModalBottomSheetState(),
    ) {
      Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
        ListItem(
          modifier =
            Modifier.fillMaxWidth().clickable {
              showBackupSheet = false
              triggerCreateBackup()
            },
          headlineContent = { Text(stringResource(R.string.backup_create)) },
          leadingContent = {
            Icon(painter = painterResource(R.drawable.save_solid), contentDescription = null)
          },
        )
        ListItem(
          modifier =
            Modifier.fillMaxWidth().clickable {
              showBackupSheet = false
              restoreBackupLauncher.launch(arrayOf("application/json"))
            },
          headlineContent = { Text(stringResource(R.string.backup_restore)) },
          leadingContent = {
            Icon(painter = painterResource(R.drawable.file_open_solid), contentDescription = null)
          },
        )
      }
    }
  }

  AboutScreenComponent(
    packageInfo = packageInfo,
    biometricAvailable = BiometricAuthService.isBiometricAvailable(context),
    biometricEnabled = biometricEnabled,
    isAutoBackupEnabled = isAutoBackupEnabled,
    onBack = { navigator.goBack() },
    onOpenWebsite = { context.startActivity(Intent(Intent.ACTION_VIEW, it.toUri())) },
    onBiometricToggle = { enabled ->
      if (enabled) {
        val activity = context as? androidx.fragment.app.FragmentActivity
        if (activity != null) {
          BiometricAuthService.authenticate(
            activity = activity,
            title = context.getString(R.string.biometric_activate_title),
            subtitle = context.getString(R.string.biometric_activate_subtitle),
            onSuccess = {
              scope.launch {
                preferencesManager.saveBiometricEnabled(true)
                Toast.makeText(
                    context,
                    context.getString(R.string.biometric_enabled),
                    Toast.LENGTH_SHORT,
                  )
                  .show()
              }
            },
            onError = {
              // Don't enable if authentication fails
            },
          )
        }
      } else {
        scope.launch { preferencesManager.saveBiometricEnabled(false) }
      }
    },
    onManualBackupClick = { showBackupSheet = true },
  )
}

@Composable
fun AboutScreenComponent(
  packageInfo: PackageInfo,
  biometricAvailable: Boolean,
  biometricEnabled: Boolean,
  isAutoBackupEnabled: Boolean,
  onBack: () -> Unit,
  onOpenWebsite: (String) -> Unit,
  onBiometricToggle: (Boolean) -> Unit,
  onManualBackupClick: () -> Unit,
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

          SettingsItem(
            icon =
              painterResource(
                if (isAutoBackupEnabled) R.drawable.cloud_done_solid else R.drawable.cloud_off_solid
              ),
            iconColor = MaterialTheme.colorScheme.onSecondaryFixedVariant,
            iconBackground = MaterialTheme.colorScheme.primaryFixed,
            title = stringResource(R.string.auto_backup_title),
            subtitle =
              stringResource(
                if (isAutoBackupEnabled) R.string.auto_backup_active
                else R.string.auto_backup_inactive
              ),
          )

          SettingsItem(
            icon = painterResource(R.drawable.archive_solid),
            iconColor = MaterialTheme.colorScheme.onSecondaryFixedVariant,
            iconBackground = MaterialTheme.colorScheme.primaryFixed,
            title = stringResource(R.string.manual_backup_title),
            subtitle = stringResource(R.string.manual_backup_subtitle),
            onClick = onManualBackupClick,
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

@Preview
@Preview(device = "id:pixel_tablet")
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
    isAutoBackupEnabled = true,
    onBack = {},
    onOpenWebsite = {},
    onBiometricToggle = {},
    onManualBackupClick = {},
  )
}
