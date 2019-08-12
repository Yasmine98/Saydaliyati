package com.example.rofaida.saydaliyati.Models

import java.io.Serializable

class Commande_details(
    var id:Int,
    var titre:String,
    var etat:String,
    var photo:String,
    var idclient:Int,
    var idpharma:Int,
    var pharma_nom:String,
    var idfacture: Int?,
    var motifRefus:String
) : Serializable {}