package de.pawcode.cardstore.ui.screens

import android.content.Intent
import android.content.pm.PackageInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import de.pawcode.cardstore.R
import de.pawcode.cardstore.ui.components.AppBar

data class Technology(val name: String, val url: String)

val TECHNOLOGIES =
  listOf<Technology>(
    Technology(name = "Kotlin", url = "https://kotlinlang.org/"),
    Technology(name = "Jetpack Compose", url = "https://developer.android.com/jetpack/compose"),
    Technology(name = "Room", url = "https://developer.android.com/jetpack/androidx/releases/room"),
    Technology(name = "Material Design 3", url = "https://m3.material.io/"),
    Technology(name = "ML Kit", url = "https://developers.google.com/ml-kit"),
    Technology(
      name = "Google Code-Scanner",
      url = "https://developers.google.com/ml-kit/vision/barcode-scanning/code-scanner",
    ),
    Technology(name = "ColorPickerView", url = "https://github.com/skydoves/ColorPickerView"),
    Technology(name = "ComposedBarcodes", url = "https://github.com/simonsickle/ComposedBarcodes"),
    Technology(name = "RevealSwipe", url = "https://github.com/ch4rl3x/RevealSwipe"),
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
      Column(modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth()) {
        Text(
          text = stringResource(R.string.about),
          style = MaterialTheme.typography.headlineSmall,
          modifier = Modifier.padding(top = 24.dp, start = 16.dp, bottom = 16.dp, end = 16.dp),
        )

        HorizontalDivider()

        ListItem(
          headlineContent = {
            Text(stringResource(R.string.version) + if (isDebug) " (debug)" else "")
          },
          supportingContent = { Text(versionName + " (${packageInfo.longVersionCode})") },
          trailingContent = {
            if (hasVersionName) {
              Icon(
                Icons.AutoMirrored.Filled.NavigateNext,
                contentDescription = stringResource(R.string.changelog_view),
              )
            } else {
              null
            }
          },
          modifier =
            if (hasVersionName) {
              Modifier.clickable {
                onOpenWebsite("https://github.com/pawcoding/card-store/releases/tag/v$versionName")
              }
            } else {
              Modifier
            },
        )

        HorizontalDivider()

        ListItem(
          headlineContent = { Text("pawcode Development") },
          supportingContent = { Text(stringResource(R.string.website)) },
          trailingContent = {
            Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
          },
          modifier = Modifier.clickable { onOpenWebsite(context.getString(R.string.website_link)) },
        )

        HorizontalDivider()

        ListItem(
          headlineContent = { Text(stringResource(R.string.source_code)) },
          supportingContent = { Text(stringResource(R.string.github_repository)) },
          trailingContent = {
            Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
          },
          modifier = Modifier.clickable { onOpenWebsite("https://github.com/pawcoding/card-store") },
        )

        HorizontalDivider()

        ListItem(
          headlineContent = { Text(stringResource(R.string.playstore)) },
          supportingContent = { Text(stringResource(R.string.playstore_description)) },
          trailingContent = {
            Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
          },
          modifier =
            Modifier.clickable {
              onOpenWebsite("https://play.google.com/store/apps/details?id=de.pawcode.cardstore")
            },
        )

        HorizontalDivider()

        ListItem(
          headlineContent = { Text(stringResource(R.string.report_issue)) },
          supportingContent = { Text(stringResource(R.string.github_issues)) },
          trailingContent = {
            Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
          },
          modifier =
            Modifier.clickable { onOpenWebsite("https://github.com/pawcoding/card-store/issues") },
        )

        HorizontalDivider()

        Text(
          text = stringResource(R.string.technologies),
          style = MaterialTheme.typography.headlineSmall,
          modifier = Modifier.padding(top = 24.dp, start = 16.dp, bottom = 16.dp, end = 16.dp),
        )

        HorizontalDivider()

        TECHNOLOGIES.forEach { technology ->
          ListItem(
            headlineContent = { Text(technology.name) },
            trailingContent = {
              Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
            },
            modifier = Modifier.clickable { onOpenWebsite(technology.url) },
          )

          HorizontalDivider()
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
