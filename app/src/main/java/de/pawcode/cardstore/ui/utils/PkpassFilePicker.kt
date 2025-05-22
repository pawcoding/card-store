package de.pawcode.cardstore.ui.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import de.pawcode.cardstore.utils.readPkpassContentFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PkpassFilePicker(onFileRead: (String) -> Unit, onCancel: () -> Unit) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val pickPkpassFileLauncher =
    rememberLauncherForActivityResult(
      contract = ActivityResultContracts.OpenDocument(),
      onResult = { uri ->
        if (uri == null) {
          onCancel()
          return@rememberLauncherForActivityResult
        }

        coroutineScope.launch(Dispatchers.IO) {
          val content = readPkpassContentFromUri(uri, context.contentResolver)
          if (content != null) {
            onFileRead(content)
          } else {
            onCancel()
          }
        }
      },
    )

  LaunchedEffect(null) { pickPkpassFileLauncher.launch(arrayOf("application/vnd.apple.pkpass")) }
}
