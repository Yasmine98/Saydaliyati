package com.example.rofaida.saydaliyati.Views

import android.content.Context
import android.widget.Toast
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.rofaida.saydaliyati.Models.Commande
import com.example.rofaida.saydaliyati.R

class CommandeAdapter(
    private var commandes: ArrayList<Commande>,
    private var context: Context
) : RecyclerView.Adapter<CommandeAdapter.DataHolder>()

{

    fun CommandeAdapter(commandes_: ArrayList<Commande>, context_: Context){
    this.commandes = commandes_
    this.context = context_
}

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_blog_layout, parent, false)
    return DataHolder(view, context, commandes!!)
}

override fun onBindViewHolder(holder: DataHolder, position: Int) {
    val commande = commandes!![position]
    holder.ordonnance_title_.setText(commande.titre)
    holder.ordonnance_pharma_.text = "by : " + commande.idpharma
    holder.ordonnance_status_.setText(commande.etat)
    Glide.with(context).load(commande.photo).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ordonnance_photo_)
}

override fun getItemCount(): Int {
    return commandes!!.size
}

inner class DataHolder(itemView: View, var context: Context, var blogs: ArrayList<Commande>) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var ordonnance_photo_: ImageView
    var ordonnance_title_: TextView
    var ordonnance_pharma_: TextView
    var ordonnance_status_: TextView

    init {
        itemView.setOnClickListener(this)
        ordonnance_photo_ = itemView.findViewById(R.id.ordonnance_photo) as ImageView
        ordonnance_title_ = itemView.findViewById(R.id.commande_titre)
        ordonnance_pharma_ = itemView.findViewById(R.id.pharmacie)
        ordonnance_status_ = itemView.findViewById(R.id.status_commande)
    }

    override fun onClick(view: View) {
        val commande = this.blogs[adapterPosition]
        Toast.makeText(context, commande.titre, Toast.LENGTH_SHORT).show()
    }

}

}