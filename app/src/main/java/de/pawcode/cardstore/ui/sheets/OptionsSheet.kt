package de.pawcode.cardstore.ui.sheets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.EXAMPLE_CARD
import de.pawcode.cardstore.utils.isLightColor

/** Option to be displayed in the [OptionSheet]. */
data class Option(
  /** Label that is shown to the user. */
  val label: String,
  /** Icons that is shown in front of the label. */
  @param:DrawableRes val icon: Int,
  /** Action that is executed when the option is clicked. */
  val onClick: () -> Unit,
)

@Composable
fun OptionSheet(vararg options: Option, content: @Composable (() -> Unit)? = null) {
  Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
    if (content != null) {
      content()

      HorizontalDivider()
    }

    options.forEach { option ->
      ListItem(
        leadingContent = { Icon(painterResource(option.icon), contentDescription = null) },
        headlineContent = { Text(option.label) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier =
          Modifier.clip(MaterialTheme.shapes.medium).clickable(onClick = { option.onClick() }),
      )
    }
  }
}

@Composable
fun OptionSheetInfo(
  backgroundColor: Color,
  iconTint: Color,
  @DrawableRes icon: Int,
  title: String,
  subtitle: String? = null,
) {
  Row(
    modifier = Modifier.padding(16.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Box(
      modifier = Modifier.size(64.dp).clip(MaterialTheme.shapes.small).background(backgroundColor),
      contentAlignment = Alignment.Center,
    ) {
      Icon(
        painterResource(icon),
        contentDescription = null,
        modifier = Modifier.size(48.dp),
        tint = iconTint,
      )
    }

    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )

      if (subtitle != null) {
        Text(
          subtitle,
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewOptionSheet() {
  val card = EXAMPLE_CARD

  val color by remember { mutableStateOf(Color(card.color)) }
  val isLightColor by remember { derivedStateOf { isLightColor(color) } }

  OptionSheet(
    Option(label = "Edit card", icon = R.drawable.edit_solid, onClick = {}),
    Option(label = "Delete card", icon = R.drawable.delete_forever_solid, onClick = {}),
  ) {
    OptionSheetInfo(
      backgroundColor = color,
      iconTint = if (isLightColor) Color.Black else Color.White,
      icon = R.drawable.credit_card,
      title = card.storeName,
      subtitle = card.cardNumber,
    )
  }
}
