package com.example.rofaida.saydaliyati

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Operation.State.SUCCESS
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker
import androidx.work.impl.utils.futures.SettableFuture
import com.example.rofaida.saydaliyati.Interfaces.RetrofitService
import com.google.common.util.concurrent.ListenableFuture
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("RestrictedApi")
class SynchroWorker
/**
 * @param appContext   The application [Context]
 * @param workerParams Parameters to setup the internal state of this worker
 */
    (appContext: Context, workerParams: WorkerParameters) : ListenableWorker(appContext, workerParams) {

    private val ctx = appContext

    override fun startWork(): ListenableFuture<ListenableWorker.Result> {
        val future = SettableFuture.create<Result>()
      //  val future = SettableFuture.create()
        //                future.set(Payload(ListenableWorker.Result.SUCCESS))
        val list:List<pharmacie> = AppDatabase.getInstance(ctx).getPharmaDao().getpharma()
        val i : Int = 0
     var list2:List<pharmacie> = listOf(pharmacie(0,"","","","","","","","", 0.0, 0.0))
        val call = RetrofitService.endpoint.getPharma()
        call.enqueue(object: Callback<List<pharmacie>> {
            override fun onResponse(
                call: Call<List<pharmacie>>?, response:
                Response<List<pharmacie>>?
            ) {
                if (response?.isSuccessful!!) {
                     list2 = response.body()!!
                }
            }
            override fun onFailure(call: Call<List<pharmacie>>?, t: Throwable?) {
                Log.e("erreur Synchro Worker", t. toString())
            }
        })
        if(list.size > 0 && list2.size>0){
            for ( p in list)
            {
                if (!list2.any { x -> x.id == p.id })  AppDatabase.getInstance(ctx).getPharmaDao().deletepharma(p)
                Log.e("erreur Synchro Worker", p.nom)
            }
        }
        future.set(Result.success())
        Log.e("erreur Synchro Worker", "worker")
        return future
    }

    companion object {
        private val TAG = "UploadWorker"
    }
}