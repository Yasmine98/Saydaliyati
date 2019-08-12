package com.example.rofaida.saydaliyati.Interfaces

import com.example.rofaida.saydaliyati.Models.BrainTreeToken
import com.example.rofaida.saydaliyati.Models.BraintreeTransaction
import com.example.rofaida.saydaliyati.Models.Payement_credentials
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface IBrainTree
{
    @GET("checkouts/new")
    fun getToken(): Observable<BrainTreeToken>

    @POST("checkouts")
    @FormUrlEncoded
    fun submitPayement(@Field("amount") amount:String, @Field("payment_method_nonce") nonce:String): Observable<BraintreeTransaction>
}