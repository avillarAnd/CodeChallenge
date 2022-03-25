package com.example.jsonfeed.data.api.jokeapi

import com.example.jsonfeed.data.api.jokeapi.response.JokeapiApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JokeapiApi {

    companion object {
        const val BASE_URL = "https://v2.jokeapi.dev/joke/"
    }

    @GET("{categories}")
    fun getJokes(
        @Path("categories") categories: String,
        @Query("lang") language: String,
        @Query("amount") amount: Int = 39
    ): Single<JokeapiApiResponse>


}