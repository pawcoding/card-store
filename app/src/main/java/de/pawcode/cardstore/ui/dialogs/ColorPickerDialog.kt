package de.pawcode.cardstore.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import de.pawcode.cardstore.R

@Composable
fun ColorPickerDialog(color: Color, onDismiss: (Color?) -> Unit) {
    val controller = rememberColorPickerController()

    Dialog(
        onDismissRequest = { onDismiss(null) }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), shape = MaterialTheme.shapes.large
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.card_pick_color),
                    style = MaterialTheme.typography.headlineSmall,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(
                                MaterialTheme.shapes.small
                            )
                            .background(controller.selectedColor.value),
                        contentAlignment = Alignment.Center
                    ) {}
                }

                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(10.dp),
                    controller = controller,
                    initialColor = color
                )

                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(10.dp),
                    controller = controller,
                    initialColor = color,
                    wheelRadius = 15.dp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onDismiss(null)
                        },
                    ) {
                        Text(stringResource(R.string.common_cancel))
                    }

                    TextButton(
                        onClick = {
                            onDismiss(controller.selectedColor.value)
                        },
                    ) {
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