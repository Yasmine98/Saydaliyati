package com.example.rofaida.saydaliyati


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "pharmacie" )
data class pharmacie(
    @PrimaryKey
    val id: Int,
    val nom: String, val adrpost: String, val ho: String, val hf: String, val tel: String, val fb: String, val loc: String, var ville:String, var longi: Double, var lat: Double): Serializable