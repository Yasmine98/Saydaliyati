package com.example.rofaida.saydaliyati.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="facture" )
data class Facture(
    @PrimaryKey
    var id:Int,
    var montant:Double,
    var charges:Double,
    var ic_cmd:Int
)