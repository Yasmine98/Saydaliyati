package com.example.saydaliyati

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(pharmacie::class, pharmacaisse::class),version = 1)

abstract class AppDatabase: RoomDatabase() {
    abstract fun getPharmaDao():pharmacieDao
    abstract fun getPharmaCaisseDao():pharmacaisseDao
    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "pharma")
                .allowMainThreadQueries().build()
    }
}