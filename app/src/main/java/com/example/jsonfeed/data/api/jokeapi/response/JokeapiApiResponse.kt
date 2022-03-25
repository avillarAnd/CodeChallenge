package com.example.jsonfeed.data.api.jokeapi.response

import com.example.jsonfeed.data.model.Joke
import com.google.gson.annotations.SerializedName

data class JokeapiApiResponse (

    @SerializedName("error")
    val error: Boolean,

    @SerializedName("amount")
    val amount: Int,

    @SerializedName("jokes")
    val jokes: List<Joke>
)