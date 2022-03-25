package com.example.jsonfeed.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.jsonfeed.R
import com.example.jsonfeed.data.model.Joke
import com.example.jsonfeed.data.model.JokeFlags
import com.example.jsonfeed.databinding.FragmentJokeDetailsBinding
import com.example.jsonfeed.view.App
import com.example.jsonfeed.viewmodel.JokeDetailsViewModel
import com.example.jsonfeed.viewmodel.ViewModelFactory

class JokeDetailsFragment: Fragment() {

    private var binding: FragmentJokeDetailsBinding? = null
    private val jokeDetailsViewModel: JokeDetailsViewModel by viewModels {
        ViewModelFactory((activity?.application as App).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentJokeDetailsBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentJokeDetailsBinding.bind(view)
        arguments?.let { bundle ->
            if (!bundle.isEmpty) {
                val joke = JokeDetailsFragmentArgs.fromBundle(bundle).joke
                jokeDetailsViewModel.setJoke(joke)
                arguments?.clear()
            }
        }
        observeViewModel()
        setListeners()
    }

    private fun observeViewModel() {
        jokeDetailsViewModel.singleJoke.observe(viewLifecycleOwner) { joke ->
             setSingleJoke(joke)
        }
        jokeDetailsViewModel.doubleJoke.observe(viewLifecycleOwner) { joke ->
             setDoubleJoke(joke)
        }
    }
    private fun setListeners() {
        binding?.favouriteLayout?.setOnClickListener {
            jokeDetailsViewModel.setFavourite()
        }
    }

    private fun setSingleJoke(joke: Joke){
        setupToolbar(joke.category.name)
        binding?.apply {
            primaryTextView.text = joke.joke
        }
        setupFlags(joke.flags)
        setFavourite(joke.isFavourite)
    }
    private fun setDoubleJoke(joke: Joke){
        setupToolbar(joke.category.name)
        binding?.apply {
            primaryTextView.text = joke.setup
            extraTextView.text = joke.delivery
            extraTextView.visibility = View.VISIBLE
        }
        setupFlags(joke.flags)
        setFavourite(joke.isFavourite)
    }
    private fun setupToolbar(title: String){
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            this.title = title
        }
    }
    private fun setupFlags(jokeFlags: JokeFlags){
        binding?.apply {
            religiousFlag.isChecked = jokeFlags.religious
            politicalFlag.isChecked = jokeFlags.political
            explicitFlag.isChecked = jokeFlags.explicit
            racistFlag.isChecked = jokeFlags.racist
            nsfwFlag.isChecked = jokeFlags.nsfw
            sexistFlag.isChecked = jokeFlags.sexist
        }
    }
    private fun setFavourite(isFavourite: Boolean){
        binding?.apply {
            favouriteLayout.setImageDrawable(
                AppCompatResources.getDrawable( requireContext(),
                    if(isFavourite) R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                )
            )
        }
    }
}