package de.pawcode.cardstore.ui.sheets

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.repositories.CardRepository
import de.pawcode.cardstore.data.database.repositories.LabelRepository
import de.pawcode.cardstore.data.managers.PreferencesManager
import de.pawcode.cardstore.data.services.BackupService
import de.pawcode.cardstore.data.services.BiometricAuthService
import de.pawcode.cardstore.ui.dialogs.ConfirmDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupSheet(visible: Boolean, onDismiss: () -> Unit) {

  val context = LocalContext.current
  val preferencesManager = PreferencesManager(context)
  val biometricEnabled by preferencesManager.biometricEnabled.collectAsState(initial = false)
  val scope = rememberCoroutineScope()

  val cardRepository = CardRepository(context)
  val labelRepository = LabelRepository(context)

  var pendingRestoreUri by remember { mutableStateOf<Uri?>(null) }
  var showRestoreConfirmDialog by remember { mutableStateOf(false) }

  val createBackupLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) {
      uri ->
      onDismiss()
      if (uri == null) return@rememberLauncherForActivityResult
      scope.launch {
        BackupService.performCreateBackup(context, uri, cardRepository, labelRepository)
          .onSuccess {
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
      if (uri == null) {
        onDismiss()
        return@rememberLauncherForActivityResult
      }
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
        onError = { onDismiss() },
      )
    } else {
      val date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
      createBackupLauncher.launch("cardstore-backup-$date.json")
    }
  }

  if (showRestoreConfirmDialog) {
    ConfirmDialog(
      onConfirmation = {
        showRestoreConfirmDialog = false
        val uri = pendingRestoreUri ?: return@ConfirmDialog
        pendingRestoreUri = null
        scope.launch {
          BackupService.performRestoreBackup(context, uri, cardRepository)
            .onSuccess {
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
          onDismiss()
        }
      },
      onDismissRequest = {
        showRestoreConfirmDialog = false
        pendingRestoreUri = null
        onDismiss()
      },
      dialogTitle = stringResource(R.string.restore_confirm_title),
      dialogText = stringResource(R.string.restore_confirm_body),
      confirmText = stringResource(R.string.backup_restore),
      allowLightDismiss = false,
    )
  }

  if (!visible) return

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(),
  ) {
    OptionSheet(
      Option(
        label = stringResource(R.string.backup_create),
        icon = R.drawable.save_solid,
        onClick = { triggerCreateBackup() },
      ),
      Option(
        label = stringResource(R.string.backup_restore),
        icon = R.drawable.file_open_solid,
        onClick = { restoreBackupLauncher.launch(arrayOf("application/json")) },
      ),
    )
  }
}
