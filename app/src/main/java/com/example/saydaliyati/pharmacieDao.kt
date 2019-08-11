package com.example.saydaliyati

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query

@Dao
interface pharmacieDao {
    @Query("select * from pharmacie where ville=:ville_name")
    fun getpharmaByville(ville_name:String):List<pharmacie>
    @Query("select * from pharmacie where id=:id")
    fun getpharmaByid(id:Int):List<pharmacie>
    @Query("select * from pharmacie")
    fun getpharma():List<pharmacie>
    @Insert
    fun addpharma(p:pharmacie)
    @Update
    fun updatepharma(p:pharmacie)
    @Delete
    fun deletepharma(p:pharmacie)
}
