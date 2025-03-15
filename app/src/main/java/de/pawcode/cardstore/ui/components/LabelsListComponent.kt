package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL
import de.pawcode.cardstore.data.database.entities.EXAMPLE_LABEL_LIST
import de.pawcode.cardstore.data.database.entities.LabelEntity

@Composable
fun LabelsListComponent(
    labels: List<LabelEntity>,
    selected: String?,
    onLabelClick: (LabelEntity) -> Unit,
    onEdit: () -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (labels.isEmpty()) {
                Text(
                    text = "No labels",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                labels.forEach { label ->
                    val chipSelected = label.labelId == selected
                    FilterChip(
                        selected = chipSelected,
                        onClick = { onLabelClick(label) },
                        label = {
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = label.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                    )
                }
            }
        }

        IconButton(
            onClick = { onEdit() }
        ) {
            Icon(
                imageVector = Icons.Filled.EditNote,
                contentDescription = "Edit labels",
                modifier = Modifier.size(32.dp)
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
        onEdit = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLabelsListComponentEmpty() {
    LabelsListComponent(
        labels = listOf(),
        selected = null,
        onLabelClick = {},
        onEdit = {}
    )
}
