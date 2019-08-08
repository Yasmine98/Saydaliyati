package com.example.rofaida.saydaliyati.Models

import java.io.Serializable

class User_details(var nss:Int,
                   var nom:String,
                   var prenom:String,
                   var adr:String,
                   var tel:String,
                   var mdp:String,
                   var path_image:String
) : Serializable {}