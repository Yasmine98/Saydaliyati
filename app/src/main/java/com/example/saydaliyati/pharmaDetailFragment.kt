package com.example.saydaliyati

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar


class pharmaDetailFragment : Fragment() {
    lateinit var pharm : pharmacie
    companion object {
        fun newInstance(pharmacie: pharmacie?) = pharmaDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable("pharmacie", pharmacie)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pharm =  arguments?.getSerializable("pharmacie") as pharmacie
        //  Toast.makeText(activity?.applicationContext, "what", Toast.LENGTH_LONG).show()
        Log.e("erreur retrofit", "error"+pharm?.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
   /*     val toolbar = view?.findViewById(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_button)
        toolbar.setNavigationOnClickListener(View.OnClickListener { activity!!.onBackPressed() })*/
        return inflater.inflate(R.layout.pharma_detail_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("erreur retrofit", "errora"+pharm?.id)
        val textView1 = view?.findViewById(R.id.idP) as TextView
        val textView2 = view?.findViewById(R.id.nomP) as TextView
        textView1.setText(pharm.id.toString())
        textView2.setText(pharm.nom)  // find your view elements and do stuff here
    }


}
