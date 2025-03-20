package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.enums.SortAttribute


data class DropdownOption<TValue : Enum<TValue>>(
    val title: String,
    val value: TValue
)

@Composable
fun <TValue : Enum<TValue>> SelectDropdownMenu(
    icon: ImageVector,
    title: String,
    value: TValue,
    values: List<DropdownOption<TValue>>,
    onValueChange: (TValue) -> Unit,
    initiallyExpanded: Boolean = false
) {
    var dropdownExpanded by remember { mutableStateOf(initiallyExpanded) }

    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        IconButton(
            onClick = { dropdownExpanded = !dropdownExpanded }
        ) {
            Icon(icon, contentDescription = title)
        }
        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false }
        ) {
            values.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.title) },
                    trailingIcon = {
                        if (value == option.value) {
                            Icon(Icons.Filled.Check, contentDescription = null)
                        }
                    },
                    onClick = {
                        onValueChange(option.value)
                        dropdownExpanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectDropdownMenu() {
    SelectDropdownMenu(
        icon = Icons.AutoMirrored.Filled.Sort,
        title = stringResource(R.string.cards_sort),
        value = SortAttribute.ALPHABETICALLY,
        values = listOf(
            DropdownOption(
                title = stringResource(R.string.sort_alphabetically),
                value = SortAttribute.ALPHABETICALLY
            ),
            DropdownOption(
                title = stringResource(R.string.sort_most_used),
                value = SortAttribute.MOST_USED
            ),
            DropdownOption(
                title = stringResource(R.string.sort_recently_used),
                value = SortAttribute.RECENTLY_USED
            )
        ),
        onValueChange = {},
        initiallyExpanded = true
    )
}