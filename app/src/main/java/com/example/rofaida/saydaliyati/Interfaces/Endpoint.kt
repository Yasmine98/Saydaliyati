package com.example.rofaida.saydaliyati.Interfaces

import com.example.rofaida.saydaliyati.Models.Client
import com.example.rofaida.saydaliyati.Models.Commande
import com.example.rofaida.saydaliyati.Models.User_details
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.PUT
import retrofit2.http.FormUrlEncoded





interface Endpoint {

    @POST("addUser")
    fun addUser(@Body client: Client):Call<String>

    /**** Image API ****/
    @Multipart
    @POST("/upload")
    fun postImage(@Part image: MultipartBody.Part, @Part("upload") name: RequestBody): Call<ResponseBody>

    @GET("uploads/{image}")
    fun getImageDetails(@Path("image") isbn:String): Call<ResponseBody>

    /**** Twilio API***/
    @FormUrlEncoded
    @POST("Accounts/{ACCOUNT_SID}/SMS/Messages")
    fun sendMessage(
        @Path("ACCOUNT_SID") accountSId: String,
        @Header("Authorization") signature: String,
        @FieldMap smsdata: Map<String, String>
    ): Call<ResponseBody>

    /**** Data API ****/

    @POST("addCommande")
    fun addCommande(@Body commande: Commande):Call<String>

    @PUT("updateUserPswd")
    fun updateUserPswd(@Body client: User_details): Call<String>

    @GET("getUserLogin/{user}")
    fun getUserLogin(@Path("user") isbn:User_details):Call<List<User_details>>

}