package com.example.rofaida.saydaliyati.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="commande" )
data class Commande (
    @PrimaryKey
    var id:Int,
    var etat:String,
    var photo:String,
    var idclient:String,
    var idpharma:String,
    var idfacture:String
)