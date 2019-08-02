package com.example.saydaliyati

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.saydaliyati.R

data class CostumAdapter(val ctx: Context, val data:List<pharmacie>) : BaseAdapter()
{
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

      var view : View
       /* if(p1==null)
        {
            view = LayoutInflater.from(ctx).inflate(R.layout.mylayout, p2, false)
            Toast.makeText(ctx,"message",Toast.LENGTH_SHORT).show()


        }
        val textView1 = view?.findViewById(R.id.name) as TextView
        val textView2 = view?.findViewById(R.id.description) as TextView
        val textView3 = view?.findViewById(R.id.nbtourist) as TextView
        val imageView = view?.findViewById(R.id.image) as ImageView
        textView1.setText(data.get(p0).nom)
        textView2.setText(data.get(p0).description)
        textView3.setText((data.get(p0).nbMoyenTour).toString())
        imageView.setImageResource(data.get(p0).image)*/
        var holder : ViewHolder
        if(p1==null)
        {

            view = LayoutInflater.from(ctx).inflate(R.layout.fragment_pharmacie, p2, false)
            Toast.makeText(ctx,"message",Toast.LENGTH_SHORT).show()
            val textView1 = view?.findViewById(R.id.item_number) as TextView
            val textView2 = view?.findViewById(R.id.content) as TextView
         //   val textView3 = view?.findViewById(R.id.stateL) as TextView
         //   val imageView = view?.findViewById(R.id.image) as ImageView
            holder = ViewHolder(textView1,textView2)
            view.setTag(holder)

        }
        else
        {
            view = p1
            holder = view.tag as ViewHolder
        }

        holder.textView1.setText(data.get(p0).id)
        holder.textView2.setText(data.get(p0).adrpost)
        var etat : String
                etat = "fermé"
       // holder.textView3.setText(etat)
       // holder.imageView.setImageResource(data.get(p0).img)
     /*   holder.imageView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intenti = Intent(ctx, Main3Activity::class.java)

                intenti.putExtra("Pharma",getItem(p0))
                intenti.putExtra("id",getItem(p0).id)
                intenti.putExtra("adr",getItem(p0).adr)
                intenti.putExtra("tel",getItem(p0).tel)
                intenti.putExtra("loca",getItem(p0).loca)
                intenti.putExtra("fb",getItem(p0).fb)
                intenti.putExtra("ferm",getItem(p0).ferm)
                intenti.putExtra("ouv",getItem(p0).ouv)
                intenti.putExtra("etat",etat)
                ctx.startActivity(intenti)
            }
        })*/


        return view
    }

    private class ViewHolder(
        val textView1: TextView,
        val textView2: TextView
        //val textView3: TextView,
       // val imageView: ImageView
    )
    override fun getItem(p0: Int) = data.get(p0)

    override fun getItemId(p0: Int) = data.get(p0).hashCode().toLong()

    override fun getCount() = data.size

}