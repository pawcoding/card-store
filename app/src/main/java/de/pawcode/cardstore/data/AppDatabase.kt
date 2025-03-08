package de.pawcode.cardstore.data

import androidx.room.Database
import androidx.room.RoomDatabase
import de.pawcode.cardstore.data.daos.CardDao
import de.pawcode.cardstore.data.entities.Card

@Database(entities = [Card::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}