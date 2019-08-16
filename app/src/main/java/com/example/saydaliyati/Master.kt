package com.example.saydaliyati

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.work.*
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_master.*
import kotlinx.android.synthetic.main.app_bar_master.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnSuccessListener
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.HashMap

/*import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions*/

class Master : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,     AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener , OnMapReadyCallback, pharmacieFragment.OnListFragmentInteractionListener, mapFragment.OnFragmentInteractionListener{
    lateinit private var toggle : ActionBarDrawerToggle
    private var mMap: GoogleMap? = null
    var actualFrag : String = "map"

    /*** pour le map **/
    private var mLatitudeTextView: Double = 0.0// 36.7317697
    private var mLongitudeTextView: Double = 0.0 // 3.1766326
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null

    private var mLocationRequest: LocationRequest? = null
    private val listener: com.google.android.gms.location.LocationListener? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    private var cptLocUp: Int = 0
    private val MaxBeforeUp : Int = 300

    private var locationManager: LocationManager? = null

    private val isLocationEnabled: Boolean
        get() {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)
        }

    /****end map ****/



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.frame_container) as SupportMapFragment
       // mapFragment.getMapAsync(this)

        /*** pour le map ***/

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        Log.e("apii","api")
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.e("locii", "loc")
        Log.d("gggg","uooo");
        checkLocation() //check whether location service is enable or not in your  phone

    /*    if (savedInstanceState == null) {
            // 2
            supportFragmentManager
                // 3
                .beginTransaction()
                // 4
               .add(R.id.frame_container, mapFragment.newInstance(mLatitudeTextView, mLongitudeTextView), "pharmaList")
                // 5
                .commit()
        }*/
        setSupportActionBar(toolbar)

        fabi.setOnClickListener { view ->
            Snackbar.make(view, "Veuillez consulter vos commandes", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

       toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        synchronizePharma()//Pour supprimé de room les pharmacies qui n'existnt plus sur le serveur

        /** pour firebase **/
    /*    val options =  FirebaseOptions.Builder()
    .setCredentials(GoogleCredentials.getApplicationDefault())
    .setDatabaseUrl("https://hello.firebaseio.com/")
    .build()*/

//FirebaseApp.initializeApp(options)

    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        //startLocationUpdates()
          mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
Log.e("cooo", mLocation?.latitude.toString())
        if (mLocation == null) {
            supportFragmentManager
                // 3
                .beginTransaction()
                // 4
                .add(R.id.frame_container, mapFragment.newInstance(mLatitudeTextView, mLongitudeTextView), "nearpharma")

                // 5
                .commitAllowingStateLoss()
            startLocationUpdates()

        }
        if (mLocation != null) {
            mLatitudeTextView = mLocation!!.latitude
            mLongitudeTextView = mLocation!!.longitude
            supportFragmentManager
                // 3
                .beginTransaction()
                // 4
                .add(R.id.frame_container, mapFragment.newInstance(mLatitudeTextView, mLongitudeTextView), "nearpharma")

                // 5
                .commitAllowingStateLoss()
            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConnectionSuspended(i: Int) {
        Log.i("tag1", "Connection Suspended")
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i("tag2", "Connection failed. Error: " + connectionResult.getErrorCode())
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

    @SuppressLint("MissingPermission")
    protected fun startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)
        // Request location updates
        Log.e("info", UPDATE_INTERVAL.toString()+"  "+FASTEST_INTERVAL.toString())
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Log.d("reque", "--->>>>")
    }

    override fun onLocationChanged(location: Location) {
    Log.e("loc","location")
        val msg = "Updated Location: " +
                java.lang.Double.toString(location.latitude) + "," +
                java.lang.Double.toString(location.longitude)
        mLatitudeTextView = location.latitude
        mLongitudeTextView= location.longitude
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        //while(mLatitudeTextView== 0.0 && mLongitudeTextView== 0.0) Log.e("coordinate", mLatitudeTextView.toString()+  "  "+mLongitudeTextView.toString())

        // You can now create a LatLng Object for use with maps
        val latLng = LatLng(location.latitude, location.longitude)

       if(cptLocUp==0 && actualFrag == "map") {
           supportFragmentManager
               // 3
               .beginTransaction()
               // 4
               .add(R.id.frame_container, mapFragment.newInstance(mLatitudeTextView, mLongitudeTextView), "nearpharma")

               // 5
               .commitAllowingStateLoss()
       }
        cptLocUp = cptLocUp + 1
        if(cptLocUp==MaxBeforeUp) cptLocUp = 0
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled)
            showAlert()
        return isLocationEnabled
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
           //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.master, menu)
        return true
    }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                sendNotification()
               // Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {
            R.id.nav_Pharma -> {
                val f = mapFragment.newInstance(mLatitudeTextView, mLongitudeTextView) as Fragment
                replaceFragment(f)
                // Handle the camera action
            }
            R.id.nav_cmd -> {

            }
            R.id.nav_fact -> {

            }
            R.id.nav_profile-> {

            }
            R.id.nav_deco -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }






    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment).addToBackStack("tag").commit();

    }

    private fun enableViews(enable: Boolean, fragment: String) {

        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if (enable && fragment == "list") {
            //You may not want to open the drawer on swipe from the left in this case
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            // Remove hamburger
            toggle.setDrawerIndicatorEnabled(false)
            // Show back button

            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            toggle.setToolbarNavigationClickListener({
                // Doesn't have to be onBackPressed
                onBackPressed()
                enableViews(false, "map")
                //  if(fragment == "list")
                //       enableViews(false, "")
             //   finish();
             //   startActivity(getIntent());

            }
            )
        }
               // mToolBarNavigationListenerIsRegistered = true
            else if (enable && fragment == "detail") {
                //You may not want to open the drawer on swipe from the left in this case
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                // Remove hamburger
                toggle.setDrawerIndicatorEnabled(false)
                // Show back button

                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
                // clicks are disabled i.e. the UP button will not work.
                // We need to add a listener, as in below, so DrawerToggle will forward
                // click events to this listener.
                toggle.setToolbarNavigationClickListener( {
                    // Doesn't have to be onBackPressed
                    onBackPressed()
                    //  if(fragment == "list")
                    //       enableViews(false, "")
                    enableViews(true, "list")

                }
                )

        } else {
            //You must regain the power of swipe for the drawer.
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

            // Remove back button
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true)
            // Remove the/any drawer toggle listener
            toggle.setToolbarNavigationClickListener(null)
          //  mToolBarNavigationListenerIsRegistered = false
        }

        // So, one may think "Hmm why not simplify to:
        // .....
        // getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        // mDrawer.setDrawerIndicatorEnabled(!enable);
        // ......
        // To re-iterate, the order in which you enable and disable views IS important #dontSimplify.
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

    override fun onFragmentInteraction(uri: String){
        Log.e("erreur frag ", "enter")
          val edit = findViewById(R.id.villeNom) as EditText
        var villeArg = "Alger"
         if (edit?.text.toString()!= "")  villeArg = edit?.text.toString()
        val f = pharmacieFragment.newInstance(1, villeArg) as Fragment
        replaceFragment(f)
        actualFrag = "list"
        enableViews(true, "list")

    }

    override fun onListFragmentInteraction(item: pharmacie?){
        Log.e("erreur frag ", "enter")
        // 2
      //  Toast.makeText(this.applicationContext, "helloo"+item?.id, Toast.LENGTH_LONG).show()
        //  drawer_layout.removeDrawerListener()
        val f = pharmaDetailFragment.newInstance(item) as Fragment
        replaceFragment(f)
        enableViews(true, "detail")
        /*drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
           // Remove hamburger
           toggle.setDrawerIndicatorEnabled(false)
           // Show back button
           supportActionBar?.setDisplayHomeAsUpEnabled(true)
           toggle.setToolbarNavigationClickListener( {
                   // Doesn't have to be onBackPressed
                   onBackPressed()
               enableViews(false)

               }
           )*/

    }


    private fun synchronizePharma() {

        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request: OneTimeWorkRequest = OneTimeWorkRequest.Builder(SynchroWorker::class.java)

            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueue(request)
    }

    /** ntification **/
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=" + "AAAAchI0EIs:APA91bEvHzqvkWQElCoHDrSXknS_LVR1T5A7GTfTmApzKCzKn388ELvTdsSVMl1SF0zULYO6A4Bg9Gh9v_-uV7om80E-EFsu7Y5nQK7UGbbNZLGjTnQyf3jQoZ0A4gN8nax5mlsg7z9A"
    private val contentType = "application/json"
    internal val TAG = "NOTIFICATION TAG"

    lateinit internal var NOTIFICATION_TITLE: String
    lateinit internal var NOTIFICATION_MESSAGE: String
    lateinit internal var TOPIC: String



    private fun sendNotification() {

        TOPIC = "/topics/userABC" //topic has to match what the receiver subscribed to
        NOTIFICATION_TITLE = "hello"
        NOTIFICATION_MESSAGE = "congrats"

        val notification = JSONObject()
        val notifcationBody = JSONObject()
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE)
            notifcationBody.put("message", NOTIFICATION_MESSAGE)

            notification.put("to", TOPIC)
            notification.put("data", notifcationBody)
        } catch (e: JSONException) {
            Log.e(TAG, "onCreate: " + e.message)
        }
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener { response ->
                Log.i(TAG, "onResponse: $response")

            },
            Response.ErrorListener {
                Toast.makeText(this@Master, "Request error", Toast.LENGTH_LONG).show()
                Log.i(TAG, "onErrorResponse: Didn't work")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest)
        fabi.isVisible = true
    }


}
