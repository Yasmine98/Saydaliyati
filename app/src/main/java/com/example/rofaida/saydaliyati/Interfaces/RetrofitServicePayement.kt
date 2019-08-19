package com.example.rofaida.saydaliyati.Interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit


object RetrofitServicePayement {

    val ibrainTree :IBrainTree by lazy {

       /* val client = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS).build()*/
        Retrofit.Builder().baseUrl("http://192.168.1.6:8292/").
            addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
            addConverterFactory(GsonConverterFactory.create()).
            build().create(IBrainTree::class.java)
    }
}