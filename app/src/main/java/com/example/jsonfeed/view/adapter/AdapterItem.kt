package com.example.jsonfeed.view.adapter

data class AdapterItem<out T>(
    val value: T?,
    val viewType: Int
)