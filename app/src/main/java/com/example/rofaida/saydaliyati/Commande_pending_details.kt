package com.example.rofaida.saydaliyati


import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rofaida.saydaliyati.Models.Commande_details
import com.squareup.picasso.Picasso
import android.content.Intent




class Commande_pending_details : Fragment(), View.OnClickListener {

    private lateinit var image_id_: TextView
    private lateinit var pharma_name_: TextView
    private lateinit var pharma_adr_: TextView
    private lateinit var commande_etat_: TextView
    private lateinit var date_commande_: TextView
    private lateinit var img_commande_: ImageView
    private lateinit var zoom_img_: ImageView

    private lateinit var view1:View

    private var commande:Commande_details? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_commande_pending_details, container, false)
        commande = arguments!!.getSerializable("commande") as Commande_details
        initViews()
        return view1
    }

    private fun initViews() {
        image_id_ = view1.findViewById<View>(R.id.image_id) as TextView
        pharma_name_ = view1.findViewById(R.id.pharma_name) as TextView
        pharma_adr_ = view1.findViewById(R.id.pharma_adr) as TextView
        commande_etat_ = view1.findViewById(R.id.commande_etat) as TextView
        date_commande_ = view1.findViewById(R.id.date_commande) as TextView
        img_commande_ = view1.findViewById(R.id.commande_image) as ImageView
        zoom_img_ = view1.findViewById(R.id.zoom_img) as ImageView

        //Init valeurs
        image_id_.setText(commande!!.photo)
        pharma_name_.setText(commande!!.pharma_nom)
        pharma_adr_.setText("Adresse Pharmacie")
        commande_etat_.setText(commande!!.etat)
        date_commande_.setText("Date Now")
        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)

        options.fitCenter()

        Glide.with(this@Commande_pending_details.context!!).load("http://192.168.1.4:8082/uploads/"+commande!!.photo).apply(options).into(img_commande_)

        zoom_img_.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.zoom_img ->
                display_ordonnance()
        }
    }

    private fun display_ordonnance() {
        val url = "http://192.168.1.4:8082/uploads/"+commande!!.photo
        val intent: Intent = Intent(getActivity(), Photo_viewer_activity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }

}
