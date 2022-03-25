package com.example.jsonfeed.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jsonfeed.data.FavouriteRepository
import com.example.jsonfeed.data.model.FavouriteJoke
import com.example.jsonfeed.data.model.Joke
import com.example.jsonfeed.data.model.Type.DOUBLE
import com.example.jsonfeed.data.model.Type.SINGLE
import kotlinx.coroutines.launch

class JokeDetailsViewModel(private val repository: FavouriteRepository) : ViewModel(){
    val singleJoke = MutableLiveData<Joke>()
    val doubleJoke = MutableLiveData<Joke>()
    private var joke: Joke? = null

    fun setJoke(joke: Joke) {
        this.joke = joke
        when(joke.type){
            SINGLE -> singleJoke.postValue(joke)
            DOUBLE -> doubleJoke.postValue(joke)
        }
    }

    fun setFavourite(){
        viewModelScope.launch {
            joke?.let { joke ->
                if(joke.isFavourite){
                    repository.delete(joke.id)
                } else {
                    repository.insert(FavouriteJoke(joke.id, joke.category.name))
                }
                joke.isFavourite = !joke.isFavourite
                setJoke(joke)
            }
        }
    }
}