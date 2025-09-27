package de.pawcode.cardstore.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simonsickle.compose.barcodes.BarcodeType
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.classes.CardWithLabels
import de.pawcode.cardstore.data.database.classes.emptyCardWithLabels
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL_LIST
import de.pawcode.cardstore.data.database.entities.LabelEntity
import de.pawcode.cardstore.ui.dialogs.ColorPickerDialog
import de.pawcode.cardstore.ui.utils.BarcodeScanner
import de.pawcode.cardstore.utils.isLightColor
import de.pawcode.cardstore.utils.mapBarcodeFormat
import de.pawcode.cardstore.utils.parseDeeplink

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditCardForm(
  modifier: Modifier = Modifier,
  card: CardWithLabels,
  labels: List<LabelEntity>,
  onCardUpdate: (CardWithLabels) -> Unit,
) {
  var showColorPicker by remember { mutableStateOf(false) }
  var showBarcodeScanner by remember { mutableStateOf(false) }

  val cardState by rememberUpdatedState(card)
  val color by remember { derivedStateOf { Color(cardState.card.color) } }
  val isLightColor by remember { derivedStateOf { isLightColor(color) } }
  val formatValid by remember {
    derivedStateOf {
      cardState.card.cardNumber == "" ||
        cardState.card.barcodeFormat.isValueValid(cardState.card.cardNumber)
    }
  }

  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier.fillMaxWidth().padding(16.dp),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Icon(
        Icons.Filled.Storefront,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(32.dp),
      )

      Text(
        text = stringResource(R.string.card_store_name),
        style = MaterialTheme.typography.bodyLarge,
      )
    }

    OutlinedTextField(
      value = card.card.storeName,
      onValueChange = { onCardUpdate(card.copy(card = card.card.copy(storeName = it))) },
      label = { Text(stringResource(R.string.card_store_name) + "*") },
      supportingText = { Text("*" + stringResource(R.string.common_required)) },
      modifier = Modifier.fillMaxWidth(),
      singleLine = true,
      keyboardOptions =
        KeyboardOptions(
          keyboardType = KeyboardType.Text,
          autoCorrectEnabled = true,
          capitalization = KeyboardCapitalization.Sentences,
          imeAction = ImeAction.Next,
        ),
    )

    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Icon(
          Icons.Filled.QrCode2,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(32.dp),
        )

        Text(text = stringResource(R.string.card_code), style = MaterialTheme.typography.bodyLarge)
      }

      OutlinedButton(onClick = { showBarcodeScanner = true }) {
        Icon(
          Icons.Filled.QrCodeScanner,
          contentDescription = null,
          modifier = Modifier.padding(end = 4.dp),
        )

        Text(
          text = stringResource(R.string.common_scan),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      OutlinedTextField(
        value = card.card.cardNumber,
        onValueChange = { onCardUpdate(card.copy(card = card.card.copy(cardNumber = it))) },
        label = { Text(stringResource(R.string.card_number) + "*") },
        supportingText = { Text("*" + stringResource(R.string.common_required)) },
        modifier = Modifier.weight(3f),
        singleLine = true,
        keyboardOptions =
          KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
      )

      var expanded by remember { mutableStateOf(false) }
      ExposedDropdownMenuBox(
        modifier = Modifier.weight(2f),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
      ) {
        OutlinedTextField(
          value = card.card.barcodeFormat.name,
          onValueChange = {},
          label = { Text(stringResource(R.string.card_barcode_type)) },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
          modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.SecondaryEditable, true),
          readOnly = true,
          singleLine = true,
          isError = !formatValid,
          supportingText = {
            if (!formatValid) {
              Text(text = stringResource(R.string.card_invalid_barcode_format))
            }
          },
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
          BarcodeType.entries
            .sortedBy { it.name }
            .forEach { type ->
              DropdownMenuItem(
                onClick = {
                  onCardUpdate(card.copy(card = card.card.copy(barcodeFormat = type)))
                  expanded = false
                },
                text = { Text(type.name) },
              )
            }
        }
      }
    }

    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Icon(
          Icons.Filled.Palette,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(32.dp),
        )

        Text(text = stringResource(R.string.card_color), style = MaterialTheme.typography.bodyLarge)
      }

      Box(
        modifier =
          Modifier.size(44.dp).clip(MaterialTheme.shapes.small).background(color).clickable {
            showColorPicker = true
          },
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Filled.Colorize,
          contentDescription = stringResource(R.string.card_pick_color),
          tint = if (isLightColor) Color.Black else Color.White,
        )
      }
    }

    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Icon(
        Icons.AutoMirrored.Filled.Label,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(32.dp),
      )

      Text(text = stringResource(R.string.card_labels), style = MaterialTheme.typography.bodyLarge)
    }

    if (labels.isNotEmpty()) {
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        labels.forEach { label ->
          val chipSelected =
            label.labelId == card.labels.find { it.labelId == label.labelId }?.labelId
          FilterChip(
            modifier = Modifier.padding(bottom = 4.dp),
            selected = chipSelected,
            onClick = {
              onCardUpdate(
                card.copy(
                  labels =
                    if (chipSelected) {
                      card.labels.filter { it.labelId != label.labelId }
                    } else {
                      card.labels + label
                    }
                )
              )
            },
            label = {
              Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = label.name,
                style = MaterialTheme.typography.bodyLarge,
              )
            },
            leadingIcon = {
              AnimatedVisibility(visible = chipSelected) {
                Icon(Icons.Filled.Check, contentDescription = null)
              }
            },
          )
        }
      }
    } else {
      Text(
        text = stringResource(R.string.card_no_labels),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(8.dp),
      )
    }
  }

  if (showColorPicker) {
    ColorPickerDialog(
      color = color,
      onDismiss = { newColor ->
        if (newColor != null) {
          onCardUpdate(card.copy(card = card.card.copy(color = newColor.toArgb())))
        }
        showColorPicker = false
      },
    )
  }

  if (showBarcodeScanner) {
    BarcodeScanner(
      onBarcodeDetected = { barcode ->
        val deeplink = parseDeeplink(barcode.rawValue ?: barcode.displayValue ?: "")
        if (deeplink != null) {
          onCardUpdate(
            card.copy(
              card =
                card.card.copy(
                  storeName = deeplink["storeName"] ?: "",
                  cardNumber = deeplink["cardNumber"] ?: "",
                  barcodeFormat = mapBarcodeFormat(deeplink["barcodeFormat"] ?: "QR_CODE"),
                  color = deeplink["color"]?.toIntOrNull() ?: Color.White.toArgb(),
                )
            )
          )
        } else {
          onCardUpdate(
            card.copy(
              card =
                card.card.copy(
                  cardNumber = barcode.rawValue ?: barcode.displayValue ?: "",
                  barcodeFormat = mapBarcodeFormat(barcode.format),
                )
            )
          )
        }
        showBarcodeScanner = false
      },
      onCancel = { showBarcodeScanner = false },
    )
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditCardForm() {
  EditCardForm(card = emptyCardWithLabels(), labels = EXAMPLE_LABEL_LIST, onCardUpdate = {})
}
