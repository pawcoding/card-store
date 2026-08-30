package de.pawcode.cardstore.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val ADD_FAVORITE_COLUMN =
  object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
      db.execSQL("ALTER TABLE cards ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0")
    }
  }
