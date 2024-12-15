package com.example.fierce.network

import com.example.fierce.model.Shoes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("shoes") // employee diganti users
    fun getAllShoes(): Call<List<Shoes>>

    // Update shoes status
    @POST("shoes/{id}")
    fun updateShoesStatus(@Path("id") id: String, @Field("status") status: String): Call<Shoes>

    // Add new shoes
    @POST("shoes")
    fun addShoes(@Body shoes: Shoes): Call<Shoes>

    // Delete shoes
    @DELETE("shoes/{id}")
    fun deleteShoes(@Path("id") id: String): Call<Void>

    // Update shoes
    @POST("shoes/{id}")
    fun updateShoes(@Path("id") id: String, @Body shoes: Shoes): Call<Shoes>





}