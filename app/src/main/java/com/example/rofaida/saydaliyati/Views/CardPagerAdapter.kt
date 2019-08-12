package com.example.rofaida.saydaliyati.Views

import com.example.rofaida.saydaliyati.R
import android.widget.TextView
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.rofaida.saydaliyati.Views.CardAdapter.Companion.MAX_ELEVATION_FACTOR


class CardPagerAdapter() : PagerAdapter(), CardAdapter {


    override fun getCount(): Int {
        return mData.size
    }

    override var baseElevation: Float = 0.0f


    private var mViews: MutableList<CardView?>
    private var mData: MutableList<CardItem>


    init {
        mData = ArrayList()
        mViews = ArrayList()
    }

    fun addCardItem(item: CardItem) {
        mViews.add(null)
        mData.add(item)
    }

    override fun getCardViewAt(position: Int): CardView {
        return mViews[position]!!
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.adapter, container, false)
        container.addView(view)
        bind(mData[position], view)
        val cardView = view.findViewById(R.id.cardView) as CardView

        if (baseElevation == 0f) {
            baseElevation = cardView.cardElevation
        }

        cardView.maxCardElevation = baseElevation * MAX_ELEVATION_FACTOR
        mViews[position] = cardView
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        mViews.set(position, null)
    }

    private fun bind(item: CardItem, view: View) {
        val title = view.findViewById(R.id.titleMedicament) as TextView
        val famille = view.findViewById(R.id.familleMedicament) as TextView
        val prix = view.findViewById(R.id.prixMedicament) as TextView
        val img = view.findViewById(R.id.medicament_img) as ImageView
        //titleTextView.setText(item.getTitle())
        //contentTextView.setText(item.getText())
    }

}