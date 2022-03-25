package com.example.jsonfeed

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jsonfeed.data.api.jokeapi.JokeapiApiService
import com.example.jsonfeed.data.api.jokeapi.response.JokeapiApiResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.jsonfeed", appContext.packageName)
    }


    val disposable = CompositeDisposable()



    @Test
    fun testValidCache() {
        disposable.add(
            JokeapiApiService.getJokes()
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<JokeapiApiResponse>(){
                    override fun onSuccess(t: JokeapiApiResponse) {
                        print(t.jokes)
                    }

                    override fun onError(e: Throwable) {
                        throw e
                    }

                })
        )
    }
}