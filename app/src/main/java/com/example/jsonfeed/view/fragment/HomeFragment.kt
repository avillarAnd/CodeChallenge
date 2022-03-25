package com.example.jsonfeed.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonfeed.R
import com.example.jsonfeed.databinding.FragmentHomeBinding
import com.example.jsonfeed.view.App
import com.example.jsonfeed.view.adapter.JokesListAdapter
import com.example.jsonfeed.view.listener.ItemClickListener
import com.example.jsonfeed.viewmodel.HomeViewModel
import com.example.jsonfeed.viewmodel.ViewModelFactory


class HomeFragment : Fragment()  {

    private val homeViewModel: HomeViewModel by viewModels{
        ViewModelFactory((activity?.application as App).repository)
    }

    private var binding: FragmentHomeBinding? = null
    private val jokesListAdapter = JokesListAdapter()
    private var loading = true

    private var swipeListener = object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            jokesListAdapter.removeAtPosition(viewHolder.adapterPosition)
        }

        override fun getSwipeDirs(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return if((viewHolder as JokesListAdapter.BaseViewHolder).canSwipe){
                super.getSwipeDirs(recyclerView, viewHolder)
            } else {
                0
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            title = getString(R.string.app_name)
        }
        binding?.apply {
            jokesList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                jokesListAdapter.favouriteClickListener  = ItemClickListener {
                    homeViewModel.setFavourite(it)
                }
                jokesListAdapter.itemClickListener = ItemClickListener { joke ->
                    val action = HomeFragmentDirections.actionHomeFragmentToJokeDetailsFragment(joke)
                    Navigation.findNavController(requireView()).navigate(action)
                }
                adapter = jokesListAdapter
                itemAnimator = null
                addOnScrollListener(object : RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        (jokesList.layoutManager as LinearLayoutManager).apply {
                           val visibleItemCount = childCount
                           val totalItemCount = itemCount
                           val pastVisibleItems = findFirstVisibleItemPosition()
                            if (loading) {
                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                    loading = false
                                    homeViewModel.refreshJokes()
                                }
                            }
                        }
                    }
                })
                ItemTouchHelper(swipeListener).attachToRecyclerView(this)
                overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            }

            refreshLayout.setOnRefreshListener {
                jokesListAdapter.clearJokesList()
                homeViewModel.refreshJokes()
            }
        }
        observeViewModel()
    }

    private fun observeViewModel(){
        homeViewModel.jokesList.observe(
            viewLifecycleOwner
        ) { jokes ->
            jokesListAdapter.addJokesToList(jokes)
            binding?.refreshLayout?.isRefreshing = false
            loading = true
        }
        homeViewModel.errorFetchingJokes.observe(
            viewLifecycleOwner
        ) { showError ->
            binding?.apply{
                noJokesToShowLayout.visibility = if(showError) View.VISIBLE else View.GONE
                refreshLayout.isRefreshing = false
            }
        }
    }



}