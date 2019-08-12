package com.example.rofaida.saydaliyati.Views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.rofaida.saydaliyati.Interfaces.RetrofitService
import com.example.rofaida.saydaliyati.Models.Commande
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import com.bumptech.glide.request.RequestOptions
import com.example.rofaida.saydaliyati.*
import com.example.rofaida.saydaliyati.Models.Commande_details
import com.example.rofaida.saydaliyati.Models.EtatCommande


class CommandeAdapter(
    private var commandes: ArrayList<Commande>,
    private var context: Context,
    private var fragmentManager_: FragmentManager
) : RecyclerView.Adapter<CommandeAdapter.DataHolder>()

{

    fun CommandeAdapter(commandes_: ArrayList<Commande>, context_: Context){
    this.commandes = commandes_
    this.context = context_
}

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_commande_layout, parent, false)
    return DataHolder(view, context, commandes!!)
}

override fun onBindViewHolder(holder: DataHolder, position: Int) {
    val commande = commandes!![position]
    holder.ordonnance_title_.setText(commande.titre)
    holder.ordonnance_pharma_.text = commande.pharma_nom
    holder.ordonnance_status_.setText(commande.etat)

    if(commande.etat.equals(EtatCommande.pending.etat)) {
        holder.ordonnance_status_.setTextColor(this.context.resources.getColor(R.color.pending_blue))
        Glide.with(context).load(R.drawable.pending).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.status_img_)
    }
    else if (commande.etat.equals(EtatCommande.accepted.etat)) {
        holder.ordonnance_status_.setTextColor(this.context.resources.getColor(R.color.accepted_green))
        Glide.with(context).load(R.drawable.success).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.status_img_)
    }
    else if (commande.etat.equals(EtatCommande.refused.etat)) {
        holder.ordonnance_status_.setTextColor(this.context.resources.getColor(R.color.refused_red))
        Glide.with(context).load(R.drawable.error).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.status_img_)
    }

    val options = RequestOptions()
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher_round)
        .error(R.mipmap.ic_launcher_round)

    options.fitCenter()

    Glide.with(context).load("http://192.168.1.4:8082/uploads/"+commande.photo).apply(options).into(holder.ordonnance_photo_)
}

override fun getItemCount(): Int {
    return commandes!!.size
}

inner class DataHolder(itemView: View, var context: Context, var commandes: ArrayList<Commande>) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var ordonnance_photo_: ImageView
    var ordonnance_title_: TextView
    var ordonnance_pharma_: TextView
    var ordonnance_status_: TextView
    var status_img_ : ImageView

    init {
        itemView.setOnClickListener(this)
        ordonnance_photo_ = itemView.findViewById(R.id.ordonnance_photo) as ImageView
        ordonnance_title_ = itemView.findViewById(R.id.commande_titre)
        ordonnance_pharma_ = itemView.findViewById(R.id.pharmacie)
        ordonnance_status_ = itemView.findViewById(R.id.status_commande)
        status_img_ = itemView.findViewById(R.id.status_image)
    }

    override fun onClick(view: View) {
        val commande = this.commandes[adapterPosition]
        val commande_:Commande_details = Commande_details(
            commande.id,
            commande.titre,
            commande.etat,
            commande.photo,
            commande.idclient,
            commande.idpharma, commande.pharma_nom,commande.idfacture,""
        )
        Toast.makeText(context, commande.titre, Toast.LENGTH_SHORT).show()
        //Show commande details
        var bundle: Bundle = Bundle()
        bundle.putSerializable("commande", commande_)
        if(commande.etat.equals(EtatCommande.pending.etat))
        {
            val fragment_new: Fragment = Commande_pending_details()
            fragment_new.arguments = bundle
            fragmentManager_!!.beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                .replace(
                    R.id.frameContainer,
                    fragment_new,
                    Utils.Commande_pending_details
                ).commit()
        }

        else if (commande.etat.equals(EtatCommande.accepted.etat))
        {
            val fragment_new: Fragment = Commande_accepted_details()
            fragment_new.arguments = bundle
            fragmentManager_!!.beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                .replace(
                    R.id.frameContainer,
                    fragment_new,
                    Utils.Commande_accepted_details
                ).commit()
        }

        else if (commande.etat.equals(EtatCommande.refused.etat))
        {
            val fragment_new: Fragment = Commande_refused_details()
            fragment_new.arguments = bundle
            fragmentManager_!!.beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                .replace(
                    R.id.frameContainer,
                    fragment_new,
                    Utils.Commande_refused_details
                ).commit()
        }
    }

}

    /********************************* Get Images ********************************************/

    fun getRetrofitImage(url:String, path:String):String {
        var FileDownloaded = ""
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
                    FileDownloaded = DownloadImage(response.body()!!, path)
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

        return FileDownloaded
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