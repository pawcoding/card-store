package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.data.entities.Card
import de.pawcode.cardstore.data.entities.EXAMPLE_CARD
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun CardsListComponent(
    cards: List<Card>,
    onEditCard: (Card) -> Unit,
    onDeleteCard: (Card) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(
            items = cards,
            key = { card -> card.id }
        ) { card ->
            CardComponent(
                card,
                onEditCard = { onEditCard(card) },
                onDeleteCard = { onDeleteCard(card) })
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
            EXAMPLE_CARD.copy(id = Uuid.random().toString()),
            EXAMPLE_CARD.copy(id = Uuid.random().toString())
        ),
        onEditCard = {},
        onDeleteCard = {}
    )
}