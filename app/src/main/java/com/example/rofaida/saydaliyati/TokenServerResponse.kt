package com.example.rofaida.saydaliyati

import com.google.gson.annotations.SerializedName



public class TokenServerResponse
{
    @SerializedName("jwt_token")
    private var jwtToken: String? = null

    fun getJwtToken(): String? {
        return jwtToken
    }

    fun setJwtToken(jwtToken: String) {
        this.jwtToken = jwtToken
    }

}