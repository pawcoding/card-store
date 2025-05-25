package de.pawcode.cardstore.utils

import de.pawcode.cardstore.data.database.entities.CardEntity
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun calculateCardScore(card: CardEntity): Int {
  val now = System.currentTimeMillis()
  val timeDifference = (now - (card.lastUsed ?: 0)).toDuration(DurationUnit.MILLISECONDS)

  val multiplier =
    when {
      timeDifference <= 1.hours -> 5
      timeDifference <= 1.days -> 4
      timeDifference <= 7.days -> 3
      timeDifference <= 30.days -> 2
      else -> 1
    }

  return card.useCount * multiplier
}
