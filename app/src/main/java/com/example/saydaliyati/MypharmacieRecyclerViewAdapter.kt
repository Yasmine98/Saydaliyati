package com.example.saydaliyati

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.saydaliyati.R


import com.example.saydaliyati.pharmacieFragment.OnListFragmentInteractionListener


import kotlinx.android.synthetic.main.fragment_pharmacie.view.*




/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MypharmacieRecyclerViewAdapter(
    private val mValues: List<pharmacie>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MypharmacieRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as pharmacie
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.

            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pharmacie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.id.toString()
        holder.mContentView.text = item.adrpost
        holder.mNom.text = item.nom
        holder.mHour.text = "Ouverte de "+item.ho + " à "+item.hf

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content
        val mNom : TextView = mView.nom
        val mImg : ImageView = mView.bar
        val mHour : TextView = mView.hour

        override fun toString(): String {
            return super.toString() + " '" + mNom.text + "'"
        }
    }
}
