package com.example.saydaliyati

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(pharmacie::class),version = 1)

abstract class AppDatabase: RoomDatabase() {
    abstract fun getPlayerDao():pharmacieDao

}