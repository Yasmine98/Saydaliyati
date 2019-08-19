package com.example.rofaida.saydaliyati


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query

@Dao
interface pharmacaisseDao {
    @Query("select * from pharmacaisse where idpharma=:id")
    fun getcaisseBypharma(id : Int):List<pharmacaisse>
    @Insert
    fun addpharmaC(p:pharmacaisse)
    @Update
    fun updatepharmaC(p:pharmacaisse)
    @Delete
    fun deletepharmaC(p:pharmacaisse)

}
