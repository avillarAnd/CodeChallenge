package com.example.jsonfeed.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.jsonfeed.data.FavouriteRepository
import com.example.jsonfeed.data.api.jokeapi.JokeapiApiService
import com.example.jsonfeed.data.api.jokeapi.response.JokeapiApiResponse
import com.example.jsonfeed.data.model.FavouriteJoke
import com.example.jsonfeed.data.model.Joke
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class HomeViewModel (private val repository: FavouriteRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val jokesList = MutableLiveData<List<Joke>>()
    val errorFetchingJokes = MutableLiveData<Boolean>()

    init {
        fetchJokes()
    }
    fun refreshJokes(){
        fetchJokes()
    }

    fun setFavourite(joke: Joke){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if(repository.getJokeById(joke.id) == null){
                    repository.insert(FavouriteJoke(joke.id, joke.category.name))
                } else {
                    repository.delete(joke.id)
                }
            }
        }
    }

    private fun fetchJokes(){
        disposable.add(
            JokeapiApiService.getJokes()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<JokeapiApiResponse>(){
                    override fun onSuccess(t: JokeapiApiResponse) {
                        runBlocking {
                            t.jokes.forEach {
                                it.isFavourite = repository.getJokeById(it.id) != null
                            }
                            jokesList.postValue(t.jokes)
                            errorFetchingJokes.postValue(false)
                        }
                    }

                    override fun onError(e: Throwable) {
                        errorFetchingJokes.postValue(true)
                    }

                })
        )
    }

}

class ViewModelFactory(private val repository: FavouriteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository ) as T
        } else if (modelClass.isAssignableFrom(JokeDetailsViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return JokeDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}