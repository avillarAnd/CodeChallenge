package com.example.jsonfeed.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


data class Joke (
    val category: Category,
    val type: Type,
    val setup: String?,
    val delivery: String?,
    val joke: String?,
    val flags: JokeFlags,
    val safe: Boolean,
    val id: Int,
    var isFavourite: Boolean
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        Category.getCategory(parcel.readString()!!)!!,
        Type.getType(parcel.readString()!!)!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(JokeFlags::class.java.classLoader)!!,
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(category.name)
        parcel.writeString(type.name)
        parcel.writeString(setup)
        parcel.writeString(delivery)
        parcel.writeString(joke)
        parcel.writeParcelable(flags,p1)
        parcel.writeByte(if (safe) 1 else 0)
        parcel.writeInt(id)
        parcel.writeByte(if (isFavourite) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<Joke> {
        override fun createFromParcel(parcel: Parcel): Joke {
            return Joke(parcel)
        }

        override fun newArray(size: Int): Array<Joke?> {
            return arrayOfNulls(size)
        }
    }
}

enum class Type(name:String){

    @SerializedName("single")
    SINGLE("single"),

    @SerializedName("twopart")
    DOUBLE("twopart");

    companion object {
        fun getType(name: String): Type? {
            return values().find { it.name == name }
        }
    }
}

enum class Category(name:String){

    @SerializedName("Programming")
    PROGRAMMING("Programming"),

    @SerializedName("Misc")
    MISC("Misc"),

    @SerializedName("Dark")
    DARK("Dark"),

    @SerializedName("Spooky")
    SPOOKY("Spooky"),

    @SerializedName("Christmas")
    CHRISTMAS("Christmas"),

    @SerializedName("Pun")
    PUN("Pun");
    companion object {
        fun getCategory(name: String): Category? {
            return values().find { it.name == name }
        }
    }

}