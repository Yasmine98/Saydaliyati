package com.example.saydaliyati

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    var mDataBase: AppDatabase? = null
    @Before
    fun initDB() {
        mDataBase =
            Room.inMemoryDatabaseBuilder(InstrumentationRegistry. getInstrumentation().context,AppDatabase::class.java).build()
    }
    @Test
    fun InsertAndGetPlayer() {
        /*   var id = R.id.id_team.text.toString()
           var name = R.id.id_team.text.toString()
           var continent = R.id.id_team.text.toString()*/
    /*    var team1 : pharmacie = pharmacie(id = )
        mDataBase?.getPlayerDao()?.addTeam(team1)
        val team2 =
            mDataBase?.getTeamDao()?.getTeamByName("hello")?.get(0)
        Assert.assertEquals(team1,team2)*/
    }
    @After
    fun closeDb(){
        mDataBase?.close()
    }
}
