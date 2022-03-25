package com.example.jsonfeed.view.listener

fun interface ItemClickListener<T> {
    fun onItemClicked(item: T)
}