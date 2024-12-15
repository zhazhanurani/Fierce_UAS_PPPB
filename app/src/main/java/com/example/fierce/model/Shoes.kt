package com.example.fierce.model

import com.google.gson.annotations.SerializedName

data class Shoes(
    @SerializedName("_id")
    val id : String? = null,

    @SerializedName("brand")
    val brand : String,

    @SerializedName("price")
    val price : Double,

    @SerializedName("size")
    val size : Float,

    @SerializedName("detail")
    val detail : String,

    @SerializedName("image")
    val image : String,

    @SerializedName("status")
    val status : String
)
