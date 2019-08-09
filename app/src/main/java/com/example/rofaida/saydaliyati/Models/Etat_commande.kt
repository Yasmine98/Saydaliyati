package com.example.rofaida.saydaliyati.Models

enum class EtatCommande (val etat:String){
    pending("En cours de traitement"),
    refused("Refusée"),
    accepted("Acceptée")
}