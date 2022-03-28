package com.example.jsonfeed.data.api.jokeapi

import com.example.jsonfeed.data.api.jokeapi.JokeapiApi.Companion.BASE_URL
import com.example.jsonfeed.data.api.jokeapi.response.JokeapiApiResponse
import com.example.jsonfeed.utils.interceptors.CacheInterceptor
import com.example.jsonfeed.utils.interceptors.RewriteCacheInterceptor
import com.example.jsonfeed.App
import io.reactivex.Single
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object JokeapiApiService {
    private val api: JokeapiApi

    init {

        val cache = Cache(App.instance.applicationContext.cacheDir, 10 * 1024 * 1024L)

        var client = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(RewriteCacheInterceptor())
            .addInterceptor(CacheInterceptor())
            .build()


        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(JokeapiApi::class.java)
    }

    fun getJokes(): Single<JokeapiApiResponse> {
        return api.getJokes("any", "en", 10)
    }

}