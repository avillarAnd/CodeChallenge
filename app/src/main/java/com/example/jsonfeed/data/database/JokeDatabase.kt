package com.example.jsonfeed.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.jsonfeed.data.FavouriteJokeDao
import com.example.jsonfeed.data.model.FavouriteJoke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [FavouriteJoke::class], version = 1, exportSchema = false)
public abstract class JokeDatabase : RoomDatabase() {
    abstract fun favJokeDao(): FavouriteJokeDao

    private class JokeDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.favJokeDao())
                }
            }
        }

        suspend fun populateDatabase(favouriteJokeDao: FavouriteJokeDao) {


        }
    }

    companion object {

        @Volatile
        private var INSTANCE: JokeDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): JokeDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JokeDatabase::class.java,
                    "jokes_database"
                ).addCallback(JokeDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}