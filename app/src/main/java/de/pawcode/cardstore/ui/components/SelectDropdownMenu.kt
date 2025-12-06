package de.pawcode.cardstore.ui.components

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.enums.SortAttribute

data class DropdownOption<TValue : Enum<TValue>>(
  val title: String,
  @param:DrawableRes val icon: Int,
  val value: TValue,
)

@Composable
fun <TValue : Enum<TValue>> SelectDropdownMenu(
  @DrawableRes icon: Int,
  title: String,
  value: TValue?,
  values: List<DropdownOption<TValue>>,
  onValueChange: (TValue) -> Unit,
  initiallyExpanded: Boolean = false,
) {
  var dropdownExpanded by remember { mutableStateOf(initiallyExpanded) }

  IconButton(onClick = { dropdownExpanded = !dropdownExpanded }) {
    Icon(painterResource(icon), contentDescription = title)
  }
  DropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
    values.forEach { option ->
      DropdownMenuItem(
        text = { Text(text = option.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        leadingIcon = {
          Icon(painter = painterResource(option.icon), contentDescription = option.title)
        },
        trailingIcon = {
          if (value == option.value) {
            Icon(painterResource(R.drawable.check_solid), contentDescription = null)
          }
        },
        onClick = {
          onValueChange(option.value)
          dropdownExpanded = false
        },
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectDropdownMenu() {
  SelectDropdownMenu(
    icon = R.drawable.sort_solid,
    title = stringResource(R.string.cards_sort),
    value = SortAttribute.INTELLIGENT,
    values =
      listOf(
        DropdownOption(
          title = stringResource(R.string.sort_intelligent),
          icon = R.drawable.wand_shine_solid,
          value = SortAttribute.INTELLIGENT,
        ),
        DropdownOption(
          title = stringResource(R.string.sort_alphabetically),
          icon = R.drawable.sort_by_alpha_solid,
          value = SortAttribute.ALPHABETICALLY,
        ),
        DropdownOption(
          title = stringResource(R.string.sort_most_used),
          icon = R.drawable.trending_up_solid,
          value = SortAttribute.MOST_USED,
        ),
        DropdownOption(
          title = stringResource(R.string.sort_recently_used),
          icon = R.drawable.history_solid,
          value = SortAttribute.RECENTLY_USED,
        ),
      ),
    onValueChange = {},
    initiallyExpanded = true,
  )
}
