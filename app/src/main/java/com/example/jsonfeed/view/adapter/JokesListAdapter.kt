package com.example.jsonfeed.view.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonfeed.R
import com.example.jsonfeed.data.model.Category
import com.example.jsonfeed.data.model.Joke
import com.example.jsonfeed.data.model.Type
import com.example.jsonfeed.databinding.HeaderGenericBinding
import com.example.jsonfeed.databinding.ItemDoubleJokeBinding
import com.example.jsonfeed.databinding.ItemLoadingBinding
import com.example.jsonfeed.databinding.ItemSingleJokeBinding
import com.example.jsonfeed.view.listener.ItemClickListener

class JokesListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_SINGLE_JOKE = 100
        const val TYPE_DOUBLE_JOKE = 101
        const val TYPE_HEADER = 102
        const val TYPE_PROGRESS_BAR = 103
    }

    private val jokes = ArrayList<AdapterItem<Joke>>()
    private val expandedJokes = ArrayList<Int>()
    var itemClickListener: ItemClickListener<Joke>? = null
    var favouriteClickListener: ItemClickListener<Joke>? = null

    fun clearJokesList(){
        jokes.clear()

    }

    fun addJokesToList(
        newJokesList: List<Joke>
    ) {

        if(jokes.size>0){
            jokes.removeLast()
        } else {
            jokes.add(0,
                AdapterItem(
                    null,
                    TYPE_HEADER
                )
            )
        }

        jokes.addAll(
            newJokesList.filter{ newJoke->
                jokes.firstOrNull{ item ->
                    item.value?.id == newJoke.id
                } == null && newJoke.safe
            }.map {
                AdapterItem(
                    it,
                    when(it.type){
                        Type.SINGLE -> TYPE_SINGLE_JOKE
                        Type.DOUBLE -> TYPE_DOUBLE_JOKE
                    }
                )
            }
        )
        jokes.add(
            AdapterItem(
                null,
                TYPE_PROGRESS_BAR
            )
        )
        notifyDataSetChanged()
    }

    fun removeAtPosition(index: Int) {
        jokes.removeAt(index)
        notifyDataSetChanged()
    }

    override fun getItemCount() = jokes.size

    override fun getItemViewType(position: Int) = jokes[position].viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(
                DataBindingUtil.inflate(inflater, R.layout.header_generic, parent, false)
            )
            TYPE_SINGLE_JOKE -> ItemSingleJokeViewHolder(
                DataBindingUtil.inflate(inflater, R.layout.item_single_joke, parent, false)
            )
            TYPE_DOUBLE_JOKE -> ItemDoubleJokeViewHolder(
                DataBindingUtil.inflate(inflater, R.layout.item_double_joke, parent, false)
            )
            TYPE_PROGRESS_BAR -> ItemLoadingViewHolder(
                DataBindingUtil.inflate(inflater, R.layout.item_loading, parent, false)
            )
            else -> throw RuntimeException("Invalid view type! $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = jokes[position]
        when (holder) {
            is HeaderViewHolder -> {
                holder.view.header = holder.view.root.context.getString(R.string.jokes_header)
            }
            is ItemSingleJokeViewHolder -> {
                item.value?.let { joke ->
                    holder.view.joke = joke
                    holder.view.apply {
                        categoryImageView.setImageDrawable(getCategoryDrawable(holder.view.root.context, joke.category))

                        favouriteLayout.setOnClickListener {
                            jokes[position].value?.isFavourite = !joke.isFavourite
                            favouriteClickListener?.onItemClicked(joke)
                            notifyItemChanged(position)
                        }

                        root.setOnClickListener {
                            itemClickListener?.onItemClicked(joke)
                        }
                    }
                }
            }
            is ItemDoubleJokeViewHolder -> {
                item.value?.let { joke ->
                    holder.view.joke = joke
                    holder.view.apply {
                        categoryImageView.setImageDrawable(getCategoryDrawable(holder.view.root.context, joke.category))
                        if(expandedJokes.find { id ->
                                id == joke.id
                            } != null){
                            this.deliveryTextView.visibility = View.VISIBLE
                            expandButton.setImageDrawable(AppCompatResources.getDrawable(holder.view.root.context, R.drawable.ic_baseline_expand_less_24))
                        } else {
                            this.deliveryTextView.visibility = View.GONE
                            expandButton.setImageDrawable(AppCompatResources.getDrawable(holder.view.root.context, R.drawable.ic_baseline_expand_more_24))
                        }
                        expandButton.setOnClickListener {
                            if(expandedJokes.find { id ->
                                id == joke.id
                            } != null){
                                expandedJokes.remove(joke.id)
                                this.deliveryTextView.visibility = View.GONE
                                expandButton.setImageDrawable(AppCompatResources.getDrawable(holder.view.root.context, R.drawable.ic_baseline_expand_more_24))
                            } else {
                                expandedJokes.add(joke.id)
                                this.deliveryTextView.visibility = View.VISIBLE
                                expandButton.setImageDrawable(AppCompatResources.getDrawable(holder.view.root.context, R.drawable.ic_baseline_expand_less_24))
                            }
                        }

                        favouriteLayout.setOnClickListener {
                            jokes[position].value?.isFavourite = !joke.isFavourite
                            favouriteClickListener?.onItemClicked(joke)
                            notifyItemChanged(position)
                        }

                        root.setOnClickListener {
                            itemClickListener?.onItemClicked(joke)
                        }
                    }
                }
            }
            is ItemLoadingViewHolder -> {
            }
        }
    }

    private fun getCategoryDrawable(context: Context, category: Category): Drawable?{
        return when(category){
            Category.PROGRAMMING -> AppCompatResources.getDrawable(context, R.drawable.ic_custom_coding_programming_development_svgrepo_com)
            Category.CHRISTMAS -> AppCompatResources.getDrawable(context, R.drawable.ic_christmas_tree_svgrepo_com)
            Category.SPOOKY -> AppCompatResources.getDrawable(context, R.drawable.ic_ghost_svgrepo_com)
            Category.MISC -> AppCompatResources.getDrawable(context, R.drawable.ic_earth_svgrepo_com)
            Category.PUN -> AppCompatResources.getDrawable(context, R.drawable.ic_plug_svgrepo_com)
            Category.DARK -> AppCompatResources.getDrawable(context, R.drawable.ic_forbidden_svgrepo_com)
        }
    }

    class HeaderViewHolder(var view: HeaderGenericBinding, override var canSwipe: Boolean = false) : BaseViewHolder(view)
    class ItemSingleJokeViewHolder(var view: ItemSingleJokeBinding,
                                   override var canSwipe: Boolean = true
    ) : BaseViewHolder(view)
    class ItemDoubleJokeViewHolder(var view: ItemDoubleJokeBinding,
                                   override var canSwipe: Boolean = true
    ) : BaseViewHolder(view)
    class ItemLoadingViewHolder(var view: ItemLoadingBinding) : BaseViewHolder(view){
        override var canSwipe = false
    }
    abstract class BaseViewHolder(view: ViewDataBinding):RecyclerView.ViewHolder(view.root){
        abstract var canSwipe: Boolean
    }
}