package de.pawcode.cardstore.data.enums

enum class SortAttribute(val key: String) {
  ALPHABETICALLY("ALPHABETICALLY"),
  RECENTLY_USED("RECENTLY_USED"),
  MOST_USED("MOST_USED");

  companion object {
    fun fromKey(key: String?): SortAttribute {
      return entries.find { it.key == key } ?: ALPHABETICALLY
    }
  }
}
