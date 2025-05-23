package de.pawcode.cardstore.utils

import de.pawcode.cardstore.data.database.entities.CardEntity

const val oneHourInMillis = 60 * 60 * 1000L
const val oneDayInMillis = 24 * oneHourInMillis
const val oneWeekInMillis = 7 * oneDayInMillis
const val oneMonthInMillis = 30 * oneDayInMillis

fun calculateCardScore(card: CardEntity): Int {
  val now = System.currentTimeMillis()
  val timeDifference = now - (card.lastUsed ?: 0)

  val multiplier =
    when {
      timeDifference <= oneHourInMillis -> 5
      timeDifference <= oneDayInMillis -> 4
      timeDifference <= oneWeekInMillis -> 3
      timeDifference <= oneMonthInMillis -> 2
      else -> 1
    }

  return card.useCount * multiplier
}
