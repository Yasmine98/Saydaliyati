package com.example.saydaliyati

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.saydaliyati.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [mapFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [mapFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class mapFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
     var  mMap  : GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_map, container, false)

      //  var mMapF = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
     //   mMapF.getMapAsync(this)
        // Inflate the layout for this fragment
        return v
    }

    override fun onActivityCreated( savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val fm = activity!!.supportFragmentManager/// getChildFragmentManager();
        var supportMapFragment = fm.findFragmentById(R.id.map) as SupportMapFragment
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance()
            fm.beginTransaction().replace(R.id.frame_container, supportMapFragment).commit()
        }
        supportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Dhaka, Bangladesh, and move the camera.
        val dhaka = LatLng(23.777176, 90.399452)
        mMap?.let {
            it.addMarker(MarkerOptions().position(dhaka).title("Marker in Dhaka"))
            it.moveCamera(CameraUpdateFactory.newLatLng(dhaka))
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: String) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment mapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            mapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
