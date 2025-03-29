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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.pawcode.cardstore.R
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.EXAMPLE_CARD
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun CardsListComponent(
    cards: List<CardEntity>,
    isFiltered: Boolean,
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
                text = stringResource(
                    if (isFiltered) R.string.cards_list_empty_filtered else R.string.cards_list_empty
                ),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
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
@Preview(showBackground = true)
@Composable
fun PreviewCardsListComponent() {
    CardsListComponent(
        cards = listOf(
            EXAMPLE_CARD,
            EXAMPLE_CARD.copy(cardId = Uuid.random().toString()),
            EXAMPLE_CARD.copy(cardId = Uuid.random().toString())
        ),
        isFiltered = false,
        listState = rememberLazyGridState(),
        onCardClicked = {},
        onCardLongPressed = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCardsListComponentEmpty() {
    CardsListComponent(
        cards = listOf(),
        isFiltered = false,
        listState = rememberLazyGridState(),
        onCardClicked = {},
        onCardLongPressed = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCardsListComponentEmptyFiltered() {
    CardsListComponent(
        cards = listOf(),
        isFiltered = true,
        listState = rememberLazyGridState(),
        onCardClicked = {},
        onCardLongPressed = {}
    )
}
