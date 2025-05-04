package de.pawcode.cardstore.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColorInt
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import de.pawcode.cardstore.R

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun ColorPickerDialog(color: Color, onDismiss: (Color?) -> Unit) {
  val controller = rememberColorPickerController()

  var hexValue by remember {
    mutableStateOf(controller.selectedColor.value.toArgb().toHexString(HexFormat.UpperCase).drop(2))
  }

  fun onColorInput(input: String) {
    var newValue = input.uppercase().filter { it.isDigit() || it in 'A'..'F' }.take(6)

    hexValue = newValue

    if (newValue.length != 6) {
      return
    }

    try {
      val color = Color(("#$newValue").toColorInt())
      controller.selectByColor(color, true)
    } catch (_: IllegalArgumentException) {}
  }

  LaunchedEffect(controller.selectedColor.value) {
    hexValue = controller.selectedColor.value.toArgb().toHexString(HexFormat.UpperCase).drop(2)
  }

  Dialog(
    onDismissRequest = { onDismiss(null) },
    properties = DialogProperties(dismissOnClickOutside = false),
  ) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)) {
        Text(
          text = stringResource(R.string.card_pick_color),
          style = MaterialTheme.typography.headlineSmall,
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Box(
            modifier =
              Modifier.height(IntrinsicSize.Min)
                .size(56.dp)
                .clip(MaterialTheme.shapes.small)
                .background(controller.selectedColor.value)
          ) {}

          OutlinedTextField(
            value = hexValue,
            onValueChange = { onColorInput(it) },
            placeholder = { Text(stringResource(R.string.card_color_hex)) },
            prefix = { Text(text = "#", fontFamily = FontFamily.Monospace) },
            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions =
              KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Done,
              ),
          )
        }

        HsvColorPicker(
          modifier = Modifier.fillMaxWidth().height(250.dp).padding(10.dp),
          controller = controller,
          initialColor = color,
        )

        BrightnessSlider(
          modifier = Modifier.fillMaxWidth().height(50.dp).padding(10.dp),
          controller = controller,
          initialColor = color,
          wheelRadius = 15.dp,
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
          TextButton(onClick = { onDismiss(null) }) { Text(stringResource(R.string.common_cancel)) }

          TextButton(onClick = { onDismiss(controller.selectedColor.value) }) {
            Text(stringResource(R.string.common_save))
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun PreviewColorPickerDialog() {
  ColorPickerDialog(Color.White, onDismiss = {})
}
