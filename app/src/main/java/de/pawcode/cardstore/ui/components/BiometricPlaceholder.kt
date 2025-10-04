package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R

@Composable
fun BiometricPlaceholder(onRetry: () -> Unit) {
  Surface(
    modifier = Modifier.fillMaxSize(),
    color = colorResource(android.R.color.system_accent1_700),
  ) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(32.dp),
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_launcher_foreground),
          contentDescription = stringResource(R.string.app_name),
          modifier = Modifier.size(200.dp),
        )

        FilledTonalButton(onClick = onRetry, shape = MaterialTheme.shapes.small) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
              Icons.Default.Fingerprint,
              contentDescription = null,
              modifier = Modifier.size(40.dp),
            )

            Text(
              text = stringResource(R.string.biometric_auth_title),
              style = MaterialTheme.typography.headlineMedium,
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
fun PreviewBiometricPlaceholder() {
  BiometricPlaceholder(onRetry = {})
}
