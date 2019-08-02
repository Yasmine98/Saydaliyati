package com.example.saydaliyati

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
import com.example.saydaliyati.R

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent






/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [pharmacieFragment.OnListFragmentInteractionListener] interface.
 */
class pharmacieFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    lateinit var v : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
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
        val call = RetrofitService.endpoint.getPharma()
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
                    Toast.makeText(getActivity()?.getApplicationContext(), list_name[0], Toast.LENGTH_LONG).show()

                    // Set the adapter
                    if (view is RecyclerView) {
                        with(view) {
                            layoutManager = when {
                                columnCount <= 1 -> androidx.recyclerview.widget.LinearLayoutManager(context)
                                else -> androidx.recyclerview.widget.GridLayoutManager(context, columnCount)
                            }
                            adapter = com.example.saydaliyati.MypharmacieRecyclerViewAdapter(list, listener)

                        }
                    }
                    // val adapter = ArrayAdapter(this@Affich_res, android.R.layout.simple_list_item_1, list_name)
                    //  list_.adapter = adapter
                }
            }
            override fun onFailure(call: Call<List<pharmacie>>?, t: Throwable?) {
                //   progressBar.visibility = View.INVISIBLE
                Log.e("erreur retrofit", t. toString())
                Toast.makeText(getActivity()?.getApplicationContext(), "failure", Toast.LENGTH_LONG).show()
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

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            pharmacieFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
