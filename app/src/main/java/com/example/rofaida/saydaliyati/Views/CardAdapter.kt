package com.example.rofaida.saydaliyati.Views

import androidx.cardview.widget.CardView


interface CardAdapter {

    var baseElevation: Float

    fun getCardViewAt(position: Int): CardView

    fun getCount(): Int

    companion object {

        val MAX_ELEVATION_FACTOR = 8
    }
}