package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_CARD
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun CardsListComponent(
    cards: List<CardEntity>,
    listState: LazyGridState,
    onCardClicked: (CardEntity) -> Unit,
    onCardLongPressed: (CardEntity) -> Unit,
) {
    if (cards.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Create your first card",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    } else {
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Adaptive(minSize = 300.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = cards,
                key = { card -> card.cardId }
            ) { card ->
                CardComponent(
                    card = card,
                    onClick = {
                        onCardClicked(card)
                    },
                    onLongPress = {
                        onCardLongPressed(card)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun PreviewCardsListComponent() {
    CardsListComponent(
        cards = listOf(
            EXAMPLE_CARD,
            EXAMPLE_CARD.copy(cardId = Uuid.random().toString()),
            EXAMPLE_CARD.copy(cardId = Uuid.random().toString())
        ),
        listState = rememberLazyGridState(),
        onCardClicked = {},
        onCardLongPressed = {}
    )
}

@Preview
@Composable
fun PreviewCardsListComponentEmpty() {
    CardsListComponent(
        cards = listOf(),
        listState = rememberLazyGridState(),
        onCardClicked = {},
        onCardLongPressed = {}
    )
}
