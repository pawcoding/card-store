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

@Preview
@Composable
fun PreviewCardsListComponent() {
    CardsListComponent(
        cards = listOf(
            Card(
                id = "1",
                store = "Amazon",
                cardNumber = "1234 5678 9012 3456",
                color = "#FF0000"
            ),
            Card(
                id = "2",
                store = "Google",
                cardNumber = "1234 5678 9012 3456",
                color = "#00FF00"
            ),
            Card(
                id = "3",
                store = "Apple",
                cardNumber = "1234 5678 9012 3456",
                color = "#0000FF"
            )
        ),
        onEditCard = {},
        onDeleteCard = {}
    )
}