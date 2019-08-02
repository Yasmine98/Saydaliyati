package com.example.saydaliyati

import android.content.Context
import androidx.room.Room

object RoomService {

    lateinit var context: Context
    val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "pharma")
            .allowMainThreadQueries().build()

    }
}