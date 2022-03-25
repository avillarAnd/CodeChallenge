package com.example.jsonfeed.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class JokeFlags (
    @SerializedName("nsfw")
    val nsfw: Boolean,
    @SerializedName("religious")
    val religious: Boolean,
    @SerializedName("political")
    val political: Boolean,
    @SerializedName("racist")
    val racist: Boolean,
    @SerializedName("sexist")
    val sexist: Boolean,
    @SerializedName("explicit")
    val explicit: Boolean,
):Parcelable