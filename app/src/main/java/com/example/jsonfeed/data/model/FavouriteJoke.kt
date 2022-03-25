package com.example.jsonfeed.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_jokes_table")
data class FavouriteJoke (
        @PrimaryKey(autoGenerate = false)val id: Int,
        @ColumnInfo(name = "category")val category: String
)