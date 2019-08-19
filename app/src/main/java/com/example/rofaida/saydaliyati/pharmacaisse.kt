package com.example.rofaida.saydaliyati

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "pharmacaisse" , primaryKeys = ["idpharma", "caisse"])
data class pharmacaisse(
        val idpharma: Int, val caisse: String): Serializable