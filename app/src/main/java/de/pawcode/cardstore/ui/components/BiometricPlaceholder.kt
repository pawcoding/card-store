package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R

@Composable
fun BiometricPlaceholder(onRetry: () -> Unit) {
  Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(32.dp),
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_launcher_foreground),
          contentDescription = stringResource(R.string.app_name),
          modifier = Modifier.size(120.dp),
        )
        Text(
          text = stringResource(R.string.biometric_required),
          style = MaterialTheme.typography.headlineSmall,
        )
        Button(onClick = onRetry) { Text(text = stringResource(R.string.biometric_auth_retry)) }
      }
    }
  }
}

@Preview
@Composable
fun PreviewBiometricPlaceholder() {
  BiometricPlaceholder(onRetry = {})
}
