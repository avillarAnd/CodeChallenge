package com.example.jsonfeed.data.model

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jsonfeed.R
import com.example.jsonfeed.data.api.jokeapi.JokeapiApiService
import com.example.jsonfeed.data.api.jokeapi.response.JokeapiApiResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}