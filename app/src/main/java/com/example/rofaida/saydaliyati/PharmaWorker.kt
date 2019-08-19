package com.example.rofaida.saydaliyati

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class PharmaWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    private val cont : Context = ctx
    override fun doWork(): Result {

        val id = inputData.getInt("ID", 0)
        val nom = inputData.getString("NOM")
        val adrpost = inputData.getString("ADRPOST")
        val ho = inputData.getString("HO")
        val hf = inputData.getString("HF")
        val tel = inputData.getString("TEL")
        val fb = inputData.getString("FB")
        val loc = inputData.getString("LOC")
        val ville = inputData.getString("VILLE")
        val longi = inputData.getDouble("LONGI", 0.0)
        val lat = inputData.getDouble("LAT", 0.0)
        val caisse = inputData.getStringArray("caisse")
        val list: List<pharmacie> = AppDatabase.getInstance(cont).getPharmaDao().getpharmaByid(id)
        if (list.size == 0) {
            Log.e("new", "dataaaa")
            AppDatabase.getInstance(cont).getPharmaDao().addpharma(pharmacie(id, nom!!, adrpost!!, ho!!, hf!!, tel!!, fb!!, loc!!, ville!!,  longi, lat ))
           if(caisse?.size !=0){
            for (i in 1..caisse!!.size)
            {
                AppDatabase.getInstance(cont).getPharmaCaisseDao().addpharmaC(pharmacaisse(id,caisse[i] ))
                Log.e("new", "dataaaa Caisse")
            }}
        }
        else {
            Log.e("old", "dataaaa")
            AppDatabase.getInstance(cont).getPharmaDao().updatepharma(pharmacie(id, nom!!, adrpost!!, ho!!, hf!!, tel!!, fb!!, loc!!, ville!!,  longi, lat ))
        }

        Log.e("database", "dataaaa")
        return Result.success()
          }
}