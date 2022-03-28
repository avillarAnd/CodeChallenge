package com.example.jsonfeed.data

import androidx.annotation.WorkerThread
import com.example.jsonfeed.data.model.FavouriteJoke

class FavouriteRepository (private val favouriteJokeDao: FavouriteJokeDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(joke: FavouriteJoke) {
        favouriteJokeDao.insert(joke)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(jokeId: Int) {
        favouriteJokeDao.deleteByJokeId(jokeId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getJokeById(id: Int): FavouriteJoke? {
        return favouriteJokeDao.getJokeById(id)
    }
}