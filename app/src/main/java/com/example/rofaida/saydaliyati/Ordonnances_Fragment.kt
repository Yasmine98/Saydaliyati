package com.example.rofaida.saydaliyati


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rofaida.saydaliyati.Models.Commande
import com.example.rofaida.saydaliyati.Views.CommandeAdapter


class Ordonnances_Fragment : Fragment() {

    private var commandes: ArrayList<Commande>? = null
    private var show_commandes: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var commandeAdapter: CommandeAdapter? = null
    private lateinit var view1:View

    private val titles = arrayOf(
        "PHP for the beginners",
        "iOS developer tutorial",
        "Learn Java for free",
        "Become Android programmer",
        "Python for data analysis",
        "PHP for the beginners",
        "iOS developer tutorial",
        "Learn Java for free"
    )
    private val authors = arrayOf(
        "Asif Khan",
        "Tanvir Ahmed",
        "Nafis Iqbal",
        "Rahim Islam",
        "Abir Hasan",
        "Asif Khan",
        "Tanvir Ahmed",
        "Nafis Iqbal"
    )
    private val photos = intArrayOf(
        R.drawable.sample_5,
        R.drawable.sample_1,
        R.drawable.sample_6,
        R.drawable.sample_5,
        R.drawable.sample_5,
        R.drawable.sample_0,
        R.drawable.sample_2,
        R.drawable.sample_3
    )

    private val likes = intArrayOf(10, 20, 30, 40, 50, 60, 15, 22)

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
        commandeAdapter = CommandeAdapter(commandes!!, this.context!!)
        show_commandes!!.adapter = commandeAdapter
        show_commandes!!.layoutManager = linearLayoutManager
        getBlogs()
    }

    // getting all the blogs
    private fun getBlogs() {
        for (count in titles.indices) {
            commandes!!.add(
                Commande(
                    0,
                    titles[count],
                    "pending",
                    photos[count],
                    0,
                    authors[count],
                    ""
                )
            )
        }
    }

}
