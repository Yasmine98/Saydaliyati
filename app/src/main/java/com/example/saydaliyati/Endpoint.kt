package com.example.saydaliyati

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Endpoint {
    // Call<List<Book>: une fonction callback qui retourne une liste
    @GET("/getpharma")
    fun getPharma(): Call<List<pharmacie>>
    // Envoi d'un paramètre name
    @GET("getpharmabyville/{ville}")
    fun pharma_vile(@Path("team") isbn:String):Call<List<pharmacie>>
    @POST("addpharma")
    fun addPharma(@Body pharmacie: pharmacie):Call<String>

}