package de.pawcode.cardstore.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.pawcode.cardstore.data.database.daos.CardDao
import de.pawcode.cardstore.data.database.daos.LabelDao
import de.pawcode.cardstore.data.database.entities.CardEntity
import de.pawcode.cardstore.data.database.entities.CardLabelCrossRef
import de.pawcode.cardstore.data.database.entities.LabelEntity

@Database(
    entities = [
        CardEntity::class,
        LabelEntity::class,
        CardLabelCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun labelDao(): LabelDao

    companion object {
        @Volatile
        private var INSTANCE: CardDatabase? = null

        fun getDatabase(context: Context): CardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardDatabase::class.java,
                    "card_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}