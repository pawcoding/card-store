package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import de.pawcode.cardstore.data.database.CardEntity
import de.pawcode.cardstore.data.database.EXAMPLE_CARD

@Composable
fun CardComponent(card: CardEntity, onClick: () -> Unit, onLongPress: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.586f)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onClick() }, onLongPress = { onLongPress() })
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(card.color.toColorInt())
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Text(
                text = card.storeName, style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun PreviewCardComponent() {
    CardComponent(card = EXAMPLE_CARD, onClick = {}, onLongPress = {})
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardComponent(card: Card, onEditCard: () -> Unit, onDeleteCard: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var isQrFullscreen by rememberSaveable { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.586f) // Aspect ratio of a credit card (85.60mm x 53.98mm)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(android.graphics.Color.parseColor(card.color))
        ),
        onClick = {
            isQrFullscreen = true
        }
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End)
        ) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Options")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Edit card") },
                    leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
                    onClick = {
                        expanded = false
                        onEditCard()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete card") },
                    leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
                    onClick = {
                        expanded = false
                        onDeleteCard()
                    }
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Text(text = card.store, style = MaterialTheme.typography.bodyLarge)
        }
    }

    if (isQrFullscreen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                isQrFullscreen = false
            },
            modifier = Modifier.safeDrawingPadding(),
            containerColor = Color(
                android.graphics.Color.parseColor(
                    card.color
                )
            )
        ) {
            CardModal(card)
        }
    }
}

@Preview
@Composable
fun PreviewCardComponent() {
    CardComponent(
        card = EXAMPLE_CARD,
        onEditCard = {},
        onDeleteCard = {}
    )
}*/
