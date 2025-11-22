package de.pawcode.cardstore.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL_LIST
import de.pawcode.cardstore.data.database.entities.LabelEntity

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LabelsListComponent(
  labels: List<LabelEntity>,
  selected: String?,
  onLabelClick: (LabelEntity) -> Unit,
  onEdit: () -> Unit,
) {
  val scrollState = rememberScrollState()

  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(
      modifier = Modifier.weight(1f).horizontalScroll(scrollState),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      if (labels.isEmpty()) {
        Text(
          text = stringResource(R.string.labels_none),
          style = MaterialTheme.typography.bodyLarge,
        )
      } else {
        labels.forEach { label ->
          val chipSelected = label.labelId == selected
          FilterChip(
            selected = chipSelected,
            onClick = { onLabelClick(label) },
            shape = MaterialTheme.shapes.medium,
            label = {
              Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = label.name,
                style = MaterialTheme.typography.bodyLarge,
              )
            },
            leadingIcon = {
              AnimatedVisibility(visible = chipSelected) {
                Icon(painterResource(R.drawable.check_solid), contentDescription = null)
              }
            },
          )
        }
      }
    }

    FilledIconButton(
      modifier =
        Modifier.padding(start = 8.dp)
          .size(
            IconButtonDefaults.smallContainerSize(IconButtonDefaults.IconButtonWidthOption.Wide)
          ),
      shapes =
        IconButtonDefaults.shapes(
          shape = IconButtonDefaults.smallSquareShape,
          pressedShape = IconButtonDefaults.smallPressedShape,
        ),
      onClick = { onEdit() },
    ) {
      Icon(
        painter =
          painterResource(
            if (labels.isNotEmpty()) R.drawable.edit_note_solid else R.drawable.add_solid
          ),
        contentDescription = stringResource(R.string.labels_edit),
        modifier = Modifier.size(IconButtonDefaults.smallIconSize),
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewLabelsListComponent() {
  LabelsListComponent(
    labels = EXAMPLE_LABEL_LIST,
    selected = EXAMPLE_LABEL.labelId,
    onLabelClick = {},
    onEdit = {},
  )
}

@Preview(showBackground = true)
@Composable
fun PreviewLabelsListComponentEmpty() {
  LabelsListComponent(labels = listOf(), selected = null, onLabelClick = {}, onEdit = {})
}
