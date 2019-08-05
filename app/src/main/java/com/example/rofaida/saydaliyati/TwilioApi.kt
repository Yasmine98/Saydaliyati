package com.example.rofaida.saydaliyati

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface TwilioApi
{
    @FormUrlEncoded
    @POST("Accounts/{ACCOUNT_SID}/SMS/Messages")
    fun sendMessage(
        @Path("ACCOUNT_SID") accountSId: String,
        @Header("Authorization") signature: String,
        @FieldMap smsdata: Map<String, String>
    ): Call<ResponseBody>

}