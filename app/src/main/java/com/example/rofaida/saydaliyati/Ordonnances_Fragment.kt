package com.example.rofaida.saydaliyati


import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rofaida.saydaliyati.Interfaces.RetrofitService
import com.example.rofaida.saydaliyati.Models.Commande
import com.example.rofaida.saydaliyati.Views.CommandeAdapter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class Ordonnances_Fragment : Fragment() {

    private var commandes: ArrayList<Commande>? = null
    private var show_commandes: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var commandeAdapter: CommandeAdapter? = null
    private lateinit var view1:View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_ordonnances, container, false)
        initViews()
        //setListeners()
        return view1
    }

    private fun initViews() {
        commandes = ArrayList()
        show_commandes = view1.findViewById(R.id.list_ordonnances) as RecyclerView
        var linearLayoutManager:LinearLayoutManager = LinearLayoutManager(this.context!!)
        show_commandes!!.layoutManager = linearLayoutManager
        getCommandes()
    }



    // getting all the orders
    private fun getCommandes() {
        val call = RetrofitService.endpoint.getCommandes()
        call.enqueue(object : Callback<ArrayList<Commande>>
        {
            override fun onFailure(call: Call<ArrayList<Commande>>, t: Throwable) {
                Toast.makeText(this@Ordonnances_Fragment.context, "Commande ajoutée avec succès", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<ArrayList<Commande>>, response: Response<ArrayList<Commande>>) {
                if (response?.isSuccessful!!){
                    for(i in response.body()!!)
                    {
                        commandes!!.add(i)
                    }
                    commandeAdapter = CommandeAdapter(commandes!!, this@Ordonnances_Fragment.context!!, this@Ordonnances_Fragment.fragmentManager!!
                    )
                    show_commandes!!.adapter = commandeAdapter
                    Toast.makeText(this@Ordonnances_Fragment.context, "Loaded :"+commandes!!.get(0).etat, Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this@Ordonnances_Fragment.context, "Erreur", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    /********************************* Get Images ********************************************/

    fun getRetrofitImage(url:String, path:String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val call = RetrofitService.endpoint.getImageDetails(path)
        call.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                try
                {
                    Log.d("onResponse", "Response came from server")
                    val FileDownloaded = DownloadImage(response.body()!!, path)
                    Log.d("onResponse", "Image is downloaded and saved ? " + FileDownloaded)
                }
                catch (e:Exception) {
                    Log.d("onResponse", "There is an error")
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("onFailure", t.toString())
            }

        })
    }

    private fun DownloadImage(body: ResponseBody, path:String):String {
        try
        {
            Log.d("DownloadImage", "Reading and writing file")
            var `in`: InputStream? = null
            var out: FileOutputStream? = null
            try
            {
                `in` = body.byteStream()
                out = FileOutputStream(this.context!!.getExternalFilesDir("").toString()+ File.separator + path)
                var c:Int
                c = `in`.read()
                while (c != -1)
                {
                    out!!.write(c)
                    c = `in`.read()
                }
            }
            catch (e: IOException) {
                Log.d("DownloadImage", e.toString())
                return ""
            }
            finally
            {
                if (`in` != null)
                {
                    `in`.close()
                }
                if (out != null)
                {
                    out.close()
                }
            }
            //val width:Int
            //val height:Int
            //val image = findViewById(R.id.IdProf) as ImageView
            //val bMap = BitmapFactory.decodeFile(this.context!!.getExternalFilesDir("").toString() + File.separator + path)
            //image.setImageBitmap(bMap)
            return this.context!!.getExternalFilesDir("").toString() + File.separator + path
        }
        catch (e: IOException) {
            Log.d("DownloadImage", e.toString())
            return ""
        }
    }

}
