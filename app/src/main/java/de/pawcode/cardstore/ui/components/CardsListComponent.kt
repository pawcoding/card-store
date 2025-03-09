package de.pawcode.cardstore.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.data.database.CardEntity
import de.pawcode.cardstore.data.database.EXAMPLE_CARD
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun CardsListComponent(
    cards: List<CardEntity>,
    onCardClicked: (CardEntity) -> Unit,
    onCardLongPressed: (CardEntity) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = cards, key = { card -> card.id }) { card ->
            CardComponent(card = card, onClick = {
                onCardClicked(card)
            }, onLongPress = {
                onCardLongPressed(card)
            })
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
        ), onCardClicked = {}, onCardLongPressed = {})
}
