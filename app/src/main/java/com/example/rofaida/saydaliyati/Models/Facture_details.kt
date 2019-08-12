package com.example.rofaida.saydaliyati.Models

import java.io.Serializable

class Facture_details(
    var id:Int,
    var montant:String,
    var charges:String,
    var ic_cmd:String
) : Serializable {}