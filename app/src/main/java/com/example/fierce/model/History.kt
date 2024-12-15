package com.example.fierce.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "history_table")
data class History(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id : Int = 0,

    @ColumnInfo("brand")
    val brand : String,

    @ColumnInfo("price")
    val price : Double,

    @ColumnInfo("size")
    val size : Float,

    @ColumnInfo("detail")
    val detail : String,

    @ColumnInfo("image")
    val image : String,

    @ColumnInfo("status")
    val status : String,

    @ColumnInfo("total_price")
    val totalPrice : Double,

    @ColumnInfo("shipping_cost")
    val shippingCost : Double,

    @ColumnInfo("address")
    val address : String,


)