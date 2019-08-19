package com.example.rofaida.saydaliyati

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.rofaida.saydaliyati.R

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.rofaida.saydaliyati.Interfaces.RetrofitService
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_master.*
import kotlinx.android.synthetic.main.app_bar_master.*


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [pharmacieFragment.OnListFragmentInteractionListener] interface.
 */
class pharmacieFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private var ville = "Alger"
    private var listener: OnListFragmentInteractionListener? = null

    lateinit var v : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            ville = it.getString(ARG_VILLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_pharmacie_list, container, false)

        return v


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       // prepareDrawer()
        val a = activity as Master
        a?.actualFrag = "list"
        // mToolBarNavigationListenerIsRegistered = true
      //  val edit = a.findViewById(R.id.villeNom) as EditText

       // if (edit?.text.toString()!= "")  ville = edit?.text.toString()
       // Log.e("villlle",edit?.text.toString() )
        val call = RetrofitService.endpoint.pharma_vile(ville)
        // progressBar.visibility = View.VISIBLE
        call.enqueue(object: Callback<List<pharmacie>> {
            override fun onResponse(call: Call<List<pharmacie>>?, response:
            Response<List<pharmacie>>?) {

                if(response?.isSuccessful!!){
                    //  progressBar.visibility = View.INVISIBLE
                    val list:List<pharmacie> = response.body()!!
                    val list_name = MutableList<String>(list.size){""}
                    for(i in 0 until list.size){
                        list_name[i] = list[i].nom
                    }
                    val view = v
                   // Toast.makeText(getActivity()?.getApplicationContext(), list_name[0], Toast.LENGTH_LONG).show()

                    // Set the adapter
                    if (view is RecyclerView) {
                        with(view) {
                            layoutManager = when {
                                columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                                else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
                            }
                            adapter = com.example.rofaida.saydaliyati.MypharmacieRecyclerViewAdapter(list, listener)

                        }
                    }
                    // val adapter = ArrayAdapter(this@Affich_res, android.R.layout.simple_list_item_1, list_name)
                    //  list_.adapter = adapter
                }
            }
            override fun onFailure(call: Call<List<pharmacie>>?, t: Throwable?) {
                //   progressBar.visibility = View.INVISIBLE
                Log.e("erreur retrofit", t. toString())
                //Toast.makeText(getActivity()?.getApplicationContext(), "Impossible d'accèder au serveur", Toast.LENGTH_LONG).show()

                val list:List<pharmacie> = AppDatabase.getInstance(activity!!.applicationContext).getPharmaDao().getpharmaByville(ville_name = ville)
                val list_name = MutableList<String>(list.size){""}
                for(i in 0 until list.size){
                    list_name[i] = list[i].nom
                }
                val view = v
             //   Toast.makeText(getActivity()?.getApplicationContext(), list_name[0], Toast.LENGTH_LONG).show()

                // Set the adapter
                if (view is RecyclerView) {
                    with(view) {
                        layoutManager = when {
                            columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                            else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
                        }
                        adapter = com.example.rofaida.saydaliyati.MypharmacieRecyclerViewAdapter(list, listener)

                    }
                }

            }
        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: pharmacie?)
    }


    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_VILLE = "ville"
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, ville: String) =
            pharmacieFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putString(ARG_VILLE, ville)
                }
            }
    }

    private fun prepareDrawer(){

        val  drawer_layout = activity?.findViewById(R.id.drawer_layout) as DrawerLayout
        var toggle = ActionBarDrawerToggle(
            activity, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        // Remove hamburger
        toggle.setDrawerIndicatorEnabled(false)
        // Show back button
        var a = activity as AppCompatActivity
        a.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
        // clicks are disabled i.e. the UP button will not work.
        // We need to add a listener, as in below, so DrawerToggle will forward
        // click events to this listener.
        toggle.setToolbarNavigationClickListener( {
            // Doesn't have to be onBackPressed
            //activity?.supportFragmentManager?.popBackStack()
            a.onBackPressed()

        }
        )

    }
}
