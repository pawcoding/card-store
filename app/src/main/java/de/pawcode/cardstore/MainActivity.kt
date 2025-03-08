package de.pawcode.cardstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.room.Room
import de.pawcode.cardstore.data.AppDatabase
import de.pawcode.cardstore.data.model.CardViewModel
import de.pawcode.cardstore.data.model.CardViewModelFactory
import de.pawcode.cardstore.ui.pages.HomePage
import de.pawcode.cardstore.ui.theme.CardStoreTheme

class MainActivity : ComponentActivity() {
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "card-database"
        ).build()
    }

    private val cardViewModel: CardViewModel by viewModels {
        CardViewModelFactory(database.cardDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardStoreTheme {
                HomePage(cardViewModel)
            }
        }
    }
}
