package com.example.saydaliyati

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import com.example.saydaliyati.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import kotlinx.android.synthetic.main.activity_master.*
import kotlinx.android.synthetic.main.app_bar_master.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
class mapFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    // TODO: Rename and change types of parameters
    private var param1: Double = 0.0
    private var param2: Double = 0.0
    private var listener: OnFragmentInteractionListener? = null
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item: String =  "hi" as String
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.

            listener?.onFragmentInteraction(item)
        }
    }
     var  mMap  : GoogleMap? = null
    private var mLatitudeTextView: TextView? = null
    private var mLongitudeTextView: TextView? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null

    private var mLocationRequest: LocationRequest? = null
    private val listener2: com.google.android.gms.location.LocationListener? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    private var locationManager: LocationManager? = null








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("instance","hi")
        arguments?.let {
            param1 = it.getDouble(ARG_PARAM1)
            param2 = it.getDouble(ARG_PARAM2)
        }
      //  mLatitudeTextView = (TextView) findViewById((R.id.latitude_textview));
       // mLongitudeTextView = (TextView) findViewById((R.id.longitude_textview));



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prepareDrawer()
        val v = inflater.inflate(R.layout.fragment_map, container, false)
       mGoogleApiClient = GoogleApiClient.Builder(activity!!)
            //           .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();

        mLocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
      //  var mMapF = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
     //   mMapF.getMapAsync(this)
        // Inflate the layout for this fragment


        return v
    }

    override fun onActivityCreated( savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val a = activity as Master
        a?.actualFrag = "map"
        val fm = activity!!.supportFragmentManager/// getChildFragmentManager();
      // var supportMapFragment = fm.findFragmentById(R.id.map) as SupportMapFragment
      //  if (supportMapFragment == null) {
          var  supportMapFragment = SupportMapFragment.newInstance()
            fm.beginTransaction().replace(R.id.map_container, supportMapFragment).commit()
        supportMapFragment.getMapAsync(this)
        val but = activity?.findViewById(R.id.viewPharma) as Button
        val ville = activity?.findViewById(R.id.villeNom) as EditText
        but.setOnClickListener(mOnClickListener)
       prepareDrawer()

    }

    override fun onMapReady(googleMap: GoogleMap) {


        //Adding the created the marker on the map

        mMap = googleMap

        // Add a marker in Dhaka, Bangladesh, and move the camera.
        var nearPharma: NearPharma = NearPharma()


        /**get pharùa **/
        lateinit var list:List<pharmacie>
        val call = RetrofitService.endpoint.getPharma()
        // progressBar.visibility = View.VISIBLE
        call.enqueue(object: Callback<List<pharmacie>> {
            override fun onResponse(call: Call<List<pharmacie>>?, response:
            Response<List<pharmacie>>?) {

                if (response?.isSuccessful!!) {
                    //  progressBar.visibility = View.INVISIBLE
                    list = response.body()!!
                    nearPharma.fill(param1, param2, list)
                    val dhaka = LatLng(23.777176, 90.399452)
                    mMap?.let {
                        it.addMarker(MarkerOptions().position(LatLng(param1, param2)).title("Je suis là!"))

                       // it.moveCamera(CameraUpdateFactory.newLatLng(LatLng(param1, param2)))
                       // it.animateCamera(CameraUpdateFactory.zoomTo(14.0f), 2000, null);

                    }
                    for(p in nearPharma.listNearPharma)
                    {
                        val coord  = LatLng(p.value.lat, p.value.longi)
                        mMap?.let{it.addMarker(MarkerOptions().position(coord).title(p.value.nom))}
                    }
                }
            }
                override fun onFailure(call: Call<List<pharmacie>>?, t: Throwable?) {
                    //   progressBar.visibility = View.INVISIBLE
                    Log.e("erreur retrofit", t. toString())
                    Toast.makeText(getActivity()?.getApplicationContext(), "Impossible d'accèder au serveur", Toast.LENGTH_LONG).show()
                     list = AppDatabase.getInstance(activity!!.applicationContext).getPharmaDao().getpharma()
                    nearPharma.fill(param1, param2, list)
                    val dhaka = LatLng(23.777176, 90.399452)
                    mMap?.let {
                        it.addMarker(MarkerOptions().position(LatLng(param1, param2)).title("Je suis là!"))

                       // it.moveCamera(CameraUpdateFactory.newLatLng(LatLng(param1, param2)))
                        //it.animateCamera(CameraUpdateFactory.zoomTo(14.0f), 2000, null);

                    }
                    for(p in nearPharma.listNearPharma)
                    {
                        val coord  = LatLng(p.value.lat, p.value.longi)
                        mMap?.let{it.addMarker(MarkerOptions().position(coord).title(p.value.nom))}
                    }

                }
            })



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
        fun newInstance(param1: Double, param2: Double) =
            mapFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_PARAM1, param1)
                    putDouble(ARG_PARAM2, param2)
                }
            }
    }

  //  @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        startLocationUpdates()

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

        if (mLocation == null) {
            startLocationUpdates()
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
            Toast.makeText(activity?.applicationContext, "Location not Detected", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(activity?.applicationContext, "Location not Detected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConnectionSuspended(i: Int) {
        Log.e(TAG, "Connection Suspended")
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e(TAG, "Connection failed. Error: " + connectionResult.getErrorCode())
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected()) {
            mGoogleApiClient!!.disconnect()
        }
    }
    override fun onLocationChanged(location: Location) {

        val msg = "Updated Location: " +
                java.lang.Double.toString(location.latitude) + "," +
                java.lang.Double.toString(location.longitude)
        mLatitudeTextView!!.text = location.latitude.toString()
        mLongitudeTextView!!.text = location.longitude.toString()
        Toast.makeText(activity?.applicationContext, msg, Toast.LENGTH_SHORT).show()
        // You can now create a LatLng Object for use with maps
        val latLng = LatLng(location.latitude, location.longitude)
    }
  //  @SuppressLint("MissingPermission")
    protected fun startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)
        // Request location updates
        if (ActivityCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
            mLocationRequest, this)
        Log.e("reque", "--->>>>")
    }

    private fun prepareDrawer()
    {
       val a = activity as AppCompatActivity
        val  drawer_layout = activity?.findViewById(R.id.drawer_layout) as DrawerLayout
        var toggle = ActionBarDrawerToggle(
            activity, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
       // Toast.makeText(activity?.applicationContext, "map", Toast.LENGTH_SHORT)
        Log.e("drawr", "drawer")
        // Remove back button
        a.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        // Show hamburger
        toggle.setDrawerIndicatorEnabled(true)
        // Remove the/any drawer toggle listener
        toggle.setToolbarNavigationClickListener(null)
    /*    drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        val a = activity as AppCompatActivity
        // Remove back button
         a?.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        // Show hamburger
        toggle.setDrawerIndicatorEnabled(true)
        // Remove the/any drawer toggle listener
        toggle.setToolbarNavigationClickListener(null)*/
    }
}
