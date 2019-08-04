package com.example.rofaida.saydaliyati

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST



public interface TokenServerApi {
    @POST("/verify/token")
    @FormUrlEncoded
    fun getToken(@Field("phone_number") phoneNumber: String): Call<TokenServerResponse>

}