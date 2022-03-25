package com.example.jsonfeed.cache

import com.example.jsonfeed.data.model.JokeFlags
import com.example.jsonfeed.data.model.Joke
import com.example.jsonfeed.data.model.Type
import com.example.jsonfeed.view.App
import com.example.jsonfeed.viewmodel.HomeViewModel
import org.junit.Before
import org.junit.Test

class CacheTest {

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        val app = App.instance
        viewModel = HomeViewModel(app.repository)
    }

    @Test
    fun testInsertNewFavourite() {
        viewModel.insertFavourite(Joke("category", Type.DOUBLE, "", "", "", JokeFlags(false,false,false,false,false, false), false, 12, true))
    }
}