package com.example.rofaida.saydaliyati


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.example.rofaida.saydaliyati.Interfaces.RetrofitService
import com.example.rofaida.saydaliyati.Models.Commande_details
import com.example.rofaida.saydaliyati.Models.Facture
import com.example.rofaida.saydaliyati.Views.CardPagerAdapter
import com.example.rofaida.saydaliyati.Views.ShadowTransformer
import com.example.rofaida.saydaliyati.Views.CardItem
import kotlinx.android.synthetic.main.fragment_commande_accepted_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Commande_accepted_details : Fragment(), View.OnClickListener {

    private lateinit var mViewPager: ViewPager

    private lateinit var mCardAdapter: CardPagerAdapter
    private lateinit var mCardShadowTransformer: ShadowTransformer
    private lateinit var mFragmentCardShadowTransformer: ShadowTransformer

    private lateinit var prix_medicaments_: TextView
    private lateinit var prix_refus_: TextView
    private lateinit var prix_total_: TextView
    private lateinit var paymentBtn_: Button

    private lateinit var view1:View

    private var facture: Facture? = null
    private var commande: Commande_details? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_commande_accepted_details, container, false)
        commande = arguments!!.getSerializable("commande") as Commande_details
        initViews()
        return view1
    }

    private fun initViews() {

        mViewPager = view1.findViewById(R.id.viewPager) as ViewPager
        mCardAdapter = CardPagerAdapter()
        mCardAdapter.addCardItem(CardItem("medicament1", "famille du medicament1", "150.00 DA", R.drawable.sample1))
        mCardAdapter.addCardItem(CardItem("medicament2", "famille du medicament2", "150.00 DA", R.drawable.sample2))
        mCardAdapter.addCardItem(CardItem("medicament3", "famille du medicament3", "150.00 DA", R.drawable.sample3))

        mCardShadowTransformer = ShadowTransformer(mViewPager, mCardAdapter)

        mViewPager.adapter = mCardAdapter
        mViewPager.setPageTransformer(false, mCardShadowTransformer)
        mViewPager.offscreenPageLimit = 3

        mViewPager.setAdapter(mCardAdapter)
        mViewPager.setPageTransformer(false, mCardShadowTransformer)

        prix_medicaments_ = view1.findViewById(R.id.prix_medicaments) as TextView
        prix_refus_ = view1.findViewById(R.id.prix_refus) as TextView
        prix_total_ = view1.findViewById(R.id.prix_total) as TextView

        paymentBtn_ = view1.findViewById(R.id.PayementBTN17) as Button

        paymentBtn_.setOnClickListener(this)

        //Init valeurs
        val call = RetrofitService.endpoint.getFacture(commande!!.id)
        call.enqueue(object : Callback<List<Facture>> {
            override fun onFailure(call: Call<List<Facture>>, t: Throwable) {
                Toast.makeText(this@Commande_accepted_details.context, "Failure", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Facture>>, response: Response<List<Facture>>) {
                if (response.isSuccessful){
                    facture = response.body()!![0]
                    prix_medicaments_.setText(facture!!.montant.toString() + " DA")
                    prix_refus_.setText(facture!!.charges.toString() + " DA")
                    prix_total_.setText("Total facture : " + (facture!!.montant + facture!!.charges).toString() + " DA")
                }
                else
                {
                    Toast.makeText(this@Commande_accepted_details.context, "Erreur, "+response.message(), Toast.LENGTH_SHORT).show()
                }

            }

        })

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.PayementBTN17->
            {
                val intent: Intent = Intent(activity, Payement_Activity::class.java)
                startActivity(intent)
            }
        }
    }


}
