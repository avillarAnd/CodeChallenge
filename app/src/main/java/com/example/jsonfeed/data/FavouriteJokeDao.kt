package com.example.jsonfeed.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jsonfeed.data.model.FavouriteJoke
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteJokeDao {
    @Query("SELECT * FROM favourite_jokes_table ORDER BY id ASC")
    fun getAllJokes(): Flow<List<FavouriteJoke>>

    @Query("SELECT * FROM favourite_jokes_table WHERE id = :jokeId")
    fun getJokeById(jokeId: Int): FavouriteJoke?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(joke: FavouriteJoke)

    @Query("DELETE FROM favourite_jokes_table WHERE id = :jokeId")
    suspend fun deleteByJokeId(jokeId: Int)
}