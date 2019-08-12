package com.example.rofaida.saydaliyati


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import androidx.viewpager.widget.ViewPager
import com.example.rofaida.saydaliyati.Views.CardPagerAdapter
import com.example.rofaida.saydaliyati.Views.ShadowTransformer
import com.example.rofaida.saydaliyati.Views.CardItem
import android.widget.CheckBox




class Commande_accepted_details : Fragment(){

    private lateinit var mViewPager: ViewPager

    private lateinit var mCardAdapter: CardPagerAdapter
    private lateinit var mCardShadowTransformer: ShadowTransformer
    private lateinit var mFragmentCardShadowTransformer: ShadowTransformer

    private lateinit var view1:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_commande_accepted_details, container, false)
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

    }

}
