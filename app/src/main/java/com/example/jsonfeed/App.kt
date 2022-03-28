package com.example.jsonfeed

import android.app.Application
import com.example.jsonfeed.data.FavouriteRepository
import com.example.jsonfeed.data.database.JokeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { JokeDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { FavouriteRepository(database.favJokeDao()) }

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}