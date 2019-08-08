package com.example.rofaida.saydaliyati.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="client" )
data class Client (
    @PrimaryKey
    var nss:Int,
    var nom:String,
    var prenom:String,
    var adr:String,
    var tel:String,
    var mdp:String,
    var path_image:String
)