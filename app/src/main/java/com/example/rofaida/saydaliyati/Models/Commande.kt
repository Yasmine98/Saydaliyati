package com.example.rofaida.saydaliyati.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date


@Entity(tableName ="commande" )
data class Commande(
    @PrimaryKey
    var id:Int,
    var titre:String,
    var etat:String,
    var photo:String,
    var idclient:Int,
    var idpharma:Int,
    var pharma_nom:String,
    var idfacture: Int?,
    var motifRefus:String,
    var date_commande: String
)