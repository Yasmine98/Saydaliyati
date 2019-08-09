package com.example.rofaida.saydaliyati.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="commande" )
data class Commande_ (
    @PrimaryKey
    var id:Int,
    var titre:String,
    var etat:String,
    var photo:Int,
    var idclient:Int,
    var idpharma:String,
    var idfacture:String
)